package com.CodeWithDurgesh.BlogApp.service;

public interface LikeService {
	
	void addLike(Integer postId, Integer userId);

	void removeLike(Integer postId, Integer userId);

}
