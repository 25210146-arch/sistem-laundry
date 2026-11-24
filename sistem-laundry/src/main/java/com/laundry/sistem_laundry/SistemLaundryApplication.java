package com.laundry.sistem_laundry;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class SistemLaundryApplication {

	@PostConstruct
	public void init() {
		// Mengatur zona waktu seluruh server backend ke WITA (Asia/Makassar)
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Makassar"));
	}

	public static void main(String[] args) {
		SpringApplication.run(SistemLaundryApplication.class, args);
	}

}