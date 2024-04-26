package com.CodeWithDurgesh.BlogApp.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowUserWrapper {
	private String following;
	private String follower;
}
