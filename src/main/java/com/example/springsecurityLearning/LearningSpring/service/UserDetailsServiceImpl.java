package com.example.springsecurityLearning.LearningSpring.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.springsecurityLearning.LearningSpring.Entity.User;
import com.example.springsecurityLearning.LearningSpring.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService{

	private final UserRepository userrepo;
	
	
	
	public UserDetailsServiceImpl(UserRepository userrepo) {
		super();
		this.userrepo = userrepo;
	}
	 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userrepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
	}

}
