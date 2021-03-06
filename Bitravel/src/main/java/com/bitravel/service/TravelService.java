package com.bitravel.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
	 * ????????? ??????
	 */
	@Transactional
	public Long save(Travel params) {

		Travel entity = travelRepository.save(params);
		return entity.getTravelId();
	}

	/**
	 * ????????? ????????? ??????
	 */
	public Page<Travel> findAll(Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		pageable = PageRequest.of(page, 9, sort);
		return travelRepository.findAll(pageable);
	}

	/**
	 * ?????? ?????? ???????????? ??????????????? ????????? ?????????
	 */
	public Page<Travel> findFavoriteCategoryList (Pageable pageable) {
		String myEmail = SecurityUtil.getCurrentEmail().get();
		List<UserTravel> utlist = userTravelRepository.findByUserEmailAndIsLiked(myEmail, true);
		HashMap<String, Integer> userPref = new HashMap<>();

		for(int i=0;i<utlist.size();i++) {
			Travel now = travelRepository.getById(utlist.get(i).getTravelId());
			// ??????????????? ?????? ??????
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
		pageable = PageRequest.of(page, 20, sort);
		return travelRepository.findBySmallCategory(result, pageable);
	}

	/**
	 * ???????????? ?????? ???????????? ???????????? ??????
	 */
	@Transactional(readOnly = true)
	public List<Travel> findUsersLike() {
		List<UserTravel> utlist = userTravelRepository.findByIsLiked(true);
		HashMap<Long, Long> userPref = new HashMap<>();

		for(int i=0;i<utlist.size();i++) {
			Travel now = travelRepository.getById(utlist.get(i).getTravelId());
			if(now!=null)
				userPref.put(now.getTravelId(), userPref.getOrDefault(userPref.get(now.getTravelId()), (long) 0)+1);
		}

		List<Travel> travel = new ArrayList<>();
		Iterator<Long> mapIter = userPref.keySet().iterator();
		
		Long[][] save = new Long[userPref.size()][2];
		
		int t = 0;
		while(mapIter.hasNext()) {
			Long key = mapIter.next();
			Long value = userPref.get(key);
			save[t][0] = value;
			save[t][1] = key;
			t++;
		}
		
		Arrays.sort(save, new Comparator<Long[]>() {
			@Override
			public int compare(Long[] o1, Long[] o2) {
				if(o1[0]==o2[0])
					return (int) (o1[1]-o2[1]);
				else
					return (int) (o2[0]-o1[0]);
			}	
		});
		
		for(int i=0;i<Math.min(save.length, 20);i++) {
			Optional<Travel> now = travelRepository.findById(save[i][1]);
			if(now.isPresent()) {
				travel.add(now.get());
			}
		}
		
		if(travel.size()>20) {
			travel = travel.subList(0, 20);
		}
		
		return travel;
	}

	/**
	 * ?????? ????????? ??????/????????? ???????????? ???????????? ?????? ??????
	 */
	@Transactional(readOnly = true)
	public List<Travel> findSimiliarAgeGenderTaste(int age, String gender) {
		age = age/10;
		if(age==0)
			age=1;
		String ageKey = age*10 + "???";
		if (age>=7)
			ageKey = "70?????????";
		log.info(ageKey);
		TravelCountAge tca = travelCountAgeRepository.getById(ageKey);
		TravelCountAge allAge = travelCountAgeRepository.getById("??????");

		if(gender.equals("Man")) {
			gender = "??????";
		} else if(gender.equals("Woman")) {
			gender = "??????";
		} else {
			gender = "??????";
		}
		TravelCountGender tcg = travelCountGenderRepository.getById(gender);

		TreeMap<Double, String> rank = new TreeMap<>();

		rank.put(tca.getSeoul()*tcg.getSeoul()/Math.pow(allAge.getSeoul(), 2), "??????");
		rank.put(tca.getBusan()*tcg.getBusan()/Math.pow(allAge.getBusan(), 2), "??????");
		rank.put(tca.getDaegu()*tcg.getDaegu()/Math.pow(allAge.getDaegu(), 2), "??????");
		rank.put(tca.getDaejeon()*tcg.getDaejeon()/Math.pow(allAge.getDaejeon(), 2), "??????");
		rank.put(tca.getGwangju()*tcg.getGwangju()/Math.pow(allAge.getGwangju(), 2), "??????");
		rank.put(tca.getIncheon()*tcg.getIncheon()/Math.pow(allAge.getIncheon(), 2), "??????");
		rank.put(tca.getUlsan()*tcg.getUlsan()/Math.pow(allAge.getUlsan(), 2), "??????");
		rank.put(tca.getSejong()*tcg.getSejong()/Math.pow(allAge.getSejong(), 2), "??????");
		rank.put(tca.getGyeonggi()*tcg.getGyeonggi()/Math.pow(allAge.getGyeonggi(), 2), "??????");
		rank.put(tca.getGangwon()*tcg.getGangwon()/Math.pow(allAge.getGangwon(), 2), "??????");
		rank.put(tca.getChungbuk()*tcg.getChungbuk()/Math.pow(allAge.getChungbuk(), 2), "??????");
		rank.put(tca.getChungnam()*tcg.getChungnam()/Math.pow(allAge.getChungnam(), 2),"??????");
		rank.put(tca.getJeonbuk()*tcg.getJeonbuk()/Math.pow(allAge.getJeonbuk(), 2),"??????");
		rank.put(tca.getJeonnam()*tcg.getJeonnam()/Math.pow(allAge.getJeonnam(), 2), "??????");
		rank.put(tca.getGyeongbuk()*tcg.getGyeongbuk()/Math.pow(allAge.getGyeongbuk(), 2), "??????");
		rank.put(tca.getGyeongnam()*tcg.getGyeongnam()/Math.pow(allAge.getGyeongnam(), 2), "??????");
		rank.put(tca.getJeju()*tcg.getJeju()/Math.pow(allAge.getJeju(), 2), "??????");

		List<Travel> travel = new ArrayList<>();		
		Double prev = rank.lastKey();
		for(int i=0;i<3;i++) {
			travel.addAll(travelRepository.findByLargeGov(rank.get(prev)));		
			prev = rank.lowerKey(prev);
		}
		
		if(travel.size()>20) {
			travel = travel.subList(0, 20);
		}
		
		return travel;
	}


	/**
	 * ????????? ?????? ?????? ?????? ??????
	 */
	public Page<Travel> findByName(String keyword, Pageable pageable) {
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 9, sort);
		return travelRepository.findByTravelNameContaining(keyword, pageable);
	}

	/**
	 * ????????? ?????? ?????? ?????? (????????? ID)
	 */
	@Transactional
	public Travel findById(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		entity.increaseView();
		addImage(entity);
		return entity;
	}
	

	/**
	 * ????????? ????????? ?????? ?????? ?????? (????????? ID)
	 */
	@Transactional
	public Travel findByTravelId(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		addImage(entity);
		return entity;
	}

	/**
	 * ????????? ?????? ?????? ????????? ?????? ?????? ?????? (????????? ID)
	 */
	@Transactional
	public Travel findByIdNoViewCount(Long id) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		return entity;
	}

	/**
	 * ????????? ?????? ?????? (????????? ??????)
	 */
	@Transactional
	public List<TravelSimpleDto> findByName(String name) {
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		List<Travel> list = travelRepository.findByTravelNameContaining(name, sort);

		return list.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}

	/**
	 * ????????? ?????? ?????? (??????????????????)
	 */
	@Transactional
	public Page<Travel> findByLargeGov(String largeGov, Pageable pageable) {

		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		pageable = PageRequest.of(page, 9, sort);

		return travelRepository.findByLargeGov(largeGov, pageable);
	}

	/**
	 * ????????? ?????? ?????? (??????????????????) non-pageable
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
	 * ????????? ?????? ?????? (??????????????????)
	 */
	@Transactional
	public Page<Travel> findBySmallGov(String largeGov, String smallGov, Pageable pageable) {

		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		pageable = PageRequest.of(page, 9, sort);

		return travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, pageable);
	}

	/**
	 * ????????? ?????? ?????? (??????????????????)
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
	 * ????????? ?????? ????????? ?????? (Simple DTO)
	 */
	public List<TravelSimpleDto> findTravelsByRegion(String largeGov, String smallGov) {
		Sort sort = Sort.by(Direction.DESC, "travelView").and(Sort.by(Direction.ASC, "travelName"));
		return travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov, sort)
				.stream().map(TravelSimpleDto::new).collect(Collectors.toList());
	}

	/**
	 * ????????? ??????
	 */
	@Transactional
	public Boolean update(final Long id, final Travel params) {
		Travel entity = travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		// ????????? ?????? admin??? ????????? ??? ?????? ??? (?????? ??????????????? ?????? ????????? ???????????? ??????)
		if(SecurityUtil.getCurrentEmail().get().equals("admin")||SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
			log.info("????????? ???????????? ?????? ???????????????. ????????? ?????? : "+id);
		} else {
			log.info("???????????? ?????? ?????? ???????????????.");
			return false;
		}
		entity.update(params);
		return true;
	}

	/**
	 * ????????? ??????
	 */
	@Transactional
	public Boolean deleteById(Long id) {
		travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND)); 
		// ????????? ?????? admin??? ????????? ??? ?????? ???
		if(SecurityUtil.getCurrentEmail().get().equals("admin")||SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
			log.info("????????? ???????????? ?????? ???????????????. ??? ?????? : "+id);
		} else {
			log.info("???????????? ?????? ?????? ???????????????.");
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

	// ?????? ?????? ???????????? ?????? ?????? ????????????????????? ????????? ??????
	@Transactional
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

	// ?????? ?????? ???????????? ?????? ?????? ????????????????????? ????????? ??????
	@Transactional
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
