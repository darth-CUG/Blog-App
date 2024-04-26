package com.CodeWithDurgesh.BlogApp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
	
	public static String formatDate(Date date) {  
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
	    String strDate = formatter.format(date); 
		return strDate;
	}
	 
    
}
