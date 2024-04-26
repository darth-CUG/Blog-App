package com.CodeWithDurgesh.BlogApp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.entity.Comment;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.payloads.CommentDto;
import com.CodeWithDurgesh.BlogApp.payloads.CommentResponse;
import com.CodeWithDurgesh.BlogApp.payloads.CommentWrapper;
import com.CodeWithDurgesh.BlogApp.repo.CommentRepo;
import com.CodeWithDurgesh.BlogApp.repo.PostRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.CommentService;
import com.CodeWithDurgesh.BlogApp.service.NotificationService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentRepo commentRepo;
	
	@Autowired
	PostRepo postRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	NotificationService notificationService;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
		Post post = postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "user Id", userId));
		
		Comment comment = modelMapper.map(commentDto, Comment.class);
	
		comment.setPost(post);
		comment.setUser(user);
		comment.setDateAdded(new Date());
		
		notificationService.createCommentNotification(comment, user, post);
		
		Comment savedComment = commentRepo.save(comment);
		return modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = commentRepo.findById(commentId).orElseThrow(
				() -> new ResourceNotFoundException("Comment", "comment Id", commentId));
		commentRepo.delete(comment);
	}

	@Override
	public CommentResponse getCommentsByPostId(Integer postId, Integer pageNo, Integer pageSize, String sortBy) { //define postId as path variable whenever you use it
		postRepo.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("Post", "post Id", postId));
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()); //
		
		Page<Comment> pagePost = commentRepo.findByPostPostId(postId, p);
		
		List<Comment> comments = pagePost.getContent();
		
		List<CommentWrapper> summaries = comments.stream().map((c) -> {
			CommentWrapper wrapper = new CommentWrapper();
			wrapper.setComment(c.getComment());
			wrapper.setByUser(c.getUser().getName());
			wrapper.setDateAdded(c.getDateAdded());
			return wrapper;
		}).collect(Collectors.toList());
		
		
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setContent(summaries);
		commentResponse.setTotalElements(pagePost.getTotalElements());
		
		return commentResponse;
	}
	
	

}
