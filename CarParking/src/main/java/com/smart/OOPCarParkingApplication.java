package com.smart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OOPCarParkingApplication implements CommandLineRunner  {
	
	@Autowired
	private BCryptPasswordEncoder bryBCryptPasswordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(OOPCarParkingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

	public BCryptPasswordEncoder getBryBCryptPasswordEncoder() {
		return bryBCryptPasswordEncoder;
	}

	public void setBryBCryptPasswordEncoder(BCryptPasswordEncoder bryBCryptPasswordEncoder) {
		this.bryBCryptPasswordEncoder = bryBCryptPasswordEncoder;
	}

}
