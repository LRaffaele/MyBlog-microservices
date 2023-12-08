package it.cmqconsulting.msrating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsRatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRatingApplication.class, args);
	}

}
