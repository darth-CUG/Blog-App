package com.CodeWithDurgesh.BlogApp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.entity.Role;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ApiException;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.payloads.FollowUserWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;
import com.CodeWithDurgesh.BlogApp.repo.RoleRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.NotificationService;
import com.CodeWithDurgesh.BlogApp.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	NotificationService notificationService;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = dtoToUser(userDto);
		User savedUser = userRepo.save(user);
		return userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updatedUser = userRepo.save(user);
		return userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		return userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
//	@Transactional
	public void deleteUser(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		user.getRoles().clear();
		userRepo.delete(user);
	}
	
	public User dtoToUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
//		User user = new User();
//		user.setUserId(userDto.getUserId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		return user;
	}
	
	public UserDto userToDto(User user) {
		UserDto userDto = modelMapper.map(user, UserDto.class);
//		UserDto userDto = new UserDto();
//		userDto.setUserId(user.getUserId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
		return userDto;
	}

	@Override
	public UserDto registerNewDto(UserDto userDto) {
		
		//validating name: no trailing/leading whitespace, case-insensitive comparison and contains only alphabets
		String name = userDto.getName().trim().toLowerCase();
		if (!name.matches("^[a-zA-Z]+$")){
		    throw new IllegalArgumentException("Username can only contain alphabets.");
		}
		userDto.setName(name);
		
		//ensure unique email in database
		String email = userDto.getEmail();
		Optional<User> emailExists = userRepo.findByEmail(email); 
		if (!emailExists.isEmpty()) {
		  throw new IllegalArgumentException("This email address is already in use.");
		}
//		userRepo.findByEmail(email).orElseThrow(()->new IllegalArgumentException("This email address is already in use."));
//		System.out.println("email------"+email+"-------------"+userRepo.findByEmail(email)+"-------------------"+emailExists+"---"+emailExists.isEmpty());
		User user = modelMapper.map(userDto, User.class);
		
		//encode password
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		//set role of new user - default role: USER_NORMAL
		Role role = roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		
		User newUser = userRepo.save(user);
		
		return modelMapper.map(newUser, UserDto.class);
	} 

	@Override
	public FollowUserWrapper followUser(Integer userId, Integer loggedInUserId) {
		
		User follower = userRepo.findById(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("User","Id",loggedInUserId));
		User following = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		FollowUserWrapper followUserWrapper = new FollowUserWrapper();
		
		//checking if user already follows or is trying to follow self
		if(!follower.getFollowing().contains(following)) {
			if(follower.getUserId()!=following.getUserId()) { //(follower.getFollowing().stream().noneMatch(following -> following.getUserId().equals(loggedInUserId))
				follower.getFollowing().add(following);
				userRepo.save(follower);
				
				followUserWrapper.setFollower(follower.getName());
				followUserWrapper.setFollowing(following.getName());
				
				notificationService.createFollowNotification(loggedInUserId, userId);
				notificationService.createFollowedNotification(userId, loggedInUserId);
			} else {
				throw new ApiException("You can't follow yourself.");
			}
		} else {
			throw new ApiException("You already follow this userId."); //http status will be bad request check ApiException
		}
		
		return followUserWrapper; 
	}
 
	@Override
	public FollowUserWrapper unfollowUser(Integer userId, Integer loggedInUserId) {

		User follower = userRepo.findById(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", loggedInUserId));
		User following = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		
		FollowUserWrapper followUserWrapper = new FollowUserWrapper();
		
		//check if already follows or not
		if(follower.getFollowing().contains(following) && follower.getUserId()!=following.getUserId()) {
			follower.getFollowing().remove(following);
			userRepo.save(follower);
			
			
			followUserWrapper.setFollower(follower.getName());
			followUserWrapper.setFollowing(following.getName());
		} else { 
			throw new ApiException("You don't follow user"+following.getName()+", so you can't unfollow."); 
		}
		
		return followUserWrapper;
	} 

}
