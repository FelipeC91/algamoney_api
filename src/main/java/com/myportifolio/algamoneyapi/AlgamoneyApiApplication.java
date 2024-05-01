package com.myportifolio.algamoneyapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AlgamoneyApiApplication {

	@Autowired
	private static PasswordEncoder encoder;
	public static void main(String[] args) {
//		System.out.println(encoder.encode("admin"));
//		System.out.println(encoder.encode("maria"));
		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}

}
