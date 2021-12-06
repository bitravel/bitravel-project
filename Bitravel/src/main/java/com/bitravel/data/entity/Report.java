package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "reportId")
    private Long reportId;
    
    @Column(name = "reportTitle")
    private String reportTitle;
    
    @Column(name = "reportContent")
    private String reportContent;
    
    @Column(name = "reportComment")
    private String reportComment;
    
    @Column(name = "reporterEmail")
    private String reporterEmail;
    
    @Column(name = "reportedEmail")
    private String reportedEmail;
	
    @Column(name = "reportDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp reportDate;
    
    @UpdateTimestamp
    @Column(name = "checkDate")
    private Timestamp checkDate;
    
    @Column(name = "checkResult")
    private String checkResult;
    
    void checkReport(String result) {
    	this.checkResult = result;
    }
    
    public Report(Long id, String title, String content, String comment, String reporterUser, String reportedUser, Timestamp reportDate, Timestamp checkDate, String checkResult) {
    	this.reportId = id;
    	this.reportTitle = title;
    	this.reportContent = content;
    	this.reportComment = comment;
    	this.reporterEmail = reporterUser;
    	this.reportedEmail = reportedUser;
    	this.reportDate = reportDate;
    	this.checkDate = checkDate;
    	this.checkResult = checkResult;
    }
    
}
