package com.CodeWithDurgesh.BlogApp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.config.CurrentUser;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.ApiResponse;
import com.CodeWithDurgesh.BlogApp.payloads.ImageResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostDto;
import com.CodeWithDurgesh.BlogApp.payloads.PostResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByMeResponse;
import com.CodeWithDurgesh.BlogApp.payloads.SinglePostResponse;
import com.CodeWithDurgesh.BlogApp.service.FileService;
import com.CodeWithDurgesh.BlogApp.service.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/posts")
public class PostController {

	@Autowired
	PostService postService;
	
	@Autowired
	FileService fileService;
	
	@PostMapping("create/user/{userId}/category/{categoryId}")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, 
							@PathVariable("userId") Integer userId, @PathVariable("categoryId") Integer categoryId){
		PostDto createdPost = postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.OK);
	}
	
	//gets the content for a single post with all comments
	@GetMapping("get/{id}") 
	public ResponseEntity<SinglePostResponse> getPostById(@PathVariable("id") Integer postId){
		return new ResponseEntity<SinglePostResponse>(postService.getPostById(postId),HttpStatus.OK);
	}
	
	@GetMapping("getAll") 
	public ResponseEntity<PostResponse> getAllPosts(
					@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
					@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
					@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
					@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){
		return new ResponseEntity<PostResponse>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir),HttpStatus.OK);
	//http://localhost:9090/api/posts/getAll?pageNo=x&pageSize=y
	//http://localhost:9090/api/posts/getAll?pageNo=XXX&pageSize=XXX&sortBy=XXX&sortDir=XXX
	//http://localhost:9090/api/posts/getAll?pageNo=0&pageSize=5&sortBy=title&sortDir=desc
	}
	
	@GetMapping("get/category/{categoryId}")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable("categoryId") Integer CategoryId,
					@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
					@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
					@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy){
		return new ResponseEntity<PostResponse>(postService.getPostsByCategory(CategoryId, pageNo, pageSize, sortBy),HttpStatus.OK);
	//http://localhost:9090/api/posts/get/category/XXX?pageNo=XXX&pageSize=XXX&sortBy=XXX
	//http://localhost:9090/api/posts/get/category/3?pageNo=0&pageSize=2&sortBy=title	
	}
	
	@GetMapping("get/user/{userId}")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable("userId") Integer userId,
					@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
					@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
					@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy){
		return new ResponseEntity<PostResponse>(postService.getPostsByUser(userId,pageNo,pageSize, sortBy),HttpStatus.OK);
	//http://localhost:9090/api/posts/get/user/XXX?pageNo=XXX&pageSize=XXX&sortBy=XXX
	//http://localhost:9090/api/posts/get/user/5?pageNo=0&pageSize=2&sortBy=title
	}
	
	@PutMapping("update/{id}")
	public ResponseEntity<PostDto> updatePost(@PathVariable("id") Integer postId, @Valid @RequestBody PostDto postDto){
		return new ResponseEntity<PostDto>(postService.updatePost(postDto, postId),HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("id") Integer postId){
		postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully.",true),HttpStatus.OK);
	}
	
	//search posts by title
	@GetMapping("search/title/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostsByTitle(@PathVariable("keywords") String keywords){
		return new ResponseEntity<List<PostDto>>(postService.searchPostsByTitle(keywords), HttpStatus.OK);	
	}
	
	//upload image in post
	@PostMapping("image/upload/post/{postId}")
	public ResponseEntity<ImageResponse> uploadPostImage(@RequestParam("image") MultipartFile image, 
						@PathVariable("postId") Integer postId) throws IOException{
		
		PostDto postDto = postService.getPostById2(postId);
		
		String fileName = fileService.uploadImage(image);
		
		postDto.setImageName(fileName);
		PostDto updatedPostDto = postService.updatePost(postDto, postId);
		
		return new ResponseEntity<ImageResponse>(new ImageResponse(updatedPostDto,"Image uploaded to post successfully."),HttpStatus.OK);
	}
	
	//serve image
	@GetMapping(value = "image/download/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = fileService.getResource(imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	//getPostsByUser(userId) gets all posts of any user -- getPostsByMe gets posts of logged in authorised user
	@GetMapping("timeline/myPosts")
	public ResponseEntity<PostsByMeResponse> getPostsByMe(@CurrentUser User loggedInUser,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy){
		return new ResponseEntity<PostsByMeResponse>(postService.getPostsByMe(loggedInUser.getUserId(),pageNo,pageSize,sortBy),HttpStatus.OK);
	}
	
}
	
