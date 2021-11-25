package com.bitravel.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.entity.Region;
import com.bitravel.data.entity.UserRegion;
import com.bitravel.service.RegionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "RegionController")
public class RegionController {

	private final RegionService regionService;

	/**
	 * 회원별 선호 지역 추가
	 */
	@PostMapping("/regions")
	@ApiOperation(value = "회원별 선호 지역 작성", notes = "회원별 선호 지역 내용을 저장하는 API. UserRegion entity 클래스로 데이터를 저장한다.")
	public Boolean save(@RequestBody final List<UserRegion> params) {
		try {
			for (int i = 0; i < params.size(); i++) {
				regionService.save(params.get(i));
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 회원별 선호 지역 리스트 조회
	 */
	@GetMapping("/regions/favorite")
	@ApiOperation(value = "회원별 선호 지역 조회", notes = "회원별 선호 지역을 조회하는 API.")
	public List<UserRegion> findAllByUser(@Valid String userEmail) {
		return regionService.findAllByUser(userEmail);
	}

	/**
	 * 나의 선호 지역 리스트 조회
	 */
	@GetMapping("/regions/favorite/mylist")
	@ApiOperation(value = "나의 선호 지역 조회", notes = "현재 로그인한 회원의 선호 지역을 조회하는 API.")
	public List<UserRegion> findAllByMe() {
		// 추후 비회원인 경우 해당 서비스 접근 제한 예정
		return regionService.findAllByMe();
	}

	/**
	 * 전체 광역자치단체 검색
	 */
	@GetMapping("/regions/list")
	@ApiOperation(value = "광역자치단체 목록 검색", notes = "광역자치단체별 기초자치단체 목록을 출력하는 API. Region entity 클래스의 LargeGov값을 기준으로 데이터를 가져온다.")
	public List<String> ListOfLargeGov() {
		return regionService.ListOfLargeGov();
	}

	/**
	 * 광역자치단체별 기초자치단체 검색
	 */
	@GetMapping("/regions/list/{name}")
	@ApiOperation(value = "광역자치단체 목록 검색", notes = "광역자치단체별 기초자치단체 목록을 출력하는 API. Region entity 클래스의 LargeGov값을 기준으로 데이터를 가져온다.")
	public List<String> ListByLargeGov(@PathVariable final String name) {
		return regionService.ListByLargeGov(name);
	}

	/**
	 * 전체 기초자치단체 검색
	 */
	@GetMapping("/regions/list/all")
	@ApiOperation(value = "기초자치단체 목록 검색", notes = "전체 기초자치단체 목록을 출력하는 API. Region entity 클래스의 모든 데이터를 가져온다.")
	public List<Region> ListAll() {
		return regionService.ListAll();
	}

	/**
	 * 나의 선호 지역 삭제
	 */
	@DeleteMapping("/regions/{id}")
	@ApiOperation(value = "회원별 선호 지역 삭제", notes = "회원별 선호 지역 내용을 삭제하는 API. UserRegion entity 클래스의 id 값으로 데이터를 삭제한다.")
	public Boolean deleteById(@PathVariable Long id) {
		return regionService.deleteById(id);
	}

}