package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Setter
@Getter
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
    
    @Column(name = "reporterEmail")
    private String reporterEmail;
    
    @Column(name = "reportedEmail")
    private String reportedEmail;
	
    @Column(name = "reportDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp reportDate;
    
    @Column(name = "checkDate")
    private Timestamp checkDate;
    
    @Column(name = "checkResult")
    private String checkResult;
    
}
