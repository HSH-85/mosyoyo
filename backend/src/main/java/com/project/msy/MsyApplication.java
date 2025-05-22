package com.project.msy;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsyApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.filename(".env")  // 기본값은 .env, 생략 가능
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		System.out.println("REDIS HOST = " + dotenv.get("REDIS_HOST"));
		SpringApplication.run(MsyApplication.class, args);
	}

}
