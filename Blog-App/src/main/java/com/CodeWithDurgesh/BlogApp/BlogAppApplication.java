package com.CodeWithDurgesh.BlogApp;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.entity.Role;
import com.CodeWithDurgesh.BlogApp.repo.RoleRepo;

@SpringBootApplication
@ComponentScan("com.CodeWithDurgesh.BlogApp")
public class BlogAppApplication implements CommandLineRunner {
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
		
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("BCrypted password is :"+passwordEncoder.encode("122132"));
		
		
		try {
			Role role1 = new Role();
			role1.setRoleId(AppConstants.ADMIN_USER); 
			role1.setName("ROLE_ADMIN");
			
			Role role2 = new Role();
			role2.setRoleId(AppConstants.NORMAL_USER);
			role2.setName("ROLE_USER");
			
			List<Role> roles = List.of(role1,role2);
			List<Role> result = roleRepo.saveAll(roles);
			result.forEach( r -> {System.out.println("ROLES have been added to the database:"+r.getName());});
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
