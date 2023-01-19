package com.myalley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@SpringBootApplication
public class MyalleyApplication {

	static {System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true"); }

	public static void main(String[] args) {
		SpringApplication.run(MyalleyApplication.class, args);
	}

}
