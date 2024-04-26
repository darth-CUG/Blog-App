package com.CodeWithDurgesh.BlogApp.service.impl;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.entity.Category;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.payloads.PostDto;
import com.CodeWithDurgesh.BlogApp.payloads.PostResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByMeResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByMeWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.SinglePostResponse;
import com.CodeWithDurgesh.BlogApp.repo.CategoryRepo;
import com.CodeWithDurgesh.BlogApp.repo.PostRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.PostService;
import com.CodeWithDurgesh.BlogApp.utils.DateFormatter;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	PostRepo postRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	CategoryRepo categoryRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		

		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "user Id", userId));
		Category category = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category", "category Id", categoryId));
		
		Post post = modelMapper.map(postDto, Post.class);
		
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName("default.jpg");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = postRepo.save(post);
		
		return modelMapper.map(newPost, PostDto.class);
	}

	@Override //SINGLE POST PAGE
	public SinglePostResponse getPostById(Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		
		SinglePostResponse singlePost = modelMapper.map(post, SinglePostResponse.class);
	
		singlePost.setAddedDate(DateFormatter.formatDate(post.getAddedDate()));
		singlePost.setCommentCount(post.getComments().size());
		singlePost.setLikesCount(post.getLikes().size());
		return singlePost;
	}
	
	@Override //ORIGINAL
	public PostDto getPostById2(Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		
		PostDto postDto = modelMapper.map(post, PostDto.class);
		postDto.setCommentCount(post.getComments().size());
		postDto.setLikesCount(post.getLikes().size());
		return postDto;
	}

	@Override
	public PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase(AppConstants.SORT_DIR))?
				Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		
		Pageable p = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> pagePost = postRepo.findAll(p);
		
		List<Post> allPosts = pagePost.getContent(); //convert post to postdto
//		List<Post> posts = postRepo.findAll();
		List<PostDto> allPostsDtos = allPosts.stream().map(
				(post) -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(allPostsDtos);
		postResponse.setPageNo(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse; 
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNo, Integer pageSize, String sortBy) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category", "category Id", categoryId));
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Page<Post> pagePost = postRepo.findByCategory(category, p);
		
		List<Post> categoryAllPosts = pagePost.getContent();
//		List<Post> posts = postRepo.findByCategory(category);
		List<PostDto> categoryAllPostsDtos = categoryAllPosts.stream().map(
				(post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(categoryAllPostsDtos);
		postResponse.setPageNo(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public PostResponse getPostsByUser(Integer userId, Integer pageNo, Integer pageSize, String sortBy) {

		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "user Id", userId));
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Page<Post> pagePost = postRepo.findByUser(user, p);
		
		List<Post> userAllPosts = pagePost.getContent();
//		List<Post> posts = postRepo.findByUser(user);
		List<PostDto> userAllPostsDtos = userAllPosts.stream().map(
				(post) -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(userAllPostsDtos);
		postResponse.setPageNo(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = postRepo.save(post);
		return modelMapper.map(updatedPost, PostDto.class);
	}
	
	@Override
	public void deletePost(Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		postRepo.delete(post);
	}

	@Override
	public List<PostDto> searchPostsByTitle(String keywords) {
		
		List<Post> posts = postRepo.findByTitleContaining(keywords);
		List<PostDto> postsDtos = posts.stream().map(
				(post) -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDtos;
	}

	@Override
	public PostsByMeResponse getPostsByMe(Integer userId, Integer pageNo, Integer pageSize, String sortBy) {
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "user Id", userId));
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		
		Page<Post> pagePost = postRepo.findByUser(user, p);
		
		List<Post> postsByMe = pagePost.getContent();
		
		List<PostsByMeWrapper> summaries = postsByMe.stream().map(
				(post) -> {
					PostsByMeWrapper wrapper = new PostsByMeWrapper();
					wrapper.setPostTitle(post.getTitle());
					wrapper.setPostAddedDate(post.getAddedDate());
					wrapper.setCategoryTitle(post.getCategory().getCategoryTitle());
					wrapper.setPostContent(post.getContent());
					wrapper.setLikesCount(post.getLikes().size());
					wrapper.setCommentsCount(post.getComments().size());
					
					return wrapper;
				}).collect(Collectors.toList());
		PostsByMeResponse response = new PostsByMeResponse();
		response.setContent(summaries);
		response.setTotalElements(pagePost.getTotalElements());
		
		return response;
	}
	
	
}
