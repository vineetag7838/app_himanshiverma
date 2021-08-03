package com.nagp.devops.assignment;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;





@SpringBootApplication

public class AssignmentApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentApplication.class);

    private final Environment env;

    public AssignmentApplication(Environment env) {
        this.env = env;
    }

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}
	
	@PostConstruct
    public void logProfile() {
        LOGGER.info("Application has been launched with profiles: ", env.getActiveProfiles());
    }
	
	

	
}
