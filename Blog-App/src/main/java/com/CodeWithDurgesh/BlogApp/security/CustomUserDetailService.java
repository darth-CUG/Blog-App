package com.CodeWithDurgesh.BlogApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username).orElseThrow(
				() -> new ResourceNotFoundException("User", "email"+username, 0));
		return user;
	}
}
