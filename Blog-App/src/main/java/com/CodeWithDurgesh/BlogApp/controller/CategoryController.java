package com.CodeWithDurgesh.BlogApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.payloads.ApiResponse;
import com.CodeWithDurgesh.BlogApp.payloads.CategoryDto;
import com.CodeWithDurgesh.BlogApp.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("create")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		return new ResponseEntity<CategoryDto>(categoryService.cretaeCategory(categoryDto),HttpStatus.OK);
	}
	
	@GetMapping("get/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Integer categoryId) {
		return new ResponseEntity<CategoryDto>(categoryService.getCategory(categoryId),HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		return new ResponseEntity<List<CategoryDto>>(categoryService.getAllCategories(),HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("update/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("id") Integer categoryId){
		return new ResponseEntity<CategoryDto>(categoryService.updateCategory(categoryDto, categoryId),HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Integer categoryId){
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully.", true),HttpStatus.OK);
	}
}
