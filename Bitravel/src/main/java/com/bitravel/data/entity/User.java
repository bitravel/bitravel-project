package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
 
@Entity
@Table(name = "User")
@Setter
@Getter
public class User {
	// 초안 기준 1차 설계 진행중
 
    @Id
    @Size (min=4, max=30)
    private @Column(name = "userId") String userId;
    
    @NotNull
    @Size(min=2, max=15)
    private @Column(name = "userName") String userName;
    
    @NotNull
    @Size(min=2, max=15)
    private @Column(name = "nickname") String nickname;
    
    @NotNull
    private @Column(name = "email") String email;
    
    @NotNull
    private @Column(name = "pwd") String password;
    
    @NotNull
    @Column(name = "point")
    private Long point;
    
    @NotNull
    private @Column(name = "gender") String gender;
    
    @NotNull
    private @Column(name = "age") Integer age;
    
    @Column(name = "userDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp userDate;
     
  
}

