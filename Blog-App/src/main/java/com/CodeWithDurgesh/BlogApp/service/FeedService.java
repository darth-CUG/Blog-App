package com.CodeWithDurgesh.BlogApp.service;

import com.CodeWithDurgesh.BlogApp.payloads.PostsByFollowingResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByLikedCatResponse;

public interface FeedService {

	PostsByLikedCatResponse getPostsByLikedCategory(Integer userId, Integer pageNo, Integer pageSize, String sortBy);

	PostsByFollowingResponse getPostsByFollowing(Integer userId, Integer pageNo, Integer pageSize, String sortBy);
	
//	List<PostsByLikedCatWrapper> getPostsByLikedCategory2(Integer userId);
	
	void addLikedCategory(Integer userId, Integer categoryId);


}
