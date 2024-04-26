package com.CodeWithDurgesh.BlogApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.config.CurrentUser;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.ApiResponse;
import com.CodeWithDurgesh.BlogApp.payloads.CommentResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByFollowingResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByLikedCatResponse;
import com.CodeWithDurgesh.BlogApp.service.CommentService;
import com.CodeWithDurgesh.BlogApp.service.FeedService;

@RestController
@RequestMapping("api/feed")
public class FeedController {
	
	@Autowired
	FeedService feedService;
	
	@Autowired
	CommentService commentService;
	
	@GetMapping("getPosts/likedCategory") //@GetMapping("getPosts/likedCategory/user/{userId}") @PathVariable("userId")
	public ResponseEntity<PostsByLikedCatResponse> getPostsByLikedCategory(@CurrentUser User loggedInUser,
					@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
					@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
					@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_ADDED_DATE, required = false) String sortBy){
		return new ResponseEntity<PostsByLikedCatResponse>(feedService.getPostsByLikedCategory(loggedInUser.getUserId(), pageNo, pageSize, sortBy),HttpStatus.OK);
	}

	@PostMapping("user/{userId}/like-category")
	public ResponseEntity<ApiResponse> addLikedCategory(@PathVariable("userId") Integer userId, @RequestBody Integer categoryId){
		feedService.addLikedCategory(userId, categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Liked category saved!",true), HttpStatus.OK);
	}
	
	@GetMapping("comments/post/{postId}")
	public ResponseEntity<CommentResponse> getCommentsgetCommentsByPostId(@PathVariable("postId") Integer postId,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_COMMENT, required = false) String sortBy){
		return new ResponseEntity<CommentResponse>(commentService.getCommentsByPostId(postId, pageNo, pageSize, sortBy),HttpStatus.OK);
	}
	
	@GetMapping("getPosts/following") //@GetMapping("getPosts/following/user/{userId}") 
	public ResponseEntity<PostsByFollowingResponse> getPostsByFollowing(@CurrentUser User loggedInUser, //@PathVariable("userId")
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_ADDED_DATE, required = false) String sortBy){
		return new ResponseEntity<PostsByFollowingResponse>(feedService.getPostsByFollowing(loggedInUser.getUserId(),pageNo,pageSize,sortBy),HttpStatus.OK);
	}
}
