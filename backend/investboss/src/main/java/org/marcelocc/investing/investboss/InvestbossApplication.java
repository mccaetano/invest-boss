package org.marcelocc.investing.investboss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJdbcRepositories
public class InvestbossApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestbossApplication.class, args);
	}

}
