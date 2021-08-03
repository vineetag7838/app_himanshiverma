package com.nagp.devops.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	
	@Autowired
	UserService userService;
	
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/getName")
	public ResponseEntity<String> getName() {
		
		
		String name=userService.getName();
		
		return new ResponseEntity<>(name, HttpStatus.OK);
	}
	
	
	
}
