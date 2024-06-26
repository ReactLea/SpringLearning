package com.example.springsecurityLearning.LearningSpring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springsecurityLearning.LearningSpring.Entity.AuthenticationResponse;
import com.example.springsecurityLearning.LearningSpring.Entity.Token;
import com.example.springsecurityLearning.LearningSpring.Entity.User;
import com.example.springsecurityLearning.LearningSpring.repository.TokenRepository;
import com.example.springsecurityLearning.LearningSpring.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;

	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
	}

	public AuthenticationResponse register(User request) {
		System.out.println("in register");
		if (repository.findByUsername(request.getUsername()).isPresent()) {
			return new AuthenticationResponse(null, null, "User already exist");
		}
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(request.getRole());

		user = repository.save(user);
		System.out.println("after saviong");

		String accessToken = jwtService.generateToken(user);

		
		String refreshToken = jwtService.generateRefreshToken(user); // //
		saveUserToken(accessToken, refreshToken, user); //
		return new AuthenticationResponse(accessToken);
	}

	public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		// extract the token from authorization
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return new ResponseEntity<AuthenticationResponse>(HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);

		// extract username from token
		String username = jwtService.extractUsername(token);

		// check if the user exist in database
		User user = repository.findByUsername(username).orElseThrow(() -> new RuntimeException("No user found"));

		// check if the token is valid
		if (jwtService.isValidRefreshToken(token, user)) { // generate access token
			String accessToken = jwtService.generateAccessToken(user);
			String refreshToken = jwtService.generateRefreshToken(user);

			revokeAllTokenByUser(user);
			saveUserToken(accessToken, refreshToken, user);

			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(accessToken, refreshToken, "New token generated"),
					HttpStatus.OK);
		}

		return new ResponseEntity<AuthenticationResponse>(HttpStatus.UNAUTHORIZED);

	}

	private void revokeAllTokenByUser(User user) {
		List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
		if (validTokens.isEmpty()) {
			return;
		}

		validTokens.forEach(t -> {
			t.setLoggedOut(true);
		});

		tokenRepository.saveAll(validTokens);
	}

	private void saveUserToken(String accessToken, String refreshToken, User user) {
		Token token = new Token();
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

	public AuthenticationResponse authenticate(User request) {
		System.out.println(request.getUsername() + request.getPassword());

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		System.out.println("Inside the authenticate ....");
		User user = repository.findByUsername(request.getUsername()).orElseThrow();
		System.out.println("Inside the login auth" + user);
		String token = jwtService.generateToken(user);

		String refreshToken = null;
		return new AuthenticationResponse(token, refreshToken, "User login was successful");

	}

}