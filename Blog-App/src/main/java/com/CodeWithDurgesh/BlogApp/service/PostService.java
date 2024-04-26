package com.CodeWithDurgesh.BlogApp.service;

import java.util.List;

//import java.util.List;

import com.CodeWithDurgesh.BlogApp.payloads.PostDto;
import com.CodeWithDurgesh.BlogApp.payloads.PostResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByMeResponse;
import com.CodeWithDurgesh.BlogApp.payloads.SinglePostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	
	SinglePostResponse getPostById(Integer postId); 
	
	PostDto getPostById2(Integer postId);
	
	PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
	
	PostResponse getPostsByCategory(Integer categoryId, Integer pageNo, Integer pageSize, String sortBy);
	
	PostResponse getPostsByUser(Integer userId, Integer pageNo, Integer pageSize, String sortBy);
	
	PostDto updatePost(PostDto postDto, Integer postId);
	
	void deletePost(Integer postId);
	
	List<PostDto> searchPostsByTitle(String keywords);

	PostsByMeResponse getPostsByMe(Integer userId, Integer pageNo, Integer pageSize, String sortBy);


}
