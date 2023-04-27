package com.a2m.sso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2m.sso.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUserId(String userId);
	
	Boolean existsByUserId(String userId);
	
	Boolean existsByEmail(String email);
}
