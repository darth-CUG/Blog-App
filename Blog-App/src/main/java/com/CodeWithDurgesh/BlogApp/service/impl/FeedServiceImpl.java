package com.CodeWithDurgesh.BlogApp.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.CodeWithDurgesh.BlogApp.entity.Category;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ResourceNotFoundException;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByLikedCatWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByFollowingResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByFollowingWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByLikedCatResponse;
import com.CodeWithDurgesh.BlogApp.repo.CategoryRepo;
import com.CodeWithDurgesh.BlogApp.repo.PostRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.FeedService;
import com.CodeWithDurgesh.BlogApp.utils.DateFormatter;

@Service
public class FeedServiceImpl implements FeedService{
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	PostRepo postRepo;
	
	@Autowired
	CategoryRepo categoryRepo;
	
	@Autowired
	ModelMapper modelMapper;

	public PostsByLikedCatResponse getPostsByLikedCategory(Integer userId, Integer pageNo, Integer pageSize, String sortBy) {
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "userId", userId));
		
		// get list of categories liked by a user
		//the liked categories of a user are stored in the many-to-many relationship table 
		List<Category> likedCategories = user.getLikedCategories();
		
//		// get list of category ids liked by a user
		List<Integer> categoryIds = likedCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
		System.out.println("\ncategory ids:"+categoryIds);
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		
		Page<Post> pagePost = postRepo.findByCategoryIdIn(categoryIds, p);
		
//		List<Post> likedCategoriesPosts = new ArrayList<>();
//		for (Category category : likedCategories) {
//			System.out.println(category.getPosts().size()+" posts of liked category: "+category.getCategoryTitle()); //"----"
//			Page<Post> pagePost = postRepo.findByCategory(category, p);
//			likedCategoriesPosts.addAll(pagePost.getContent());
////			likedCategoriesPosts.addAll(category.getPosts());
//		}
		
		List<Post> likedCategoriesPosts = pagePost.getContent();
//		List<PostDto> likedCategoriesPostsDtos = likedCategoriesPosts.stream().map(
//				(post) -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		System.out.println("\nNumber of posts in likedcategories:"+likedCategoriesPosts.size());
		
		List<PostsByLikedCatWrapper> summaries = likedCategoriesPosts.stream() 
	            .map(post -> {
	            	PostsByLikedCatWrapper wrapper = new PostsByLikedCatWrapper();
	            	wrapper.setPostId(post.getPostId());
	            	wrapper.setPostTitle(post.getTitle());
	            	wrapper.setPostContent(post.getContent());
	            	wrapper.setPostImageName(post.getImageName()); //("/Blog-App/Images/"+post.getImageName())
	            	wrapper.setPostAddedDate(DateFormatter.formatDate(post.getAddedDate()));
	            	wrapper.setCategoryTitle(post.getCategory().getCategoryTitle());
	            	wrapper.setWrittenBy(post.getUser().getName());
	            	wrapper.setCommentsCount(post.getComments().size());
	            	wrapper.setLikesCount(post.getLikes().size());
	                return wrapper;
	            }
	            ).collect(Collectors.toList());
		
		PostsByLikedCatResponse wrapperResponse = new PostsByLikedCatResponse();
		wrapperResponse.setContent(summaries);
//		wrapperResponse.setTotalElements(pagePost.getTotalElements());
		wrapperResponse.setTotalPages(pagePost.getTotalPages()); //changes for JSP pagination logic in feed.jsp
		wrapperResponse.setCurrentPage(pageNo); //added for JSP pagination logic in feed.jsp;
		
		return wrapperResponse;
	}

	@Override
	public void addLikedCategory(Integer userId, Integer categoryId) {
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "userId", userId));
		
		Category newLikedCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		user.getLikedCategories().add(newLikedCategory);
		userRepo.save(user);
		
	}

//	@Override
//	public List<PostsByLikedCatWrapper> getPostsByLikedCategory2(Integer userId) {
//		User user = userRepo.findById(userId).orElseThrow(
//				() -> new ResourceNotFoundException("User", "userId", userId));
//		
//		Pageable p = PageRequest.of(0, 5, Sort.by("postId").ascending());
//		
//		Page<Post> pagePost = postRepo.findPostsByLikedCategory(userId, p);
//		
//		List<Post> likedCategoriesAllPosts = pagePost.getContent();
//		
//		 // Map Post to PostSummaryWrapper
//	    List<PostsByLikedCatWrapper> summaries = likedCategoriesAllPosts.stream()
//	            .map(post -> {
//	            	PostsByLikedCatWrapper wrapper = new PostsByLikedCatWrapper();
//	            	wrapper.setPostTitle(post.getTitle());
//	            	wrapper.setPostContent(post.getContent());
//	            	wrapper.setPostImageName(post.getImageName());
//	            	wrapper.setPostAddedDate(post.getAddedDate());
//	            	wrapper.setCategoryTitle(post.getCategory().getCategoryTitle());
//	            	wrapper.setWrittenBy(post.getUser().getUsername());
//	            	wrapper.setCommentsCount(post.getComments().size());
//	            	
//	            	wrapper.setTotalElements(pagePost.getTotalElements());
//	            	
//	                return wrapper;
//	            }
//	            ).collect(Collectors.toList());
//		
//	
//		return summaries;
//		return null;
//	}

	@Override
	public PostsByFollowingResponse getPostsByFollowing(Integer userId, Integer pageNo, Integer pageSize, String sortBy) {
		User user = userRepo.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User", "user id", userId));
		
		Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()); 
		
		Set<User> followingUsers = user.getFollowing();
		List<Integer> followingUsersIds = followingUsers.stream().map(User::getUserId).collect(Collectors.toList()); 
		
		Page<Post> pagePost = postRepo.findByUserUserIdIn(followingUsersIds, p); 

		List<Post> followingUsersPosts = pagePost.getContent();
		
		List<PostsByFollowingWrapper> summaries = followingUsersPosts.stream().map(
				(post) -> {
					PostsByFollowingWrapper wrapper = new PostsByFollowingWrapper();
					wrapper.setPostId(post.getPostId());
					wrapper.setPostTitle(post.getTitle());
					wrapper.setCategoryTitle(post.getCategory().getCategoryTitle());
					wrapper.setWrittenBy(post.getUser().getName());
					wrapper.setPostAddedDate(DateFormatter.formatDate(post.getAddedDate()));
					wrapper.setPostImageName(post.getImageName());
					
					return wrapper;
				}).collect(Collectors.toList());

		PostsByFollowingResponse response = new PostsByFollowingResponse();
		response.setContent(summaries);
		response.setTotalElements(pagePost.getTotalElements());
		
		return response;
	}
	


	
}
