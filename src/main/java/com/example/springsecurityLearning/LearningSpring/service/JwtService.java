package com.example.springsecurityLearning.LearningSpring.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.springsecurityLearning.LearningSpring.Entity.User;
import com.example.springsecurityLearning.LearningSpring.repository.TokenRepository;
import com.example.springsecurityLearning.LearningSpring.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final String secrete_key ="4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

	private final TokenRepository tokenRepository;

	public JwtService(TokenRepository tokenRepository) {
	    this.tokenRepository = tokenRepository;
	}
	
	@Value("${application.security.jwt.access-token-expiration}")
	private long accessTokenExpire;
	
	@Value("${application.security.jwt.refresh-token-expiration}")
	private long refreshTokenExpire;
	
	public String extractUsername(String token) {
	return extractClaim(token, Claims::getSubject);
	}
	
	
	public boolean isValid(String token, UserDetails user) {
	String username = extractUsername(token);
	
	return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}
	
	
	
	private boolean isTokenExpired(String token) {
	// TODO Auto-generated method stub
	return ectractExpiration(token).before(new Date());
	}
	
	
	private Date ectractExpiration(String token) {
	// TODO Auto-generated method stub
	return extractClaim(token, Claims::getExpiration);
	}
	
	
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
	Claims claims = extractAllClaims(token);
	return resolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
	 return Jwts
	            .parserBuilder()
	            .setSigningKey(getSigninKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	
	}
	
	
	public String generateToken(User user) {
	    String token = Jwts
	            .builder()
	            .setSubject(user.getUsername())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + (24*60*60) ))
	            .signWith(getSigninKey())
	            .compact();
	
	    return token;
	}
	public String generateAccessToken(User user) {
	    return generateToken(user, accessTokenExpire);
	}
	
	private SecretKey getSigninKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64URL.decode(secrete_key);
	return Keys.hmacShaKeyFor(keyBytes);
}


public String generateRefreshToken(User user) {
// TODO Auto-generated method stub
return generateToken(user, refreshTokenExpire );
}


private String generateToken(User user, long expireTime) {
String token = Jwts
        .builder()
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expireTime ))
        .signWith(getSigninKey())
        .compact();

return token;
}


public boolean isValidRefreshToken(String token, User user) {
String username = extractUsername(token);

boolean validRefreshToken = tokenRepository
        .findByRefreshToken(token)
        .map(t -> !t.isLoggedOut())
        .orElse(false);

return (username.equals(user.getUsername())) && !isTokenExpired(token) && validRefreshToken;
}


}