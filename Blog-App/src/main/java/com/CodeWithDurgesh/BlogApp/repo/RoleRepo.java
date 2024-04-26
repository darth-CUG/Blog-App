package com.CodeWithDurgesh.BlogApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CodeWithDurgesh.BlogApp.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
