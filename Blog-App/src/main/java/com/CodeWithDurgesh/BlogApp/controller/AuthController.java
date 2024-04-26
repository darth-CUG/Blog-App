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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.CodeWithDurgesh.BlogApp.payloads.JwtAuthRequest;
import com.CodeWithDurgesh.BlogApp.payloads.JwtAuthResponse;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;
import com.CodeWithDurgesh.BlogApp.payloads.jsp.UserForm;
import com.CodeWithDurgesh.BlogApp.security.JwtTokenHelper;
import com.CodeWithDurgesh.BlogApp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth") 
//@Controller
public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("login") // and "/register" BOTH WORK
	public ModelAndView showLoginPage(@ModelAttribute("request") JwtAuthRequest request, ModelMap model) {
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}
	
//	@PostMapping("login")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<ModelAndView> createToken(@ModelAttribute("request") JwtAuthRequest request, ModelMap model) throws Exception{ //@Valid @RequestBody
		
		authenticate(request.getUsername(), request.getPassword());
		
		//get user details from token
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		
		//extract token
		String token = jwtTokenHelper.generateToken(userDetails);
		
		//to send
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		
		model.put("token", token);
		
		ModelAndView mav = new ModelAndView("redirect:/api/auth/welcome");
		mav.setViewName("welcome");
		
		return new ResponseEntity<ModelAndView>(mav,HttpStatus.OK);
	}

//	//ORIGINAL
//	@RequestMapping(value = "login", method = RequestMethod.POST)
//	public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthRequest request) throws Exception{ 
//		
//		authenticate(request.getUsername(), request.getPassword());
//		
//		//get user details from token
//		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//		
//		//extract token
//		String token = jwtTokenHelper.generateToken(userDetails);
//		
//		//to send
//		JwtAuthResponse response = new JwtAuthResponse();
//		response.setToken(token);
//	
//		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
//	}
	
	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid details");
			throw new Exception("Invalid user name or password.");
		} 
	}
	
//	@RequestMapping(value = "/login", method = RequestMethod.GET) 
//	public String showLoginPage(ModelMap model) {
//		return "webapp/WEB-INF/view/newlogin"; //webapp/WEB-INF/view/login.jsp
//	}
	
////	@GetMapping(value = "/login")  //, produces = "application/json"
//	@RequestMapping(value = "/login", method = RequestMethod.GET) 
//	public ModelAndView showLoginPage(ModelMap model) {
//		ModelAndView mav = new ModelAndView("newlogin");
//		return mav;
//	}
	
//	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@GetMapping("register") // and "/register" BOTH WORK
	public ModelAndView showRegisterPage(@ModelAttribute("userForm") UserForm userForm, ModelMap model) {
		ModelAndView mav = new ModelAndView("register");
		return mav;
	}
	
//	@RequestMapping(value = "/register", method = RequestMethod.GET)
//	public String showRegisterPage(ModelMap model) {
//		return "register";
//	} 
	
//	 
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ModelAndView> resgisterUser(@ModelAttribute(name="userForm") UserForm userForm, ModelMap model) { //ResponseEntity<ModelAndView>  @Valid
		
		UserDto userDto = modelMapper.map(userForm, UserDto.class);
		
		userService.registerNewDto(userDto);
		
//		model.addAttribute("registationSuccess", true);
		model.addAttribute("userForm", new UserForm());
		
		ModelAndView mav = new ModelAndView("redirect:/api/auth/welcome");
		mav.setViewName("welcome");
		
		return new ResponseEntity<ModelAndView>(mav,HttpStatus.CREATED);
	}
	
	@GetMapping("welcome") // and "/register" BOTH WORK
	public ModelAndView showWelcomePage(ModelMap model) {
		ModelAndView mav = new ModelAndView("welcome");
		return mav;
	}
	
//	@PostMapping("register") //ORIGINAL
//	public ResponseEntity<UserDto> resgisterUser(@Valid @RequestBody UserDto userDto) {
//		
//		UserDto registeredUser = userService.registerNewDto(userDto);
//		
//		return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
//	}
	
}
