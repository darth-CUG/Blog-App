package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	 public UserDto(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	//created for unit testing

	private int userId;
	
	@NotEmpty
	@Size(min = 3, message = "Length of name should at least be 3 characters long.")
	private String name;
	
	@NotEmpty
	@Email(message = "Please enter a valid email.")
	private String email;
	
	@NotEmpty
	@Size(min = 6, max = 15, message = "Password must be between 6-15 characters long.")
	@JsonProperty(access = Access.WRITE_ONLY) //and @JsonIgnore both work
	private String password;

	@NotEmpty(message = "About field can not be empty.")
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
	
	private List<CategoryDto> likedCategories;
}
