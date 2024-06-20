package com.example.springsecurityLearning.LearningSpring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsecurityLearning.LearningSpring.Entity.AuthenticationResponse;
import com.example.springsecurityLearning.LearningSpring.Entity.User;
import com.example.springsecurityLearning.LearningSpring.service.AuthenticationService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
public class Controller {
	private final AuthenticationService authservice;

	public Controller(AuthenticationService authservice) {
		super();
		this.authservice = authservice;
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody User user){
		System.out.println("Inside the register");
		AuthenticationResponse authresponse = authservice.register(user);
		System.out.println("Inside the register After call");
		return ResponseEntity.ok(authresponse);
		
	}
	 @PostMapping("/login")
	    public ResponseEntity<AuthenticationResponse> login(
	            @RequestBody User request
	    ) {
		   System.out.println("Inside the login before call");
	        return ResponseEntity.ok(authservice.authenticate(request));
	        
	    }

}
