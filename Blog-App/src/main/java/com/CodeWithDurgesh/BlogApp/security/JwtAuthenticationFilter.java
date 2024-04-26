package com.CodeWithDurgesh.BlogApp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 
		
		//1. get token (from header of request)
		String requestToken = request.getHeader("Authorization");
		System.out.println("resquestToken: "+requestToken); 
		
		String username = null;
		String token = null;

		//2. is token format correct
		if (requestToken!=null && requestToken.startsWith("Bearer")) {
			
			//get actual token
			token = requestToken.substring(7);
			
			try {
				username = jwtTokenHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT token.");
				
			} catch (ExpiredJwtException e) {
				System.out.println("JWT token has expired");
				
			} catch (MalformedJwtException e) {
				System.out.println("Invalid JWT.");
			}
		} else { System.out.println("JWT token is null or does not begin with 'Bearer'."); }
		
		//3. validate token
		if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null) { //Ensures that the username was successfully extracted from the token in the previous steps && prevents redundant authentication attempts 
			//4. get user from token
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			//5. load user associated w/ token
			if (jwtTokenHelper.validateToken(token, userDetails)) { // <- validation done
				
				// create a U.P.A.T. to pass in .setAuthorities
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				// to set detail - create detail using builder method, which is inside WebAuthenticationDetailsSource
				usernamePasswordAuthenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				
				//6. set spring security = This allows the user to be treated as authenticated for the request.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				System.out.println("-->"+userDetails.getAuthorities()); //to check authority of a user is extracted from token or not
			} else { System.out.println("Invalid JWT token."); }
		} else { System.out.println("Username is null or Context is not null."); }
		
		filterChain.doFilter(request, response);
	}

}
