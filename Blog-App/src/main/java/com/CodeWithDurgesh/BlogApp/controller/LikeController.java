package com.CodeWithDurgesh.BlogApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.service.LikeService;

@RestController
@RequestMapping("api/likes")
public class LikeController {
	 
	@Autowired
	LikeService likeService;
	
	@PostMapping("addLike/post/{postId}/user/{userId}") 
	public void addLike(@PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {
		likeService.addLike(postId, userId);
	}
	
	@DeleteMapping("removeLike/post/{postId}/user/{userId}")
	public void removeLike(@PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId){
		likeService.removeLike(postId,userId); 
	}
}
 