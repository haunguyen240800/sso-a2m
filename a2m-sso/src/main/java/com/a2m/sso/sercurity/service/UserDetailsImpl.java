package com.a2m.sso.sercurity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.a2m.sso.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	private Long userUid;
	
	private String userId;
	
	private String email;
	
	private Boolean status;
	
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public static UserDetailsImpl build(User user) {
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		return new UserDetailsImpl(
					user.getUserUid(),
					user.getUserId(),
					user.getEmail(),
					user.getStatus(),
					user.getPassword(),
					authorities
				);
		
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return status;
	}

}
