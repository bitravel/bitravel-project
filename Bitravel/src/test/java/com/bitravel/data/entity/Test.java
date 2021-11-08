package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
 
@Entity
@Table(name = "Test")
@Setter
@Getter
public class Test {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "test_id") Long testId;
    private @Column(name = "test_name") String testName;
     
    @CreationTimestamp
    private @Column(name = "test_date") Timestamp testDate;
     
  /*  @UpdateTimestamp
    private @Column(name = "udate") Timestamp udate; */
}

