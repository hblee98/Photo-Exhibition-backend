package com.hanbinlee.photoExhibition;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.hanbinlee.photoExhibition.service.PhotoService;

@SpringBootApplication
public class PhotoExhibitionApplication {

	private final PhotoService photoService;

	public PhotoExhibitionApplication(PhotoService photoService) {
		this.photoService = photoService;
	}

	public static void main(String[] args) {
		SpringApplication.run(PhotoExhibitionApplication.class, args);
	}

	@Bean
	@SuppressWarnings("unused")
	public CommandLineRunner run() {
		return args -> {
			try {
				photoService.savePhotoInfo();
			} catch (Exception e) {
				System.err.println("Error while saving photos: " + e.getMessage());
			}
		};
	}
}
