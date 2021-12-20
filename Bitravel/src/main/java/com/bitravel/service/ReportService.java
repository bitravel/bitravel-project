package com.bitravel.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.ReportCheckDto;
import com.bitravel.data.dto.ReportDto;
import com.bitravel.data.entity.BoardComment;
import com.bitravel.data.entity.Report;
import com.bitravel.data.entity.ReviewComment;
import com.bitravel.data.repository.BoardCommentRepository;
import com.bitravel.data.repository.BoardRepository;
import com.bitravel.data.repository.ReportRepository;
import com.bitravel.data.repository.ReviewCommentRepository;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportRepository reportRepository;
	private final BoardCommentRepository bCommentRepository;
	private final ReviewCommentRepository rCommentRepository;
	private final BoardRepository boardRepository;
	private final ReviewRepository reviewRepository;

	/**
	 * 전체 신고 내역 검색
	 */
	@Transactional(readOnly = true)
	public List<Report> ListAll() {
		List<Report> list = reportRepository.findAll();
		return list;
	}

	/**
	 * 게시물/후기 신고하기
	 */
	@Transactional
	public Boolean savePostReport(ReportDto param) {
		
		Report entity = null;
		String reportContent = null;
		String nowEmail = SecurityUtil.getCurrentEmail().get();
		try {		
			if(param.getReportType().equals("b")) {
				reportContent = "/board/"+param.getPostId();
				
				entity = Report.builder()
						.reportTitle(param.getReportTitle())
						.reportContent(reportContent)
						.reportedEmail(boardRepository.findById(Long.parseLong(param.getPostId())).get().getUserEmail())
						.reporterEmail(nowEmail)
						.build();
			} else if (param.getReportType().equals("r")) {
				reportContent = "/review/"+param.getPostId();
				entity = Report.builder()
						.reportTitle(param.getReportTitle())
						.reportContent(reportContent)
						.reportedEmail(reviewRepository.findById(Long.parseLong(param.getPostId())).get().getUserEmail())
						.reporterEmail(nowEmail)
						.build();
			} else {
				return false;
			}
			
			if(entity.getReportedEmail().equals(nowEmail)) {
				throw new Exception("same email");
			}
			
			reportRepository.save(entity);
			
		} catch (Exception e) {
			log.info(e.getMessage());
			return false;
		}
		
		return true;
	}


	/**
	 * 댓글 신고하기
	 */
	@Transactional
	public Boolean saveCommentReport(String cid) {
		Report entity = null;
		String reportContent = null;
		String reportedEmail = null;
		String reportTitle = null;
		try {
			if(cid.charAt(0)=='b') {
				Optional<BoardComment> bc = bCommentRepository.findById(Long.parseLong(cid.substring(1, cid.length())));
				if(bc.isEmpty())
					return false;
				Long boardId = bc.get().getBoard().getBoardId();
				reportContent = "/board/"+boardId;
				reportedEmail = bc.get().getUserEmail();
				reportTitle = "여행 게시판 댓글 신고";

			} else if(cid.charAt(0)=='r') {
				Optional<ReviewComment> rc = rCommentRepository.findById(Long.parseLong(cid.substring(1, cid.length())));
				if(rc.isEmpty())
					return false;
				Long reviewId = rc.get().getReview().getReviewId();
				reportContent = "/review/"+reviewId;
				reportedEmail = rc.get().getUserEmail();
				reportTitle = "후기 게시판 댓글 신고";
			} else {
				return false;
			}
			
			String nowEmail = SecurityUtil.getCurrentEmail().get();
			
			if(reportedEmail.equals(nowEmail)) {
				throw new Exception("same email");
			}
			
			entity = Report.builder()
					.reportComment(cid)
					.reportTitle(reportTitle)
					.reportContent(reportContent)
					.reportedEmail(reportedEmail)
					.reporterEmail(nowEmail)
					.build();
			
			reportRepository.save(entity);
			
		} catch (Exception e) {
			log.info(e.getMessage());
			return false;
		}

		return true;
	}
	
	/**
	 * 중복 게시물/후기 신고 여부 확인
	 */
	public Boolean isReportedPost(ReportDto param) {
		String reportContent = null;
		if(param.getReportType().equals("b")) {
			reportContent = "/board/detail/"+param.getPostId();
		} else if (param.getReportType().equals("r")) {
			reportContent = "/review/detail/"+param.getPostId();
		} else {
			return true;
		}
		
		Optional<Report> find = reportRepository.findByReportContentAndReporterEmail(reportContent, SecurityUtil.getCurrentEmail().get());
		if(find.isPresent()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 중복 댓글 신고 여부 확인
	 */
	public Boolean isReportedComment(String cid) {
		Optional<Report> find = reportRepository.findByReportCommentAndReporterEmail(cid, SecurityUtil.getCurrentEmail().get());
		if(find.isPresent()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 신고 제목으로 검색한 결과 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListByTitle(String keyword) {
		return reportRepository.findByReportTitleContaining(keyword);
	}
	
	/**
	 * 신고 내용(글 번호 등 링크)으로 검색한 결과 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListByContent(String keyword) {
		return reportRepository.findByReportContentContaining(keyword);
	}
	
	/**
	 * 신고한 유저로 검색한 결과 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListByReporter(String keyword) {
		return reportRepository.findByReporterEmailContaining(keyword);
	}
	
	/**
	 * 신고된 유저로 검색한 결과 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListByReported(String keyword) {
		return reportRepository.findByReportedEmailContaining(keyword);
	}
	
	/**
	 * 미처리 결과만 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListNotChecked() {
		return reportRepository.findByCheckResultIsNull();
	}
	
	/**
	 * 처리 완료 결과만 불러오기
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Report> getReportListChecked() {
		return reportRepository.findByCheckResultIsNotNull();
	}
	
	/**
	 * 신고내역 처리 완료하기
	 */
	@Transactional
	public Boolean checkReport(ReportCheckDto param) {		
		List<Long> list = param.getList();
		
		for(int i=0;i<list.size();i++) {
			Optional<Report> rp = reportRepository.findById(list.get(i));
			if (rp.isEmpty())
				return false;
			else {
				rp.get().setCheckResult(param.getResult());
			}
		}	
		return true;		
	}
	
}
