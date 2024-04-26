package com.CodeWithDurgesh.BlogApp.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.CodeWithDurgesh.BlogApp.entity.Category;
import com.CodeWithDurgesh.BlogApp.entity.Post;
import com.CodeWithDurgesh.BlogApp.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer>{
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	//pagination and sorting
	Page<Post> findByCategory(Category category, Pageable pageable);
	Page<Post> findByUser(User user, Pageable pageable);
	
	//search
	List<Post> findByTitleContaining(String title);
	
//	@Query(value = "SELECT p.category_id, p.post_id, p.user_id, p.added_date, p.title, p.content, p.image_name FROM post p INNER JOIN categories c ON p.category_id = c.category_id WHERE c.category_id IN (:categoryIds);", nativeQuery = true) // pagination not working
	@Query("SELECT p FROM Post p JOIN p.category c WHERE c.categoryId IN :categoryIds") //*
	Page<Post> findByCategoryIdIn(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);
	
//	@Query("SELECT p FROM Post p JOIN p.user u WHERE u.userId IN :followingUserIds")    @Param("followingUserIds") 
	Page<Post> findByUserUserIdIn(List<Integer> followingUserIds, Pageable pageable);
	
//	Page<Post> findAll(Pageable p);
	
}
 