package com.CodeWithDurgesh.BlogApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CodeWithDurgesh.BlogApp.entity.Likes;

public interface LikeRepo extends JpaRepository<Likes, Integer> {
	
	Likes findByPostPostIdAndUserUserId(Integer postId, Integer userId);
}
