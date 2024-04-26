package com.CodeWithDurgesh.BlogApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;
import com.CodeWithDurgesh.BlogApp.entity.Role;
import com.CodeWithDurgesh.BlogApp.entity.User;
import com.CodeWithDurgesh.BlogApp.exception.ApiException;
import com.CodeWithDurgesh.BlogApp.payloads.FollowUserWrapper;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;
import com.CodeWithDurgesh.BlogApp.repo.RoleRepo;
import com.CodeWithDurgesh.BlogApp.repo.UserRepo;
import com.CodeWithDurgesh.BlogApp.service.impl.UserServiceImpl;

@SpringBootTest
class UserServiceTest {
	
	@Mock
	UserRepo userRepo;
	
	@Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepo roleRepo;
    
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserServiceImpl userServiceImpl; // as UserServiceImpl implements UserService
    
    @Test
    @DisplayName("Register New User - User Should Get Created With Valid Data")
    public void testRegisterNewDto_withValidData_createsUser() {
       
    	UserDto userDto = new UserDto("JohnDoe", "john.doe@example.com", "securePassword");

		when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty()); // No existing user
		
		when(modelMapper.map(userDto, User.class)).thenReturn(new User()); // Mapped User object
		
		// Mock user object after processing (password hashed)
		User expectedUser = new User(userDto.getName(), userDto.getEmail());
		when(passwordEncoder.encode(anyString())).thenReturn(userDto.getPassword());
//		expectedUser.setPassword(passwordEncoder.encode(userDto.getPassword())); // Assuming password gets encoded
		
		Role expectedRole = new Role(AppConstants.NORMAL_USER,"ROLE_USER"); // Mock expected role
		when(roleRepo.findById(AppConstants.NORMAL_USER)).thenReturn(Optional.of(expectedRole));
		
		when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto); // Map saved user back to Dto
		
		when(userRepo.save(any(User.class))).thenReturn(expectedUser); // Mock userRepo.save to return the expectedUser

		UserDto savedUserDto = userServiceImpl.registerNewDto(userDto);
		
		// Assertions
		assertNotNull(savedUserDto); // Assert user was created and returned
		assertEquals(userDto.getName(), savedUserDto.getName()); // Assert name matches
		assertEquals(userDto.getEmail(), savedUserDto.getEmail()); // Assert email matches
		
    }
    
    @Test
    @DisplayName("IllegalArgumentException - 'This email address is already in use.'")
    public void testRegisterNewDto_withExistingEmail_throwsException() {
    	
	    UserDto userDto = new UserDto("JohnDoe", "existing.email@example.com", "securePassword");
	
	    // Mock expected behavior (existing user with the email)
	    when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new User()));
	
	    // Expect the exception to be thrown
	    try {
	      userServiceImpl.registerNewDto(userDto);
	      fail("Expected IllegalArgumentException for existing email"); // This line will fail the test if no exception is thrown
	    } catch (IllegalArgumentException e) {
	      // Assertion to verify the exception message
	      assertEquals("This email address is already in use.", e.getMessage());
	    }
    }
    
    @Test
    @DisplayName("IllegalArgumentException - 'Username can only contain alphabets.'")
    public void testRegisterNewDto_withInvalidName_throwsException() {
    	
    	// UserDto with invalid name containing special characters
    	UserDto invalidUserDto = new UserDto("John Doe*!@", "john.doe@example.com", "securePassword");
	
	    // Mock expected behavior (existing user with the email)
	    
    	// Expect the exception to be thrown
    	try {
    	  userServiceImpl.registerNewDto(invalidUserDto);
    	  fail("Expected IllegalArgumentException for invalid username"); // This line will fail the test if no exception is thrown
    	} catch (IllegalArgumentException e) {
    	  // Assertion to verify the exception message
    	  assertEquals("Username can only contain alphabets.", e.getMessage());
    	}
    }
    
    @Test
    @DisplayName("Follow User Success")
    public void testFollowUser_Success() {
    	
    	Integer loggedInUserId = 1;
    	Integer userId = 2;
    	String followerName = "JohnDoe";
    	String followingName = "JaneDoe";
    	User follower = new User(loggedInUserId, followerName);
    	User following = new User(userId, followingName);
    	
    	Set<User> followingSet = new HashSet<>(); // Empty following list for follower
        follower.setFollowing(followingSet);

    	// Mock userRepo behavior
    	when(userRepo.save(any(User.class))).thenReturn(follower); // Mock userRepo.save to return the follower
    	when(userRepo.save(any(User.class))).thenReturn(following); // Mock userRepo.save to return the following
    	when(userRepo.findById(loggedInUserId)).thenReturn(Optional.of(follower));
    	when(userRepo.findById(userId)).thenReturn(Optional.of(following)); //when(userRepo.findById(userId)).thenReturn(Optional.of(new User()))
    	
    	// Mock notificationService (optional)
//    	verify(notificationService).createFollowNotification(loggedInUserId, userId);
//    	verify(notificationService).createFollowedNotification(userId, loggedInUserId);
    	
    	// Call the followUser method
        FollowUserWrapper followUserWrapper = userServiceImpl.followUser(userId, loggedInUserId);
        
        // Assertions
        assertNotNull(followUserWrapper); // Ensure a FollowUserWrapper is returned
        assertEquals(follower.getName(), followUserWrapper.getFollower()); // Verify follower name
        assertEquals(following.getName(), followUserWrapper.getFollowing()); // Verify following name

    }
    
    @Test
    @DisplayName("Api Exception - 'You can't follow yourself.'")
    public void testFollowUser_FollowingYourself_throwsException() {
    	
        // Mock user data (follower and following are the same)
        Integer loggedInUserId = 1;
        User user = new User(loggedInUserId, "John Doe");
        
        Set<User> followingSet = new HashSet<>(); // Empty following list for follower
        user.setFollowing(followingSet);

        // Mock userRepo behavior
        when(userRepo.save(any(User.class))).thenReturn(user); // Mock userRepo.save to return the following
        when(userRepo.findById(loggedInUserId)).thenReturn(Optional.of(user));

        // Test fails if no exception is thrown
        ApiException e = assertThrows(ApiException.class, () -> 
        userServiceImpl.followUser(loggedInUserId, loggedInUserId));   // Call the followUser method
        assertEquals("You can't follow yourself.", e.getMessage());
    }

    @Test
    @DisplayName("Api Exception - 'You already follow this userId.'")
    public void testFollowUser_FollowingAnAlreadyFollowedUser_throwsException() {
    	// Mock user data 
        Integer loggedInUserId = 1;
        Integer userId = 2;
        User follower = new User(loggedInUserId, "John Doe");
        User following = new User(userId, "Jane Doe");
        
        //Mock behaviour
        Set<User> followingSet = new HashSet<>(); // Empty following list for follower
        follower.setFollowing(followingSet);
        follower.getFollowing().add(following);
    	
        when(userRepo.save(any(User.class))).thenReturn(follower);
        when(userRepo.save(any(User.class))).thenReturn(following);
        when(userRepo.findById(loggedInUserId)).thenReturn(Optional.of(follower));
        when(userRepo.findById(userId)).thenReturn(Optional.of(following));
        
        ApiException e = assertThrows(ApiException.class, () ->
        userServiceImpl.followUser(userId, loggedInUserId));
        assertEquals("You already follow this userId.", e.getMessage());
    }

    @Test
    @DisplayName("Unfollow User Success")
    public void testUnfollowUser_Success() {
    	Integer loggedInUserId = 1;
        Integer userId = 2;
        User follower = new User(loggedInUserId, "John Doe");
        User following = new User(userId, "Jane Doe");
        
        //Mock behaviour set
        Set<User> followingSet = new HashSet<>(); // Empty following list for follower
        follower.setFollowing(followingSet);
        follower.getFollowing().add(following);
    	
        when(userRepo.save(any(User.class))).thenReturn(follower);
        when(userRepo.save(any(User.class))).thenReturn(following);
        when(userRepo.findById(loggedInUserId)).thenReturn(Optional.of(follower));
        when(userRepo.findById(userId)).thenReturn(Optional.of(following));
        
     // Call the followUser method
        FollowUserWrapper followUserWrapper = userServiceImpl.unfollowUser(userId, loggedInUserId);
        
        // Assertions
        assertNotNull(followUserWrapper); // Ensure a FollowUserWrapper is returned
        assertEquals(follower.getName(), followUserWrapper.getFollower()); // Verify follower name
        assertEquals(following.getName(), followUserWrapper.getFollowing()); // Verify following name
    }
    
    @Test
    @DisplayName("Api Exception - 'You don't follow user, so you can't unfollow.'")
    public void testUnfollowUser_UnfollowNotFollowedUser_throwsException() {
    	
    	// Mock user data 
        Integer loggedInUserId = 1;
        Integer userId = 2;
        User follower = new User(loggedInUserId, "John Doe");
        User following = new User(userId, "Jane Doe");
        
        //Mock behaviour
        Set<User> followingSet = new HashSet<>(); // Empty following list for follower
        follower.setFollowing(followingSet);
        
        when(userRepo.save(any(User.class))).thenReturn(follower);
        when(userRepo.save(any(User.class))).thenReturn(following);
        when(userRepo.findById(loggedInUserId)).thenReturn(Optional.of(follower));
        when(userRepo.findById(userId)).thenReturn(Optional.of(following));
        
        ApiException e = assertThrows(ApiException.class, () ->
        userServiceImpl.unfollowUser(userId, loggedInUserId));
        assertEquals("You don't follow user"+following.getName()+", so you can't unfollow.", e.getMessage());
    }
}
