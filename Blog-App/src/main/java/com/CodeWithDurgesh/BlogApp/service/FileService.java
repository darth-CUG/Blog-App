package com.CodeWithDurgesh.BlogApp.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	String uploadImage(MultipartFile file) throws IOException;
	
	InputStream getResource(String fileName) throws FileNotFoundException;
}
