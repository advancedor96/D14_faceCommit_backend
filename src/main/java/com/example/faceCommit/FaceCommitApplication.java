package com.example.faceCommit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 新增這行
public class FaceCommitApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaceCommitApplication.class, args);
	}

}
