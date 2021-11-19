package com.bitravel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.entity.Travel;
import com.bitravel.service.TravelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "TravelController")
public class TravelController {

    private final TravelService travelService;

    /**
     * 여행지 작성
     */
    @PostMapping("/travels")
    @ApiOperation(value = "여행지 작성", notes = "여행지 내용을 저장하는 API. Travel entity 클래스로 데이터를 저장한다.")
    public Long save(@RequestBody final Travel params) {
        return travelService.save(params);
    }

    /**
     * 여행지 리스트 조회
     */
    @GetMapping("/travels")
    @ApiOperation(value = "여행지 목록 조회", notes = "여행지 목록을 조회하는 API.")
    public List<Travel> findAll() {
        return travelService.findAll();
    }

    /**
     * 여행지 수정
     */
    @PatchMapping("/travels/{id}")
    @ApiOperation(value = "여행지 수정", notes = "여행지 내용을 수정하는 API. Travel entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 id 값을 함깨 보내줘야한다.")
    public Boolean save(@PathVariable final Long id, @RequestBody final Travel params) {
        return travelService.update(id, params);
    }
    /**
     * 여행지 상세 정보 조회
     */
    @GetMapping("/travels/{id}")
    @ApiOperation(value = "여행지 내용 조회", notes = "개별 여행지의 정보를 조회하는 API. Travel entity 클래스의 id값을 기준으로 데이터를 가져온다.")
    public Travel detailById(@PathVariable final Long id) {
    	return travelService.detailById(id);
    }
    
    /**
     * 여행지 이름 검색
     */
    @GetMapping("/travels/name/{name}")
    @ApiOperation(value = "여행지 이름 검색", notes = "여행지 이름으로 검색한 결과를 목록으로 출력하는 API. Travel entity 클래스의 travelName값을 기준으로 데이터를 가져온다.")
    public List<Travel> detailsByName(@PathVariable final String name) {
    	return travelService.detailsByName(name);
    }
    
    /**
     * 여행지 광역자치단체별 검색
     */
    @GetMapping("/travels/region/{name}")
    @ApiOperation(value = "광역자치단체별 여행지 검색", notes = "광역자치단체별로 검색한 결과를 목록으로 출력하는 API. Travel entity 클래스의 LargeGov값을 기준으로 데이터를 가져온다.")
    public List<Travel> detailsByLargeGov(@PathVariable final String name) {
    	return travelService.detailsByLargeGov(name);
    }
    
    /**
     * 여행지 기초자치단체별 검색
     */
    @GetMapping("/travels/region/{large}/{small}")
    @ApiOperation(value = "기초자치단체별 여행지 검색", notes = "기초자치단체별로 검색한 결과를 목록으로 출력하는 API. Travel entity 클래스의 LargeGov 값과 SmallGov 값 기준으로 데이터를 가져온다.")
    public List<Travel> detailsBySmallGov(@PathVariable("large") final String largeGov, @PathVariable("small") final String smallGov) {
    	return travelService.detailsBySmallGov(largeGov, smallGov);
    }
    
    /**
     * 여행지 삭제
     */
    @DeleteMapping("/travels/{id}")
    @ApiOperation(value = "여행지 삭제", notes = "여행지 내용을 삭제하는 API. Travel entity 클래스의 id 값으로 데이터를 삭제한다.")
    public Boolean deleteById(@PathVariable Long id) {	
    	return travelService.deleteById(id);
    }
    
}