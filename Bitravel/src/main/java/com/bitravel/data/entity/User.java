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
	// 초안 기준 1차 설계 진행중
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "userId") Long userId;
    private @Column(name = "userName") String userName;
    private @Column(name = "nickname") String nickname;
    private @Column(name = "email") String email;
    private @Column(name = "pwd") String password;
    private @Column(name = "point") Long point;
    private @Column(name = "gender") String gender;
    private @Column(name = "age") Integer age;
    
    @CreationTimestamp
    private @Column(name = "userDate") Timestamp userDate;
     
  
}

