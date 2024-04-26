package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;

import com.CodeWithDurgesh.BlogApp.utils.NotificationType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationWrapper {
	
	private int notificationId;
	
	private Integer recipientId;
	
	private Integer senderId;
	
//	private String targetEntityType;
//	private int targetEntityId;
//	private Boolean isFollow;
	
	private Integer postId;
	
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;
	
	private String notificationMessage;
	
	private Date createdAt;

}
