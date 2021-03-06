package com.bitravel.jwt;

import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.bitravel.data.entity.User;
import com.bitravel.data.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
	// 토큰 생성 시 내부에서 토큰 내용을 나타낼 이름
	private static final String AUTHORITIES_KEY = "auth";

	// 토큰의 유효성 확인에 필요한 비밀 암호문. 오로지 서버 안에만 존재함
	private final String secret;
	// 토큰의 유효기간 설정값
	private final long tokenValidityInMilliseconds;
	// 로그아웃된 토큰들을 보관하는 redis db에 접근하는 repository
	@Autowired
	private final StringRedisTemplate redisTemplate;

	@Autowired
	private UserRepository userRepository;

	// 비밀 암호문을 한 번 더 암호화한 키
	private Key key;

	// 이 클래스 생성 시 값을 application.properties 파일에서 가져옴
	public TokenProvider(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds
			) {
		this.secret = secret;
		this.tokenValidityInMilliseconds = tokenValidityInSeconds*1000;
		this.redisTemplate = new StringRedisTemplate();
	}

	// 클래스가 만들어진 직후 바로 실행되어 키값도 만들어냄
	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// 새로운 jwt를 만드는 메소드
	public String createToken(Authentication authentication) {
		// 인증 정보에 저장된 해당 회원의 권한 정보를 불러옴
		String authorites = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		// 현재 시간 기준으로 토큰 만료 일시를 지정함
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);
		// 위의 결과를 토대로 새로운 jwt를 하나 만들어냄
		return Jwts.builder().setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorites)
				.setIssuedAt(new Date(now))
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}

	public void createCookie(HttpServletResponse response, String token) {
		try {
			ResponseCookie cookie = ResponseCookie.from(JwtFilter.AUTHORIZATION_HEADER, URLEncoder.encode(token, "UTF-8"))
					.httpOnly(true)
					.secure(false)
					.maxAge(tokenValidityInMilliseconds/1000)
					.path("/")
					.build();
			response.addHeader("Set-Cookie", cookie.toString());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

	public Authentication getAuthentication(String token) {
		// client에서 받아온 토큰 문자열을 분해하여 필요한 값만 찾음
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		// 해당 회원의 권한 정보를 얻음
		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect((Collectors.toSet()));
		// 메일, 본명, 닉네임, 포인트, 성별, 나이, 가입일자, 권한 정보 등 회원 객체를 임시로 생성
		User principal = new User(claims.getSubject(), authorities);
		User user = userRepository.findOneWithAuthoritiesByEmail(principal.getEmail()).get();
		principal.setUserId(user.getUserId());
		principal.setNickname(user.getNickname());
		principal.setPoint(user.getPoint());
		principal.setAge(user.getAge());
		principal.setGender(user.getGender());
		principal.setUserImage(user.getUserImage());
		principal.setUserLargeGov(user.getUserLargeGov());
		principal.setUserSmallGov(user.getUserSmallGov());
		// 해당 유저에 대한 권한을 부여하여 반환
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) {
		try {
			// Client의 header에서 가져온 토큰을 분해하여 정보 추출
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			// 로그아웃된 토큰 저장소에 존재하는 토큰인지 확인
			ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
			if(logoutValueOperations.get(token) != null) {
				log.info("로그아웃된 토큰");
				return false;
			}
			// 토큰 유효기간이 끝나지 않았다면 true를 반환
			return !claims.getBody().getExpiration().before(new Date());
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			// secret 키가 서버에서 지정한 것과 다른 경우 또는 토큰 생성 방식 자체가 아예 다른 경우 발생 -> 해킹 시도
			log.info("잘못된 jwt 서명");
		} catch (ExpiredJwtException e) {
			log.info("만료된 jwt 토큰");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 jwt 토큰");
		} catch (IllegalArgumentException e) {
			log.info("잘못된 jwt 토큰");
		}
		return false;
	}
}
