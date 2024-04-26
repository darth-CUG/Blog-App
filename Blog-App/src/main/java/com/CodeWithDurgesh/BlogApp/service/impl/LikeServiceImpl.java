package com.CodeWithDurgesh.BlogApp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.entity.Likes;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.repo.LikeRepo;
import com.CodeWithDurgesh.BlogApp.repo.PostRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.LikeService;
import com.CodeWithDurgesh.BlogApp.service.NotificationService;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	PostRepo postRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	LikeRepo likeRepo;
	
	@Autowired
	NotificationService notificationService;
	
	@Override
	public void addLike(Integer postId, Integer userId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "postId", postId));
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "userId", userId));
		
		Likes existingLike = likeRepo.findByPostPostIdAndUserUserId(postId, userId);
		if (existingLike==null) {
			Likes like = new Likes();
			like.setPost(post);
			like.setUser(user);
			like.setAddedDate(new Date());
			likeRepo.save(like);
			
			notificationService.createLikeNotification(user, post);
			
			
		} else {
			//handle error? already exists
		}
	}

	@Override
	public void removeLike(Integer postId, Integer userId) {
		postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "postId", postId));
		userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "userId", userId));
		
		Likes existingLike = likeRepo.findByPostPostIdAndUserUserId(postId, userId);
		
		if(existingLike!=null) {
			likeRepo.delete(existingLike);
		} else {
			//nothing
		}
	}

}
