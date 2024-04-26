package com.CodeWithDurgesh.BlogApp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.entity.Comment;
import com.CodeWithDurgesh.BlogApp.entity.Notification;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ApiException;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.payloads.NotificationResponse;
import com.CodeWithDurgesh.BlogApp.repo.CommentRepo;
import com.CodeWithDurgesh.BlogApp.repo.LikeRepo;
import com.CodeWithDurgesh.BlogApp.repo.NotificationRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.NotificationService;
import com.CodeWithDurgesh.BlogApp.utils.NotificationType;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	NotificationRepo notificationRepo;
	
	@Autowired
	LikeRepo likeRepo;
	
	@Autowired
	CommentRepo commentRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public void createLikeNotification(User user, Post post) { //createLikeNotification(Integer likeId)
//		Likes like = likeRepo.findById(likeId).orElseThrow(
//				() -> new ResourceNotFoundException("Like", "like id", likeId));
//		User user = like.getUser();
//		Post post = like.getPost();
		
		String notificationMessage = user.getName()+" liked your post: "+post.getTitle();
		
		Notification notif = new Notification();
		Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(post.getUser(), user, notificationMessage);
		if (existingNotification==null){
			notif.setRecipient(post.getUser()); //post owner
			notif.setSender(user);  //liker
			notif.setOnPost(post.getPostId());
//			notif.setIsFollow(false);
			notif.setNotificationType(NotificationType.LIKED);
			notif.setNotificationMessage(notificationMessage);
			notif.setCreatedAt(new Date()); //like.getAddedDate()
		
			notificationRepo.save(notif);
		} else {
			throw new ApiException("You have already liked this post: "+post.getTitle());
		}
//		NotificationWrapper wrapper = modelMapper.map(notif, NotificationWrapper.class);
//		return wrapper;
	}

	@Override
	public void createCommentNotification(Comment comment, User user, Post post) {
//		Comment comment = commentRepo.findById(commentId).orElseThrow(
//				() -> new ResourceNotFoundException("Comment", "comment id", commentId));
//		
//		User user = comment.getUser();
//		Post post = comment.getPost();
		
		String notificationMessage = user.getName()+" commented on your post, "+post.getTitle()+": "+comment.getComment();	
	
		Notification notif = new Notification();
		Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(post.getUser(), user, notificationMessage);
		if (existingNotification==null){
			notif.setRecipient(post.getUser()); //post owner
			notif.setSender(user);  //Commenter id
			notif.setOnPost(post.getPostId());
//			notif.setIsFollow(false);
			notif.setNotificationType(NotificationType.COMMENTED);
			notif.setNotificationMessage(notificationMessage);
			notif.setCreatedAt(new Date());
		
			notificationRepo.save(notif);
		} else {
			throw new ApiException("You have already commented this.");
		}
		
//		NotificationWrapper wrapper = modelMapper.map(notif, NotificationWrapper.class);
//		return wrapper;
	}

	@Override
	public void createFollowNotification(Integer loggedInUserId, Integer followingId) {
		User loggedInUser = userRepo.findById(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("Logged in user", "user id", loggedInUserId));
		
		//retrieve information about the user whom you started following using loggedInUserId
		Set<User> followingUsers = loggedInUser.getFollowing();
		User followingUser = userRepo.findById(followingId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", followingId));
	
		String notificationMessage = "You started following "+followingUser.getName()+".";
		
		Notification notif = new Notification();
		
		
		Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(loggedInUser, followingUser, notificationMessage);

		if(followingUsers.contains(followingUser) && existingNotification==null) {
			
			notif.setRecipient(loggedInUser); //the user being followed
			notif.setSender(followingUser); //follower
//			notif.setIsFollow(true);
			notif.setCreatedAt(new Date());
			notif.setNotificationType(NotificationType.FOLLOWED);
			notif.setNotificationMessage(notificationMessage);

			notificationRepo.save(notif);
		
		} else {
			throw new ResourceNotFoundException("User in your following list was","user id",followingId); //not found
		}
//		} catch (Exception e) {
//
//			  System.out.println("\nMultiple notifications found for recipient: "+loggedInUser+", sender: "+followingUser+", message: "+notificationMessage);
//		}
		
//		NotificationWrapper wrapper = modelMapper.map(notif,NotificationWrapper.class);
//		return wrapper;
	}
	
	@Override
	public void createFollowedNotification(Integer loggedInUserId, Integer followerId) {
		User loggedInUser = userRepo.findById(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("Logged in user", "user id", loggedInUserId));
		
		Set<User> followerUsers = loggedInUser.getFollowers();
		User followerUser = userRepo.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", followerId));
		
		String notificationMessage = followerUser.getName()+" started following you.";
		
		Notification notif = new Notification();
		
		Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(loggedInUser, followerUser, notificationMessage);

		if(followerUsers.contains(followerUser) && existingNotification==null) {
			notif.setRecipient(loggedInUser);
			notif.setSender(followerUser);
			notif.setCreatedAt(new Date());
			notif.setNotificationType(NotificationType.FOLLOWED);
			notif.setNotificationMessage(notificationMessage);
			
			notificationRepo.save(notif);
		} else {
			throw new ResourceNotFoundException("User in your following list was","user id",followerId);
		}
//		NotificationWrapper wrapper = modelMapper.map(notif,NotificationWrapper.class);
//		return wrapper;
	}

	@Override
	public NotificationResponse getNotificationsByUser(User loggedInUser, Integer pageNo, Integer pageSize, String sortBy) { //.getUserId()
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); //Sort.by(sortBy)
		Page<Notification> pagePost = notificationRepo.findByRecipient(loggedInUser,p);
		
		List<Notification> notifications = pagePost.getContent();
		
//		List<NotificationWrapper> wrapper = notifications.stream().map(
//				(notif) -> modelMapper.map(notif, NotificationWrapper.class)).collect(Collectors.toList());
		
		NotificationResponse notificationResponse = new NotificationResponse();
//		notificationResponse.setContent(wrapper);
		notificationResponse.setContent(notifications.stream().map(Notification::getNotificationMessage).collect(Collectors.toList()));
		notificationResponse.setTotalElements(pagePost.getTotalElements());
		
		return notificationResponse;
	}



	@Override
	public NotificationResponse getNotificationsByUserId(Integer loggedInUserId, Integer pageNo, Integer pageSize, String sortBy) {
		Pageable p = PageRequest.of(pageNo, 20, Sort.by(sortBy).descending()); //Sort.by(sortBy)
		Page<Notification> pagePost = notificationRepo.findByRecipientUserId(loggedInUserId,p);
		
		List<Notification> notifications = pagePost.getContent();
		
//		List<NotificationWrapper> wrapper = notifications.stream().map(
//				(notif) -> modelMapper.map(notif, NotificationWrapper.class)).collect(Collectors.toList());
		
		NotificationResponse notificationResponse = new NotificationResponse();
//		notificationResponse.setContent(wrapper);
		notificationResponse.setContent(notifications.stream().map(Notification::getNotificationMessage).collect(Collectors.toList()));
		notificationResponse.setTotalElements(pagePost.getTotalElements());
		
		return notificationResponse;
	}

}

//@Override
//public NotificationWrapper createCommentNotification(Integer commentId) {
//	Comment comment = commentRepo.findById(commentId).orElseThrow(
//			() -> new ResourceNotFoundException("Comment", "comment id", commentId));
//	
//	User user = comment.getUser();
//	Post post = comment.getPost();
//	
//	String notificationMessage = user.getName()+" commented on your post, "+post.getTitle()+": "+comment.getComment();	
//
//	Notification notif = new Notification();
//	Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(post.getUser(), user, notificationMessage);
//	if (existingNotification==null){
//		notif.setRecipient(post.getUser()); //post owner
//		notif.setSender(user);  //Commenter id
//		notif.setOnPost(post.getPostId());
////		notif.setIsFollow(false);
//		notif.setNotificationType(NotificationType.COMMENTED);
//		notif.setNotificationMessage(notificationMessage);
//		notif.setCreatedAt(comment.getDateAdded());
//	
//		notificationRepo.save(notif);
//	} else {
//		throw new ApiException("You have already commented this: "+comment.getComment());
//	}
//	
//	NotificationWrapper wrapper = modelMapper.map(notif, NotificationWrapper.class);
//	return wrapper;
//}

//@Override
//public NotificationWrapper createFollowNotification(Integer loggedInUserId, Integer followingId) {
//	User loggedInUser = userRepo.findById(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("Logged in user", "user id", loggedInUserId));
//	
//	//retrieve information about the user whom you started following using loggedInUserId
//	Set<User> followingUsers = loggedInUser.getFollowing();
//	User followingUser = userRepo.findById(followingId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", followingId));
//
//	String notificationMessage = "You started following "+followingUser.getName()+".";
//	
//	Notification notif = new Notification();
//	
//	
//	Notification existingNotification = notificationRepo.findByRecipientAndSenderAndNotificationMessage(loggedInUser, followingUser, notificationMessage);
//
//	if(followingUsers.contains(followingUser) && existingNotification==null) {
//		
//		notif.setRecipient(loggedInUser); //the user being followed
//		notif.setSender(followingUser); //follower
////		notif.setIsFollow(true);
//		notif.setNotificationType(NotificationType.FOLLOWED);
//		notif.setNotificationMessage(notificationMessage);
//
//		notificationRepo.save(notif);
//	
//	} else {
//		throw new ResourceNotFoundException("User in your following list was","user id",followingId); //not found
//	}
////	} catch (Exception e) {
////
////		  System.out.println("\nMultiple notifications found for recipient: "+loggedInUser+", sender: "+followingUser+", message: "+notificationMessage);
////	}
//	
//	NotificationWrapper wrapper = modelMapper.map(notif,NotificationWrapper.class);
//	return wrapper;
//}
