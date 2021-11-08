package com.bitravel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	@DisplayName("password encoder test")
	void passwordEncode() {
		String rawPassword = "lotuslivesinpurewater311";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		System.out.println(encodedPassword);
		assertAll(
				() -> assertNotEquals(rawPassword, encodedPassword),
				() -> assertTrue(passwordEncoder.matches(rawPassword, encodedPassword))
				);
	}
}
