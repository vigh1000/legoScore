package vg.legoScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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
		String input = scanner.nextLine();
		return args -> {
			RebrickableWebService partCategory = new PartCategory().callRebrickable(input,restTemplate);
			log.info(partCategory.toString());
			log.info("Gewünschtes Set war: " + input);
		};
	}
}
