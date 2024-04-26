package com.CodeWithDurgesh.BlogApp.config;

public class AppConstants {
	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "5";
	public static final String SORT_BY = "postId";
	public static final String SORT_DIR = "asc";
	
	//JwtTokenHelper.java
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	public static final String SECRET = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	
	//UserServiceImpl
	public static final Integer ADMIN_USER = 501;
	public static final Integer NORMAL_USER = 502;
	
	//PostDto
	public static final Integer ZERO_INTEGER = 0;
	
	//FeedController
	public static final String SORT_BY_CATEGORY_TITLE = "categoryTitle";
	public static final String SORT_BY_POST_TITLE = "postTitle";
	public static final String SORT_BY_COMMENT = "comment";
	public static final String SORT_BY_ADDED_DATE ="addedDate";
	
	//NotificationController
	public static final String SORT_BY_CREATED_AT ="createdAt"; 
	
}
