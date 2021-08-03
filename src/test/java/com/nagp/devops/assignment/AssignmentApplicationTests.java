package com.nagp.devops.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nagp.devops.assignment.UserService;

@SpringBootTest
class AssignmentApplicationTests {

	
	@Autowired
	UserService userService;
	// Test to pass as to verify listeners .
		@Test
		 void verifyUserDetails() {
			
			String name = userService.getName();
			assertEquals("Himanshi",name);
			assertNotNull(name);
		}
}
