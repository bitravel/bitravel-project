package com.bitravel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.dto.ReportCheckDto;
import com.bitravel.data.dto.ReportDto;
import com.bitravel.data.entity.Report;
import com.bitravel.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "ReportController")
public class ReportController {

	private final ReportService reportService;


	/**
	 * 전체 신고 내역 검색 (최대 1000개, 그 외에 찾고 싶은 것은 검색으로)
	 */
	@GetMapping("/reports/list")
	@ApiOperation(value = "전체 신고 목록 검색", notes = "전체 신고 목록을 최대 1000개까지 출력하는 API. Report entity 클래스의 모든 데이터를 가져온다.")
	public List<Report> ListAll() {
		List<Report> list = reportService.ListAll();
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 제목으로 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/search/title")
	@ApiOperation(value = "신고 제목 검색 목록", notes = "신고 제목으로 검색한 목록을 최대 1000개까지 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListByTitle(String keyword) {
		List<Report> list = reportService.getReportListByTitle(keyword);
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 내용으로 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/search/content")
	@ApiOperation(value = "신고 내용 검색 목록", notes = "신고 내용으로 검색한 목록을 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListByContent(String keyword) {
		List<Report> list = reportService.getReportListByContent(keyword);
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 신고한 유저로 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/search/reporter")
	@ApiOperation(value = "신고한 유저 검색 목록", notes = "신고한 유저로 검색한 목록을 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListByReporter(String keyword) {
		List<Report> list = reportService.getReportListByReporter(keyword);
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 신고당한 유저로 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/search/reported")
	@ApiOperation(value = "신고당한 유저 검색 목록", notes = "신고당한 유저로 검색한 목록을 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListByReported(String keyword) {
		List<Report> list = reportService.getReportListByReported(keyword);
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 미처리 내역 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/notchecked")
	@ApiOperation(value = "미처리 신고 목록", notes = "아직 처리 결과가 없는 신고 목록을 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListNotChecked() {
		List<Report> list = reportService.getReportListNotChecked();
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 처리 완료 내역 검색 (최대 1000개, 그 외에 찾고 싶은 것은 키워드를 세분화해서)
	 */
	@GetMapping("/reports/checked")
	@ApiOperation(value = "처리 완료 신고 목록", notes = "처리 결과가 존재하는 신고 목록을 출력하는 API. Report entity 클래스 데이터를 가져온다.")
	public List<Report> ListChecked() {
		List<Report> list = reportService.getReportListChecked();
		if(list.size()<1000)
			return list;
		else
			return list.subList(0, 1000);
	}
	
	/**
	 * 게시물/후기 신고하기
	 */
	@PostMapping("/reports/post")
	@ApiOperation(value = "기초자치단체 목록 검색", notes = "전체 기초자치단체 목록을 출력하는 API. Report entity 클래스의 데이터를 가져온다.")
	public ResponseEntity<ReportDto> reportPost(@RequestBody ReportDto param) {
		
		if(reportService.isReportedPost(param))
			return ResponseEntity.badRequest().body(param);
		
		if(reportService.savePostReport(param))
			return ResponseEntity.ok(param);
		else
			return ResponseEntity.badRequest().body(param);
	}
	
	
	/**
	 * 댓글 신고하기
	 */
	@GetMapping("/reports/comment/{cid}")
	@ApiOperation(value = "기초자치단체 목록 검색", notes = "전체 기초자치단체 목록을 출력하는 API. Report entity 클래스의 모든 데이터를 가져온다.")
	public ResponseEntity<Report> reportComment(@PathVariable final String cid) {
		
		if(reportService.isReportedComment(cid)) {
			return ResponseEntity.badRequest().body(null);
		}
		
		if(reportService.saveCommentReport(cid))
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.badRequest().body(null);
	}
	
	/**
	 * 신고내역 처리 완료하기
	 */
	@PostMapping("/reports/check")
	@ApiOperation(value = "기초자치단체 목록 검색", notes = "전체 기초자치단체 목록을 출력하는 API. Report entity 클래스의 모든 데이터를 가져온다.")
	public ResponseEntity<?> checkReport(@RequestBody final ReportCheckDto param) {
		
		if(reportService.checkReport(param))
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.badRequest().body(null);
	}
}