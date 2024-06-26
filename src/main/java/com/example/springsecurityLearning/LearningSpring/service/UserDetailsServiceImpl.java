package com.example.springsecurityLearning.LearningSpring.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
		System.out.println("in load method");
		return userrepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
	}

}
