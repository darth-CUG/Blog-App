package com.CodeWithDurgesh.BlogApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.CodeWithDurgesh.BlogApp.security.CustomUserDetailService;
import com.CodeWithDurgesh.BlogApp.security.JwtAuthenticationEntryPoint;
import com.CodeWithDurgesh.BlogApp.security.JwtAuthenticationFilter;

//import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests( (authz) -> authz	
//        		.requestMatchers("api/auth/**").permitAll()
        		.requestMatchers(HttpMethod.GET).permitAll()
        		.requestMatchers(HttpMethod.POST).permitAll()
//        		.requestMatchers("login").permitAll()
        		//OR: .requestMatchers("api/auth/**").permitAll()
        		.anyRequest().authenticated())
        .exceptionHandling( (ex) -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint) )
        .sessionManagement( (sess) -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );
        
        
        // (authz) -> authz.requestMatchers("/test").authenticated().requestMatchers("/auth/login").permitAll())
        
        http
        .addFilterBefore
        (jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
         
        //.and()
//        .exceptionHandling((ex) -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//        .and()
//	        .sessionManagement()
//	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationToken.class);
        
        return http.build();
    }
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//		return authenticationManagerBean();
//    }
    
    
/*    
    @Autowired
	private CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated()
            ).httpBasic(withDefaults());
        return http.build();
    }
*/
    
    
}

