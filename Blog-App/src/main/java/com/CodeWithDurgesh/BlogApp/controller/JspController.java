package com.CodeWithDurgesh.BlogApp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.payloads.JwtAuthRequest;
import com.CodeWithDurgesh.BlogApp.payloads.JwtAuthResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostDto;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByFollowingResponse;
import com.CodeWithDurgesh.BlogApp.payloads.PostsByLikedCatResponse;
import com.CodeWithDurgesh.BlogApp.payloads.SinglePostResponse;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;
import com.CodeWithDurgesh.BlogApp.payloads.jsp.PostForm;
import com.CodeWithDurgesh.BlogApp.payloads.jsp.UserForm;
import com.CodeWithDurgesh.BlogApp.security.JwtTokenHelper;
import com.CodeWithDurgesh.BlogApp.service.FeedService;
import com.CodeWithDurgesh.BlogApp.service.PostService;
import com.CodeWithDurgesh.BlogApp.service.UserService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("token")
public class JspController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	FeedService feedService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	//------------------------------------------------REGISTER---------------------------------------------------------------------------------------
	
	@RequestMapping(value = "register", method = RequestMethod.GET) // and "/register" BOTH WORK
	public String showRegisterPage(@ModelAttribute("userForm") UserForm userForm, ModelMap model) {
		return "register";
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String resgisterUser(@ModelAttribute("userForm") UserForm userForm, ModelMap model) { //ResponseEntity<ModelAndView>  @Valid
		
		UserDto userDto = modelMapper.map(userForm, UserDto.class);
		
		userService.registerNewDto(userDto);
		
//		model.addAttribute("registationSuccess", true);
		model.addAttribute("userForm", new UserForm());

		
		return "redirect:/login";  //new ResponseEntity<ModelAndView>(mav,HttpStatus.CREATED)
	}
	
	//------------------------------------------------LOGIN---------------------------------------------------------------------------------------

	@RequestMapping(value = "login", method = RequestMethod.GET) // and "/register" BOTH WORK
	public String showLoginPage(@ModelAttribute("request") JwtAuthRequest request, ModelMap model) throws Exception {
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String createToken(@ModelAttribute("request") JwtAuthRequest request, ModelMap model) throws Exception{ //@Valid @RequestBody
		
		authenticate(request.getUsername(), request.getPassword());
		
		//get user details from token
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		
		//extract token
		String token = jwtTokenHelper.generateToken(userDetails);
		
		//to send
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		
		model.put("token", token);
		
		return "redirect:/feed";
	}
	
	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid details");
			throw new Exception("Invalid user name or password.");
		} 
	}
	
	//------------------------------------------------FEED---------------------------------------------------------------------------------------

	@RequestMapping(value = "feedTry", method = RequestMethod.GET)
	public String getFeedTry() {
		return "feedTRY";
	}
	
	
	@RequestMapping(value = "feed", method = RequestMethod.GET)
	public String getFeed(
			@ModelAttribute("loggedInUser") User loggedInUser, //@CurrentUser
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo, 
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_ADDED_DATE, required = false) String sortBy, 
			ModelMap model) {
		
		PostsByLikedCatResponse catResponse = feedService.getPostsByLikedCategory(1, pageNo, pageSize, sortBy); //loggedInUser.getUserId()
		
		PostsByFollowingResponse followingResponse = feedService.getPostsByFollowing(1, pageNo, pageSize, sortBy);
		
		// Add data to model
		model.addAttribute("totalPages", catResponse.getTotalPages());
		model.addAttribute("postsByLikedCategories", catResponse.getContent());
		model.addAttribute("currentPage", catResponse.getCurrentPage());
		model.addAttribute("postsByFollowing", followingResponse.getContent());
		
		return "feed";
	}
	
	//------------------------------------------------SINGLE POST---------------------------------------------------------------------------------------

	@RequestMapping(value = "singlePost/{postId}", method = RequestMethod.GET)
	public String getSinglePost(@ModelAttribute("postId") Integer postId, ModelMap model) {
		SinglePostResponse response = postService.getPostById(postId);
		
		//add data to model
		model.addAttribute("post", response);
		
		return "singlePost";
	}
	
//	@GetMapping("get/{id}") 
//	public ResponseEntity<SinglePostResponse> getPostById(@PathVariable("id") Integer postId){
//		return new ResponseEntity<SinglePostResponse>(postService.getPostById(postId),HttpStatus.OK);
//	}

	//------------------------------------------------CREATE NEW POST---------------------------------------------------------------------------------------

	@RequestMapping(value = "createNewPost/user/{userId}/category/{categoryId}", method = RequestMethod.GET)
	public String showCreatePostPage(@ModelAttribute("postForm") PostForm postForm, @PathVariable("userId") Integer userId,  //@Valid
			@PathVariable("categoryId") Integer categoryId, ModelMap model) {
				return "createNewPost";
	}

//	@PostMapping("create/user/{userId}/category/{categoryId}")
	@RequestMapping(value = "createNewPost/user/{userId}/category/{categoryId}", method = RequestMethod.POST)
	public String createPost(@ModelAttribute("postForm") PostForm postForm, @PathVariable("userId") Integer userId,  //@Valid
			@PathVariable("categoryId") Integer categoryId, ModelMap model){
		
		PostDto postDto = modelMapper.map(postForm, PostDto.class);
		
		postService.createPost(postDto, userId, categoryId);
		
		model.addAttribute("postForm", new PostForm());
		 model.addAttribute("userId", userId);
	     model.addAttribute("categoryId", categoryId);
		return "welcome";
	}

}
