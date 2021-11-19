package com.bitravel.data.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	// 초안 기준 1차 설계 진행 중

	@SuppressWarnings("unchecked")
	public User(String subject, Collection<? extends GrantedAuthority> authorities) {
		this.email = subject;
		this.authorities = (Set<Authority>) authorities;
	}

	@JsonIgnore
	@Id
	@Column(name="userId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
    
    @Column(name = "email", unique = true)
    private String email;
    
    private @Column(name = "realName") String realName;
    
    private @Column(name = "nickname", unique = true) String nickname;
    
    @JsonIgnore
    private @Column(name = "password") String password;
    
    // 회원정보 수정하는 경우 있음
    @Column(name = "point")
    private Integer point;
    
    private @Column(name = "gender") String gender;
    
    private @Column(name = "age") Integer age;
        @Column(name = "userDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp userDate;
    
    @JsonIgnore
    @Column(name = "activated", columnDefinition = "BIT(1) DEFAULT 1")
    private boolean activated;
    
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleName", referencedColumnName = "roleName")})
    private Set<Authority> authorities;

}

