package vg.legoScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		return args -> {
			RebrickableWebService webServiceObject = null;
			Scanner scanner = new Scanner(System.in);
			String input;

			while (true) {
				switch (chooseOptionFromMenu()) {
					case 1:
						System.out.println("Enter part number: ");
						input = scanner.next();
						webServiceObject = new Part().callRebrickable(input, restTemplate);
						break;
					case 2:
						System.out.println("Enter set number for set details: ");
						input = scanner.next();
						webServiceObject = new Set().callRebrickable(input, restTemplate);
						break;
					case 3:
						System.out.println("Enter set number for part list: ");
						input = scanner.next();
						webServiceObject = new PartList().callRebrickable(input, restTemplate);

						List<Results> results = new ArrayList<Results>();
						results.addAll(((PartList) webServiceObject).getResults());

						while (((PartList) webServiceObject).getNext() != null) {
							webServiceObject = ((PartList) webServiceObject).callNext(((PartList) webServiceObject).getNext(), restTemplate);
							results.addAll(((PartList) webServiceObject).getResults());
						}

						List<Part> parts = new ArrayList<Part>();
						int totalParts = 0;
						for (Results result : results) {
							parts.add(result.getPart());
							totalParts += result.getQuantity();
						}
						log.info(parts.get(3).getName());
						log.info(String.valueOf(totalParts));
						break;
					case 4:
						System.out.println("Enter Part Category: ");
						input = scanner.next();
						webServiceObject = new PartCategory().callRebrickable(input, restTemplate);
						break;
					case 0:
						return;
				}
				log.info(webServiceObject.toString());
			}
		};
	}
	private static int chooseOptionFromMenu() {
		System.out.println("Choose Option: ");
		System.out.println("1. Part");
		System.out.println("2. Set details");
		System.out.println("3. Set part list");
		System.out.println("4. PartCategory");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextInt();
	}
}
