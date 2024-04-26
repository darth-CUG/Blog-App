package com.CodeWithDurgesh.BlogApp.repo;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.CodeWithDurgesh.BlogApp.entity.Category;
import com.CodeWithDurgesh.BlogApp.entity.Comment;
import com.CodeWithDurgesh.BlogApp.entity.Likes;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.Role;
import com.CodeWithDurgesh.BlogApp.entity.User;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRepoTest {
	
	@Autowired
	UserRepo userRepo;
	
	@Mock
	private List<Post> mockPost;
	@Mock
	private List<Comment> mockComment;
	@Mock
	private List<Category> mockCategories;
	@Mock
	private Set<Likes> mockLikes;
	@Mock
	private Set<Role> mockRoles;

	@Test
	@DisplayName("User Found by findById()")
	void findById() {
		// Arrange (Prepare test data efficiently)
		Integer userId = 1;
        String name = "jane";
        String email = "janedoe@gmail.com";
        String password = "hashedPassword";
        String about = "i am a dummy user";

        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setName(name);
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setAbout(about);
        userRepo.save(expectedUser);

        // Act (Perform the operation being tested)
        Optional<User> actualResult = userRepo.findById(expectedUser.getUserId());

        // Assert (Verify the expected outcome)
        assertThat(actualResult).isPresent();  // Assert that a user is found  
//        assertThat(actualResult.get())
//	        .usingRecursiveComparison()
//	        .ignoringFields("password") // Compare excluding password
//	        .isEqualTo(expectedUser);  
	}

	@Test
	@DisplayName("User Found by findByEmail()")
	void findByEmail() {
		// Arrange (Prepare test data efficiently)
		Integer userId = 1;
        String name = "jane";
        String email = "janedoe@gmail.com";
        String password = "hashedPassword";
        String about = "i am a dummy user";

        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setName(name);
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setAbout(about);
        userRepo.save(expectedUser);

        // Act (Perform the operation being tested)
        Optional<User> actualResult = userRepo.findByEmail(expectedUser.getEmail());

        // Assert (Verify the expected outcome)
        assertThat(actualResult).isPresent();
	}
}
