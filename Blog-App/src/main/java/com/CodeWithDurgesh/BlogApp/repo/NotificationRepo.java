package com.CodeWithDurgesh.BlogApp.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CodeWithDurgesh.BlogApp.entity.Notification;
import com.CodeWithDurgesh.BlogApp.entity.User;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {

	Notification findByRecipientAndSenderAndNotificationMessage(User recipient, User sender, String notificationMessage);
	
	Page<Notification> findByRecipient(User recipient, Pageable p);
	
	Page<Notification> findByRecipientUserId(Integer loggedInUserId, Pageable p);
}
