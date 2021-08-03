package com.nagp.devops.assignment;

import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
	
	
private static String name = "Himanshi";
	
public String getName() {
	   
	    
		
		return name;
	}

}
