package com.CodeWithDurgesh.BlogApp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

//import org.hibernate.boot.jaxb.mapping.DiscriminatedAssociation.Key;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;

import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.Jwts.KEY;
//import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenHelper {
	
	//requirement :
    public static final long JWT_TOKEN_VALIDITY = AppConstants.JWT_TOKEN_VALIDITY;
    //    public static final long JWT_TOKEN_VALIDITY =  60;
    
    private String secret = AppConstants.SECRET;
   
	//    @PostConstruct
	//    public void init() { private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); }    ---    try:  private static SecretKey key = Jwts.SIG.HS512.key().build(); 
	//    SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    
    private SecretKey getSigningKey() {
    	  
    	  byte[] keyBytes = Decoders.BASE64.decode(this.secret);
    	  return Keys.hmacShaKeyFor(keyBytes);
    	}
    
    
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
    	return Jwts.parser()
    	         .verifyWith(getSigningKey())
    	         .build().parseSignedClaims(token)
    	         .getPayload();
    
//        return Jwts.parser()
//        		.setSigningKey(getSigningKey())
//        		.build()
//        		.parseClaimsJws(token)
//        		.getBody();
/*
	public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()))
                .parseClaimsJws(token) //this part is highlighted in red
                .getBody();
        return claims.getSubject();
    }
 */
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
    	return Jwts.builder().claims(claims)
    			.subject(subject)
    			.issuedAt(new Date(System.currentTimeMillis()))
    			.expiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY * 1000))
    			.signWith(getSigningKey())
    			.compact();
    			
//    			.setSubject(subject)
//    		    .setIssuedAt(new Date(System.currentTimeMillis()))
//    		    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//    		    .setClaims(claims)
//    		    .signWith(SignatureAlgorithm.HS512, secret) // Replace "secret" with your actual secret key
//    		    .compact();
    	
//        return Jwts.builder()
//        		.setClaims(claims)
//        		.setSubject(subject)
//        		.setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();/
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
