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
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails {
	// 초안 기준 1차 설계 진행 중

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923379214282610871L;

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
    
    @Column(name = "realName")
    private String realName;
    
    @Column(name = "nickname", unique = true)
    private String nickname;
    
    @JsonIgnore
    @Column(name = "password")
    private String password;
    
    // 회원정보 수정하는 경우 있음
    @Column(name = "point")
    private Integer point;
    
    @Column(name = "userImage")
    private String userImage;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "userLargeGov")
    private String userLargeGov; // 사용자 거주 광역자치단체 (외국인 경우도 포함)

    @Column(name = "userSmallGov")
    private String userSmallGov; // 사용자 거주 기초자치단체 (외국인 경우도 포함)
    
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
    
	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return activated;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
    // 포인트 관리
    public void changePoint(int diff)
    {
    	this.point += diff;
    }

}

