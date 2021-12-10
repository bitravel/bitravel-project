package com.bitravel.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.RegionDto;
import com.bitravel.data.entity.Region;
import com.bitravel.data.entity.UserRegion;
import com.bitravel.data.repository.RegionRepository;
import com.bitravel.data.repository.UserRegionRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionService {

	private final UserRegionRepository userRegionRepository;
	private final RegionRepository regionRepository;

	/**
	 * 회원별 선호 지역 추가
	 */
	@Transactional
	public Long save(UserRegion params) {
		UserRegion entity = userRegionRepository.save(params);
		return entity.getUserRegionId();
	}

	/**
	 * 회원별 선호 지역 리스트 조회
	 */
	public List<UserRegion> findAllByUser(String keyword) {
		return userRegionRepository.findByUserEmail(keyword);
	}

	/**
	 * 나의 선호 지역 리스트 조회
	 */
	public List<UserRegion> findAllByMe() {
		String keyword = SecurityUtil.getCurrentEmail().get();
		return userRegionRepository.findByUserEmail(keyword);
	}

	/**
	 *  전체 광역자치단체 검색
	 */

	@Transactional(readOnly = true)
	public List<String> ListOfLargeGov() {
		// 전체 광역자치단체 검색
		List<Region> all = regionRepository.findAll();
		List<String> list = new ArrayList<>();
		for(int i=0;i<all.size();i++) {
			if(!list.contains(all.get(i).getLargeGov())) {
				list.add(all.get(i).getLargeGov());
			}
		}
		return list;
	}

	/**
	 * 광역자치단체별 기초자치단체 검색
	 */
	@Transactional(readOnly = true)
	public List<String> ListByLargeGov(String largeGov) {
		List<Region> all = regionRepository.findByLargeGov(largeGov);
		List<String> list = new ArrayList<>();
		for(int i=0;i<all.size();i++) {
			list.add(all.get(i).getSmallGov());
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 전체 기초자치단체 검색
	 */
	@Transactional(readOnly = true)
	public List<Region> ListAll() {
		List<Region> list = regionRepository.findAll();
		return list;
	}

	/**
	 * 나의 선호 지역 삭제
	 */
	@Transactional
	public Boolean deleteById(Long id) {
		UserRegion userRegion = userRegionRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND)); 
		// 글쓴이 또는 admin만 수정할 수 있게 함
		if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
			log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
		} else if (SecurityUtil.getCurrentEmail().get().equals(userRegion.getUserEmail())) {
			log.info(userRegion.getUserEmail()+ "회원이 본인의 선호 지역을 삭제합니다. 글 번호 : "+id);
		} else {
			log.info("유효하지 않은 삭제 요청입니다.");
			return false;
		}
		userRegionRepository.deleteById(id);
		return true;
	}

	/**
	 * 전체 광역자치단체별 좌표 검색
	 */
	public List<RegionDto> ListOfLargeGovAndCoordinate() {
		// 전체 광역자치단체 검색
		List<Region> all = regionRepository.findAll();
		List<RegionDto> list = new ArrayList<>();		

		HashSet<String> largeSet = new HashSet<>();
		
		// 데이터 완성 후 코드 깔끔하게 바꿀 예정
		
		for(int i=0;i<all.size();i++) {
			RegionDto data = new RegionDto(all.get(i));
			String now = all.get(i).getLargeGov();
			if(!largeSet.contains(now)) {
				
				double latSum = 0;
				double longSum = 0;
				int t = 0;
				boolean flag = true;
				try {
					while(all.get(i+t).getLargeGov().equals(now)) {						
						latSum += Double.parseDouble(all.get(i+t).getRegionLat());
						longSum += Double.parseDouble(all.get(i+t).getRegionLong());
						t++;
						if(i+t>=all.size())
							break;
					}
				} catch(Exception e) {
					flag = false;
				}
				if(flag) {
					latSum /= t;
					longSum /= t;
					data.setRegionLat(Double.toString(latSum));
					data.setRegionLong(Double.toString(longSum));
				}
				
				list.add(data);
				largeSet.add(now);
			} else {
				continue;
			}
		}
		return list;
	}

	public List<RegionDto> ListOfSmallGovAndCoordinate(String large) {
		// 광역자치단체별 기초자치단체 검색
		List<Region> list = regionRepository.findByLargeGov(large);
		double latSum = 0;
		double longSum = 0;
		int t = list.size();
		for(int i=0;i<t;i++) {
			latSum += Double.parseDouble(list.get(i).getRegionLat());
			longSum += Double.parseDouble(list.get(i).getRegionLong());
		}
		list.add(0, Region.builder().largeGov(large).smallGov(large)
				.regionLat(Double.toString(latSum/t))
				.regionLong(Double.toString(longSum/t)).build());
		return list.stream().map(RegionDto::new).collect(Collectors.toList());
	}

}
