package RatingService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class RatingServiceApplication {

	@Autowired
	MongoTemplate mongoTemplate;

	@PostConstruct
	public void proof() {
		System.out.println("ACTUAL DATABASE = " + mongoTemplate.getDb().getName());
	}

	public static void main(String[] args) {

		SpringApplication.run(RatingServiceApplication.class, args);
	}

}
