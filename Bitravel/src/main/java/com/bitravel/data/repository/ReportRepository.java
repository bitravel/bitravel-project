package com.bitravel.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Report;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface ReportRepository extends JpaRepository<Report, Long>{	
	
	Optional<Report> findByReportContentAndReporterEmail(String reportContent, String reporterEmail);
	Optional<Report> findByReportCommentAndReporterEmail(String reportComment, String reporterEmail);
	
	List<Report> findByReportTitleContaining(String reportTitle);
	List<Report> findByReportTitleContaining(String reportTitle, Sort sort);
	
	List<Report> findByReportContentContaining(String reportContent);
	List<Report> findByReportContentContaining(String reportContent, Sort sort);
	
	List<Report> findByReporterEmailContaining(String reporterEmail);
	List<Report> findByReporterEmailContaining(String reporterEmail, Sort sort);
	
	List<Report> findByReportedEmailContaining(String reportedEmail);
	List<Report> findByReportedEmailContaining(String reportedEmail, Sort sort);
	
	List<Report> findByCheckResultContaining(String checkResult);
	List<Report> findByCheckResultContaining(String checkResult, Sort sort);
	
	List<Report> findByCheckResultIsNull();
	List<Report> findByCheckResultIsNotNull();
}
