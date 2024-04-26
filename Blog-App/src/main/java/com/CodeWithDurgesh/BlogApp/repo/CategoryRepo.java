package com.CodeWithDurgesh.BlogApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CodeWithDurgesh.BlogApp.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
