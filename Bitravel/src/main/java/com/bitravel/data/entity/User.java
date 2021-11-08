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
@Table(name = "User")
@Setter
@Getter
public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "userId") Long userId;
    private @Column(name = "userName") String userName;
    
    @CreationTimestamp
    private @Column(name = "userDate") Timestamp userDate;
     
  /*  @UpdateTimestamp
    private @Column(name = "udate") Timestamp udate; */
}

