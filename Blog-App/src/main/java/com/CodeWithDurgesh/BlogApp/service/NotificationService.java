package com.CodeWithDurgesh.BlogApp.service;

import com.CodeWithDurgesh.BlogApp.entity.Comment;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.NotificationResponse;

public interface NotificationService {

	void createLikeNotification(User user, Post post); //(Integer likeId)

	void createCommentNotification(Comment comment, User user, Post post); //NotificationWrapper createCommentNotification(Integer commentId);

	void createFollowNotification(Integer loggedInUserId, Integer followingId);

	NotificationResponse getNotificationsByUser(User loggedInUser, Integer pageNo, Integer pageSize, String sortBy); //Integer loggedInUserId
	NotificationResponse getNotificationsByUserId(Integer loggedInUserId, Integer pageNo, Integer pageSize, String sortBy);
	
	void createFollowedNotification(Integer loggedInUserId, Integer followerId);

}
