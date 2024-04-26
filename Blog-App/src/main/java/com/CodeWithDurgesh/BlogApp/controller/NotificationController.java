package com.CodeWithDurgesh.BlogApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.config.CurrentUser;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.NotificationResponse;
import com.CodeWithDurgesh.BlogApp.service.NotificationService;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;

//	@PostMapping("likes")
//	public ResponseEntity<NotificationWrapper> createLikeNotification(@RequestBody Integer likeId){
//		return new ResponseEntity<NotificationWrapper>(notificationService.createLikeNotification(likeId), HttpStatus.OK);
//	}
//	
//	@PostMapping("comments")
//	public ResponseEntity<NotificationWrapper> createCommentNotification(@RequestBody Integer commentId){
//		return new ResponseEntity<NotificationWrapper>(notificationService.createCommentNotification(commentId), HttpStatus.OK);
//	}
//	
//	@PostMapping("youFollowed")
//	public ResponseEntity<NotificationWrapper> createFollowNotification(@CurrentUser User loggedInUser, @RequestBody Integer followingId){
//		return new ResponseEntity<NotificationWrapper>(notificationService.createFollowNotification(loggedInUser.getUserId(),followingId), HttpStatus.OK);
//	}
	
//	@PostMapping("followedYou")
//	public ResponseEntity<NotificationWrapper> createFollowedByNotification(@CurrentUser User loggedInUser, @RequestBody Integer followerId){
//		return new ResponseEntity<NotificationWrapper>(notificationService.createFollowedNotification(loggedInUser.getUserId(),followerId), HttpStatus.OK);
//	}
	
	@GetMapping("get")
	public ResponseEntity<NotificationResponse> getMyNotifications(@CurrentUser User loggedInUser,
			@RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_AT, required = false) String sortBy){
		return new ResponseEntity<NotificationResponse>(notificationService.getNotificationsByUser(loggedInUser,pageNo,pageSize,sortBy),HttpStatus.OK);
	}
	
	@GetMapping("get/userId/{userId}")
	public ResponseEntity<NotificationResponse> getMyNotificationsByUserId(@PathVariable("userId") Integer loggedInUserId, //@CurrentUser User loggedInUser
			@RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_AT, required = false) String sortBy){ //loggedInUser.getUserId()
		return new ResponseEntity<NotificationResponse>(notificationService.getNotificationsByUserId(loggedInUserId,pageNo,pageSize,sortBy),HttpStatus.OK);
	}
}
