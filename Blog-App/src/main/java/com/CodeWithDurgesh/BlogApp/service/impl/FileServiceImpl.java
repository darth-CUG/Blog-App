package com.CodeWithDurgesh.BlogApp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CodeWithDurgesh.BlogApp.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(MultipartFile file) throws IOException {
		
		//file name
		String name = file.getOriginalFilename();
		
		//random file name generation
		String randomID = UUID.randomUUID().toString();
		String randomFileName = randomID.concat(name.substring(name.lastIndexOf(".")));
		
		//full path
		String path = System.getProperty("user.dir")+"/src/main/resources/static/images";
		String filePath = path+File.separator+randomFileName;
		
		//create folder if not created already
		File f = new File(path);
		if(!f.exists()) {
			f.mkdir(); 
		}
		 
		//file copy
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		return randomFileName;
	}

	@Override
	public InputStream getResource(String fileName) throws FileNotFoundException {
		String path = System.getProperty("user.dir")+"/Images";
		String fullPath = path+File.separator+fileName;
		
		InputStream is = new FileInputStream(fullPath); 
		return is;
	}

}
