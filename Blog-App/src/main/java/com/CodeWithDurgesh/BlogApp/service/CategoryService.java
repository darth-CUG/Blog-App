package com.CodeWithDurgesh.BlogApp.service;

import java.util.List;

import com.CodeWithDurgesh.BlogApp.payloads.CategoryDto;

public interface CategoryService {
	
	CategoryDto cretaeCategory(CategoryDto categoryDto);	
	
	CategoryDto getCategory(Integer categoryId);
	
	List<CategoryDto> getAllCategories();
	
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	
	void deleteCategory(Integer categoryId);

}
