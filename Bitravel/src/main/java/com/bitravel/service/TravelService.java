package com.bitravel.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.TravelSimpleDto;
import com.bitravel.data.dto.WeatherDto;
import com.bitravel.data.entity.Travel;
import com.bitravel.data.entity.TravelCountAge;
import com.bitravel.data.entity.TravelCountGender;
import com.bitravel.data.entity.TravelImage;
import com.bitravel.data.entity.UserTravel;
import com.bitravel.data.repository.TravelCountAgeRepository;
import com.bitravel.data.repository.TravelCountGenderRepository;
import com.bitravel.data.repository.TravelImageRepository;
import com.bitravel.data.repository.TravelRepository;
import com.bitravel.data.repository.UserTravelRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {
	
	private final UserTravelRepository userTravelRepository;
	private final TravelRepository travelRepository;
	private final TravelImageRepository travelImageRepository;
	private final TravelCountAgeRepository travelCountAgeRepository;
	private final TravelCountGenderRepository travelCountGenderRepository;

	/**
	 * 여행지 생성
	 */
	@Transactional
	public Long save(Travel params) {

		Travel entity = travelRepository.save(params);
		return entity.getTravelId();
	}

	/**
	 * 여행지 리스트 조회
	 */
	public Page<Travel> findAll(Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		pageable = PageRequest.of(page, 9, sort);
		return travelRepository.findAll(pageable);
	}
	
	/**
	 * 내가 가장 선호하는 카테고리의 여행지 리스트
	 */
	public Page<Travel> findFavoriteCategoryList (Pageable pageable) {
		String myEmail = SecurityUtil.getCurrentEmail().get();
		List<UserTravel> utlist = userTravelRepository.findByUserEmail(myEmail);
		HashMap<String, Integer> userPref = new HashMap<>();
		
		for(int i=0;i<utlist.size();i++) {
			Travel now = travelRepository.getById(utlist.get(i).getTravelId());
			// 카테고리별 개수 세기
			if(now.getMiddleCategory()!=null && now.getMiddleCategory().trim().length()>0) {
				userPref.put(now.getSmallCategory(), userPref.getOrDefault(userPref.get(now.getSmallCategory()), 0)+1);
			}
		}
		
		Iterator<String> mapIter = userPref.keySet().iterator();
		String result = "";
		int max = 0;
		while(mapIter.hasNext()) {
			String key = mapIter.next();
			Integer value = userPref.get(key);
			if(value>max) {
				max = value;
				result = key;
			}		
		}
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		pageable = PageRequest.of(page, 9, sort);
		return travelRepository.findBySmallCategory(result, pageable);
	}
	
	/**
	 * 나와 비슷한 나이/성별의 사람들이 선호하는 지역 확인
	 */
	@Transactional(readOnly = true)
	public List<Travel> findSimiliarAgeGenderTaste(int age, String gender) {
		age = age/10;
		if(age==0)
			age=1;
		else if (age>7)
			age=7;
		String ageKey = age*10 + "대";
		TravelCountAge tca = travelCountAgeRepository.getById(ageKey);
		TravelCountAge allAge = travelCountAgeRepository.getById("전체");
		
		if(gender.equals("Man")) {
			gender = "남자";
		} else if(gender.equals("Woman")) {
			gender = "여자";
		} else {
			gender = "전체";
		}
		TravelCountGender tcg = travelCountGenderRepository.getById(gender);
		
		TreeMap<Double, String> rank = new TreeMap<>();
		
		if(Math.pow(tca.getSeoul()*tcg.getSeoul(),0.5)>allAge.getSeoul()) {
			rank.put(tca.getSeoul()*tcg.getSeoul()/allAge.getSeoul(), "서울");
		}
		if(Math.pow(tca.getBusan()*tcg.getBusan(),0.5)>allAge.getBusan()) {
			rank.put(tca.getBusan()*tcg.getBusan()/allAge.getBusan(), "부산");
		}
		if(Math.pow(tca.getDaegu()*tcg.getDaegu(),0.5)>allAge.getDaegu()) {
			rank.put(tca.getDaegu()*tcg.getDaegu()/allAge.getDaegu(), "대구");
		}
		if(Math.pow(tca.getDaejeon()*tcg.getDaejeon(),0.5)>allAge.getDaejeon()) {
			rank.put(tca.getDaejeon()*tcg.getDaejeon()/allAge.getDaejeon(), "대전");
		}
		if(Math.pow(tca.getGwangju()*tcg.getGwangju(),0.5)>allAge.getGwangju()) {
			rank.put(tca.getGwangju()*tcg.getGwangju()/allAge.getGwangju(), "광주");
		}
		if(Math.pow(tca.getIncheon()*tcg.getIncheon(),0.5)>allAge.getIncheon()) {
			rank.put(tca.getIncheon()*tcg.getIncheon()/allAge.getIncheon(), "인천");
		}
		if(Math.pow(tca.getUlsan()*tcg.getUlsan(),0.5)>allAge.getUlsan()) {
			rank.put(tca.getUlsan()*tcg.getUlsan()/allAge.getUlsan(), "울산");
		}
		if(Math.pow(tca.getSejong()*tcg.getSejong(),0.5)>allAge.getSejong()) {
			rank.put(tca.getSejong()*tcg.getSejong()/allAge.getSejong(), "세종");
		}
		if(Math.pow(tca.getGyeonggi()*tcg.getGyeonggi(),0.5)>allAge.getGyeonggi()) {
			rank.put(tca.getGyeonggi()*tcg.getGyeonggi()/allAge.getGyeonggi(), "경기");
		}
		if(Math.pow(tca.getGangwon()*tcg.getGangwon(),0.5)>allAge.getGangwon()) {
			rank.put(tca.getGangwon()*tcg.getGangwon()/allAge.getGangwon(), "강원");
		}
		if(Math.pow(tca.getChungbuk()*tcg.getChungbuk(),0.5)>allAge.getChungbuk()) {
			rank.put(tca.getChungbuk()*tcg.getChungbuk()/allAge.getChungbuk(), "충북");
		}
		if(Math.pow(tca.getChungnam()*tcg.getChungnam(),0.5)>allAge.getChungnam()) {
			rank.put(tca.getChungnam()*tcg.getChungnam()/allAge.getChungnam(),"충남");
		}
		if(Math.pow(tca.getJeonbuk()*tcg.getJeonbuk(),0.5)>allAge.getJeonbuk()) {
			rank.put(tca.getJeonbuk()*tcg.getJeonbuk()/allAge.getJeonbuk(),"전북");
		}
		if(Math.pow(tca.getJeonnam()*tcg.getJeonnam(),0.5)>allAge.getJeonnam()) {
			rank.put(tca.getJeonnam()*tcg.getJeonnam()/allAge.getJeonnam(), "전남");
		}
		if(Math.pow(tca.getGyeongbuk()*tcg.getGyeongbuk(),0.5)>allAge.getGyeongbuk()) {
			rank.put(tca.getGyeongbuk()*tcg.getGyeongbuk()/allAge.getGyeongbuk(), "경북");
		}
		if(Math.pow(tca.getGyeongnam()*tcg.getGyeongnam(),0.5)>allAge.getGyeongnam()) {
			rank.put(tca.getGyeongnam()*tcg.getGyeongnam()/allAge.getGyeongnam(), "경남");
		}
		if(Math.pow(tca.getJeju()*tcg.getJeju(),0.5)>allAge.getJeju()) {
			rank.put(tca.getJeju()*tcg.getJeju()/allAge.getJeju(), "제주");
		}
		
		List<Travel> travel = new ArrayList<>();		
		Double prev = rank.firstKey();
		for(int i=1;i<rank.size();i++) {
			prev = rank.floorKey(prev);
			travel.addAll(travelRepository.findByLargeGov(rank.get(prev)));
			if(i>=2) {
				break;
			} 
		}
		return travel;
	}
	
	
	/**
	 * 여행지 이름 검색 결과 조회
	 */
	public Page<Travel> findByName(String keyword, Pageable pageable) {
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, sort);
		return travelRepository.findByTravelNameContaining(keyword, pageable);
	}

	/**
	 * 여행지 상세 정보 조회 (여행지 ID)
	 */
	@Transactional
	public Travel findById(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		entity.increaseView();
		addImage(entity);
		return entity;
	}
	
	/**
	 * 전체 여행지 방문 순위
	 */
//	@Transactional
//	public List<Travel> findRankingOfAll() {
		//List<>
		
//	}
	
	/**
	 * 조회수 증가 없이 여행지 상세 정보 조회 (여행지 ID)
	 */
	@Transactional
	public Travel findByIdNoViewCount(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		return entity;
	}

	/**
	 * 여행지 목록 조회 (여행지 이름)
	 */
	@Transactional
	public List<TravelSimpleDto> findByName(String name) {
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		List<Travel> list = travelRepository.findByTravelNameContaining(name, sort);

		return list.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}

	/**
	 * 여행지 목록 조회 (광역자치단체)
	 */
	@Transactional
	public Page<Travel> findByLargeGov(String largeGov, Pageable pageable) {
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "travelId"));
		
		return travelRepository.findByLargeGov(largeGov, pageable);
	}
	
	/**
	 * 여행지 목록 조회 (광역자치단체) non-pageable
	 */
	@Transactional
	public List<Travel> findByLargeGov(String largeGov) {
		List<Travel> list = travelRepository.findByLargeGov(largeGov);
		if(list.size() == 0) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		return list;
	}
	
	/**
	 * 여행지 목록 조회 (기초자치단체)
	 */
	@Transactional
	public Page<Travel> findBySmallGov(String largeGov, String smallGov, Pageable pageable) {
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "travelId"));
		
		return travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, pageable);
	}
	
	/**
	 * 여행지 목록 조회 (기초자치단체)
	 */
	@Transactional
	public List<Travel> findBySmallGov(String largeGov, String smallGov) {
		List<Travel> list = travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov);
		if(list.size() == 0) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		return list;
	}

	/**
	 * 지역별 인기 여행지 조회 (Simple DTO)
	 */
	public List<TravelSimpleDto> findTravelsByRegion(String largeGov, String smallGov) {
		Sort sort = Sort.by(Direction.ASC, "travelView", "travelName");
		return travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, sort)
				.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}

	/**
	 * 여행지 수정
	 */
	@Transactional
	public Boolean update(final Long id, final Travel params) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		// 글쓴이 또는 admin만 수정할 수 있게 함 (개발 단계에서는 익명 유저도 가능하게 처리)
		if(SecurityUtil.getCurrentEmail().get().equals("admin")||SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
			log.info("관리자 권한으로 글을 수정합니다. 여행지 번호 : "+id);
		} else {
			log.info("유효하지 않은 수정 요청입니다.");
			return false;
		}
		entity.update(params);
		return true;
	}

	/**
	 * 여행지 삭제
	 */
	@Transactional
	public Boolean deleteById(Long id) {
		travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND)); 
		// 글쓴이 또는 admin만 수정할 수 있게 함
		if(SecurityUtil.getCurrentEmail().get().equals("admin")||SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
			log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
		} else {
			log.info("유효하지 않은 삭제 요청입니다.");
			return false;
		}
		travelRepository.deleteById(id);
		return true;
	}

	public String weathersShortByTravel(final WeatherDto now) throws IOException, JSONException {
		String myWeather = 	
				"k9ex5ipQp8k%2Baiet3GfC015PcRbjkuEv%2Bq8XD2ScEoT0CMfyyZgG5%2BjRCpsuFqQ2LFtwGcZdiDuigKZLvnn7yg%3D%3D";
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=";
		url += myWeather;
		url += "&pageNo=1&numOfRows=650&dataType=JSON&base_date=";
		url += now.getDayShort();
		url += "&base_time=";
		url += now.getTimeShort();
		url += "&nx=";
		url += Math.round(Double.parseDouble(now.getLatitude()));
		url += "&ny=";
		url += Math.round(Double.parseDouble(now.getLongitude()));
		log.info(url);
		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8");

		log.info("Successfully got the information from Weather api (short)");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("weather", resultMap);

		return jsonObj.toString();
	}

	public String weathersMiddleByTravel(final WeatherDto now) throws IOException, JSONException {
		String myWeather = 	
				"k9ex5ipQp8k%2Baiet3GfC015PcRbjkuEv%2Bq8XD2ScEoT0CMfyyZgG5%2BjRCpsuFqQ2LFtwGcZdiDuigKZLvnn7yg%3D%3D";
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey=";
		url += myWeather;
		url += "&pageNo=1&numOfRows=10&dataType=JSON&regId=";
		url += now.getRegionId();
		url += "&tmFc=";
		url += now.getTimeMiddle();
		log.info(url);
		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8");

		log.info("Successfully got the information from Weather api (middle)");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("weather", resultMap);

		return jsonObj.toString();
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> getDataFromJson(String url, String encoding) throws IOException {
		URL apiURL = new URL(url);
		HttpURLConnection conn = null;
		BufferedReader br = null;

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			conn = (HttpURLConnection) apiURL.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(7000);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.connect();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			String line = null;
			StringBuffer result = new StringBuffer();			
			while((line=br.readLine())!=null)
				result.append(line);
			ObjectMapper mapper = new ObjectMapper();

			resultMap = mapper.readValue(result.toString(), HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) conn.disconnect();
			if(br != null) br.close();
		}
		return resultMap;
	}
	
	// 내가 아직 방문하지 않은 특정 광역자치단체의 여행지 검색
	@Transactional(readOnly = true)
	public List<TravelSimpleDto> findNotVisitedTravelByLargeGov(String largeGov, Long id) {
		String myEmail = SecurityUtil.getCurrentEmail().get();
		List<UserTravel> list = userTravelRepository.findByUserEmailAndIsVisited(myEmail, false);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		List<Travel> all = travelRepository.findByLargeGov(largeGov, sort);
		HashSet<Long> visitedSet = new HashSet<>();
		visitedSet.add(id);
		
		for(int i=0;i<list.size();i++) {
			Travel now = travelRepository.findById(list.get(i).getTravelId()).get();
			if(now==null) {
				continue;
			} else if(now.getLargeGov().equals(largeGov)) {
				visitedSet.add(now.getTravelId());
			}	
		}
		
		for(int i=0;i<all.size();i++) {
			if(visitedSet.contains(all.get(i).getTravelId())) {
				all.remove(i);
			}
		}
		
		if(all.size()>15)
			return all.subList(0, 15).stream().map(TravelSimpleDto::new).collect(Collectors.toList());
		else
			return all.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}
	
	// 내가 아직 방문하지 않은 특정 기초자치단체의 여행지 검색
	@Transactional(readOnly = true)
	public List<TravelSimpleDto> findNotVisitedTravelBySmallGov(String largeGov, String smallGov, Long id) {
		String myEmail = SecurityUtil.getCurrentEmail().get();
		List<UserTravel> list = userTravelRepository.findByUserEmailAndIsVisited(myEmail, false);
		
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		List<Travel> all = travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, sort);
		HashSet<Long> visitedSet = new HashSet<>();
		visitedSet.add(id);
		for(int i=0;i<list.size();i++) {
			Travel now = travelRepository.findById(list.get(i).getTravelId()).get();
			if(now==null) {
				continue;
			} else if(now.getSmallGov().equals(smallGov)) {
				visitedSet.add(now.getTravelId());
			}	
		}
		
		for(int i=0;i<all.size();i++) {
			if(visitedSet.contains(all.get(i).getTravelId())) {
				all.remove(i);
			}
		}
		
		if(all.size()>15)
			return all.subList(0, 15).stream().map(TravelSimpleDto::new).collect(Collectors.toList());
		else
			return all.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}
	
	private void addImage (Travel entity) {
		if(entity.getTravelImage()==null) {
			List<TravelImage> ti = travelImageRepository.findByIsUpdatedAndTravelName(false, entity.getTravelName());
			if(ti.size()==1) {
				entity.setTravelImage(ti.get(0).getTravelImage());
				travelRepository.save(entity);
				ti.get(0).setUpdated(true);
				travelImageRepository.save(ti.get(0));
			}
		}
	}
	
}
