package com.CodeWithDurgesh.BlogApp.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CodeWithDurgesh.BlogApp.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
	
//	@Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")   @Param("postId")
	Page<Comment> findByPostPostId(Integer postId, Pageable p);

}
