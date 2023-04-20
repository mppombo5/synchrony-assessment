package me.mppombo.synchronyapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SynchronyApiApplication {
	private final static Logger logger = LoggerFactory.getLogger(SynchronyApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SynchronyApiApplication.class, args);
		logger.info("Synchrony Imgur API is up and running!");
	}
}
