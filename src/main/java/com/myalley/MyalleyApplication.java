package com.myalley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MyalleyApplication {

	static {System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true"); }


	public static void main(String[] args) {
		SpringApplication.run(MyalleyApplication.class, args);
	}

}
