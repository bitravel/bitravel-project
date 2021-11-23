package com.bitravel.service;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.entity.Travel;
import com.bitravel.data.repository.TravelRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {
	
	private final TravelRepository travelRepository;
	
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
    public List<Travel> findAll() {
    	// 기본 리스트 Sort 기준을 인기순으로 할 수 있을듯. 일단은 이름순 정렬로 세팅
        Sort sort = Sort.by(Direction.ASC, "travelName");
        return travelRepository.findAll(sort);
    }
    
    /**
     * 여행지 상세 정보 조회 (여행지 ID)
     */
    @Transactional(readOnly = true)
    public Travel detailById(Long id) {
    	return travelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    }
    
    /**
     * 여행지 목록 조회 (여행지 이름)
     */
    @Transactional(readOnly = true)
    public List<Travel> detailsByName(String name) {
    	List<Travel> list = travelRepository.findByTravelNameContaining(name);
    	if(list.size() == 0) {
    		throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
    	}
    	return list;
    }
    
    /**
     * 여행지 목록 조회 (광역자치단체)
     */
    @Transactional(readOnly = true)
    public List<Travel> detailsByLargeGov(String largeGov) {
    	List<Travel> list = travelRepository.findByLargeGov(largeGov);
    	if(list.size() == 0) {
    		throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
    	}
    	return list;
    }
    
    /**
     * 여행지 목록 조회 (기초자치단체)
     */
    @Transactional(readOnly = true)
    public List<Travel> detailsBySmallGov(String largeGov, String smallGov) {
    	List<Travel> list = travelRepository.findByLargeGovAndSmallGov(largeGov, smallGov);
    	if(list.size() == 0) {
    		throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
    	}
    	return list;
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
    
}
