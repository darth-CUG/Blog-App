package com.CodeWithDurgesh.BlogApp.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	
	private int categoryId;
	
	@Size(min = 4, message = "Title should be at least 4 characters long.")
	@NotBlank
	private String categoryTitle;
	
	@Size(min = 10, message = "Decription should be at least 10 characters long.")
	@NotBlank
	private String categoryDescription;

}
