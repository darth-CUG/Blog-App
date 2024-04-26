package com.CodeWithDurgesh.BlogApp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CodeWithDurgesh.BlogApp.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);

	/* getPostsByLikedCategory(userId)
	JOIN u.likedCategories c  -- Join with the 'likedCategories' collection
	JOIN c.posts p            -- Join with the 'posts' collection of the joined 'likedCategories' entity
	 */
//	@Query(value = "SELECT * FROM user u JOIN u.likedCategories c JOIN c.posts p WHERE u.userId =:userId",nativeQuery = true)
//	Page<Post> findPostsByLikedCategory(@Param("userId") Integer userId, Pageable pageable);
	
//	@Query("SELECT p.title, c.categoryTitle, p.addedDate, p.imageName, u.name FROM User u JOIN u.following f JOIN f.posts p JOIN p.category c WHERE u.userId =:userId")
//	Page<PostsByFollowingWrapper> findPostsByFollowing(@Param("userId") Integer userId, Pageable p);
//	
}
