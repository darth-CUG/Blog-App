package com.CodeWithDurgesh.BlogApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.config.CurrentUser;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.ApiResponse;
import com.CodeWithDurgesh.BlogApp.payloads.FollowUserResponse;
import com.CodeWithDurgesh.BlogApp.payloads.FollowUserWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;
import com.CodeWithDurgesh.BlogApp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@PostMapping("create")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		return new ResponseEntity<>(userService.createUser(userDto),HttpStatus.CREATED);
	}
	
	@GetMapping("get/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id") Integer userId){
		return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
	}
	
	@PutMapping("update/{id}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("id") Integer userId){
		return new ResponseEntity<>(userService.updateUser(userDto, userId),HttpStatus.OK);
	}
	
	@PostMapping("user/{userId}/follow")
	public ResponseEntity<FollowUserResponse> followUser(@PathVariable("userId") Integer userId, @CurrentUser User loggedInUser){
		FollowUserWrapper wrapper = userService.followUser(userId,loggedInUser.getUserId()); //loggedInUser.getUserId()
		return new ResponseEntity<FollowUserResponse>(new FollowUserResponse("You successfully follow",wrapper),HttpStatus.OK);	
	}
	
	@PostMapping("user/{userId}/unfollow")
	public ResponseEntity<FollowUserResponse> unfollowUser(@PathVariable("userId") Integer userId, @CurrentUser User loggedInUser){
		FollowUserWrapper wrapper = userService.unfollowUser(userId,loggedInUser.getUserId()); 
		return new ResponseEntity<FollowUserResponse>(new FollowUserResponse("You succesfully unfollowed", wrapper),HttpStatus.OK);
	}
	// way 1 : return type itself is http status
//	@DeleteMapping("delete/{id}")
//	private ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Integer userId){
//		userService.deleteUser(userId);
//		return new ResponseEntity<>(HttpStatus.OK);//ß
//	}
	// way 2: use api response from payload
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Integer userId){
		userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully.",true),HttpStatus.OK);
	}
	
}
