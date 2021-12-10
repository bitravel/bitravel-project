package com.bitravel.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
import com.bitravel.data.entity.TravelImage;
import com.bitravel.data.repository.TravelImageRepository;
import com.bitravel.data.repository.TravelRepository;
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

	private final TravelRepository travelRepository;
	private final TravelImageRepository travelImageRepository;

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
		pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "travelId"));
		return travelRepository.findAll(pageable);
	}

	/**
	 * 여행지 상세 정보 조회 (여행지 ID)
	 */
	@Transactional
	public Travel detailById(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		addImage(entity);
		return entity;
	}

	/**
	 * 여행지 목록 조회 (여행지 이름)
	 */
	@Transactional
	public List<TravelSimpleDto> detailsByName(String name) {
		List<Travel> list = travelRepository.findByTravelNameContaining(name);
		if(list.size() == 0) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		for(int i=0;i<list.size();i++) {
			addImage(list.get(i));
		}

		return list.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}

	/**
	 * 여행지 목록 조회 (광역자치단체)
	 */
	@Transactional
	public Page<Travel> detailsByLargeGov(String largeGov, Pageable pageable) {
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "travelId"));
		
		return travelRepository.findByLargeGov(largeGov, pageable);
	}
	
	/**
	 * 여행지 목록 조회 (광역자치단체) non-pageable
	 */
	@Transactional
	public List<Travel> detailsByLargeGov(String largeGov) {
		List<Travel> list = travelRepository.findByLargeGov(largeGov);
		if(list.size() == 0) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		for(int i=0;i<list.size();i++) {
			addImage(list.get(i));
		}
		
		return list;
	}
	
	/**
	 * 여행지 목록 조회 (기초자치단체)
	 */
	@Transactional
	public Page<Travel> detailsBySmallGov(String largeGov, String smallGov, Pageable pageable) {
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "travelId"));
		
		return travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, pageable);
	}
	
	/**
	 * 여행지 목록 조회 (기초자치단체)
	 */
	@Transactional
	public List<Travel> detailsBySmallGov(String largeGov, String smallGov) {
		List<Travel> list = travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov);
		if(list.size() == 0) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		for(int i=0;i<list.size();i++) {
			addImage(list.get(i));
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
	
	private void addImage (Travel entity) {
		if(entity.getTravelImage()==null) {
			Optional<TravelImage> ti = travelImageRepository.findByIsUpdatedAndTravelName(false, entity.getTravelName());
			if(ti.isPresent()) {
				entity.setTravelImage(ti.get().getTravelImage());
				travelRepository.save(entity);
				ti.get().setUpdated(true);
				travelImageRepository.save(ti.get());
			}	
		}
	}
}
