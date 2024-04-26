package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
	
//	private List<NotificationWrapper> content;
	
	private List<String> content;
	
	private long totalElements;

}
