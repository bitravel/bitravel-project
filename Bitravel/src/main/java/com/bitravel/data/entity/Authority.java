package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9176530094464751268L;
	@Id
	@Column(name = "roleName", length=50)
	private String roleName;

	@Override
	public String getAuthority() {
		return this.roleName;
	}
}
