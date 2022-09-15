package vg.legoScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Part;
import java.util.Scanner;

@SpringBootApplication
public class LegoScoreApplication {

	private static final Logger log = LoggerFactory.getLogger(LegoScoreApplication.class);

	public static void main(String[] args) {SpringApplication.run(LegoScoreApplication.class, args);}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String inputSet = scanner.nextLine();
		return args -> {
			//PartCategory partCategory = restTemplate.getForObject(
			Set set = restTemplate.getForObject(
			//"https://rebrickable.com/api/v3/lego/part_categories/12/?key=d4f0a3eaa0fc59ffc6f425289e8640c2", PartCategory.class);
			"https://rebrickable.com/api/v3/lego/sets/" + inputSet + "/?key=d4f0a3eaa0fc59ffc6f425289e8640c2", Set.class);
			//log.info(partCategory.toString());
			log.info(set.toString());
		};
	}
}
