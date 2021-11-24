package com.bitravel.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		params.setUserEmail(SecurityUtil.getCurrentEmail().get());
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
	public List<Region> ListByLargeGov(String largeGov) {
		List<Region> list = regionRepository.findByLargeGov(largeGov);
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

}
