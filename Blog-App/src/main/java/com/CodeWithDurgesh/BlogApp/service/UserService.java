package com.CodeWithDurgesh.BlogApp.service;

import java.util.List;

import com.CodeWithDurgesh.BlogApp.payloads.FollowUserWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;

public interface UserService {
	
	UserDto registerNewDto(UserDto userDto);
	
	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUser(Integer userId);

	FollowUserWrapper followUser(Integer userId, Integer loggedInUserId);

	FollowUserWrapper unfollowUser(Integer userId, Integer loggedInUserId);

}
