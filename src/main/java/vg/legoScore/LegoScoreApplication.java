package vg.legoScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;
import vg.legoScore.rebrickableObjects.Set;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.*;

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
//			firstTry(restTemplate);

			RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
			PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
			log.info(allPartCategories.toString());
			log.info(allPartCategories.getPartCategoriesAsMap().toString());

			Scanner scanner = new Scanner(System.in);
			while (true) {
				String input;
				System.out.println("Enter set number: ");
				input = scanner.next();
				CompleteSet completeSet = new CompleteSet(input, restTemplate);
				log.info(completeSet.setDetails.toString());
				log.info("Total quantity from PartList: " + String.valueOf(completeSet.getTotalPartsQuantity()));
				log.info("Total quantity from Set Details: " + completeSet.setDetails.getNum_parts().toString());
				log.info("Ratio unique parts to total parts: " + String.valueOf(completeSet.getRatioUniquePartsToTotalParts()));
				log.info("Number of colors in this set: " + String.valueOf(completeSet.setPartsPerColorMap.size()));

				HashMap<Long, Integer> partsPerCategory = new HashMap<>();
				partsPerCategory.putAll(completeSet.getPartsPerCategoryMap());
				for (Map.Entry<Long, Integer> entry : partsPerCategory.entrySet()) {
					log.info("'" + allPartCategories.getPartCategoriesAsMap().get(entry.getKey()) + "': " + entry.getValue());
				}

				log.info("Done");
			}
		};
	}

	private static void firstTry(RestTemplate restTemplate) {
		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
		Scanner scanner = new Scanner(System.in);
		String input;

		while (true) {
			switch (chooseOptionFromMenu()) {
				case 1:
					System.out.println("Enter part number: ");
					input = scanner.next();
					Part part = webServiceObject.callRebrickablePart(input);
					log.info(part.toString());
					break;
				case 2:
					System.out.println("Enter set number for set details: ");
					input = scanner.next();
					Set set = webServiceObject.callRebrickableSet(input);
					log.info(set.toString());
					break;
				case 3:
					System.out.println("Enter set number for part list: ");
					input = scanner.next();
					SetPartList setPartList = webServiceObject.callRebrickableSetParts(input);

					List<Results> results = new ArrayList<Results>();
					results.addAll(setPartList.getResults());
					log.info(setPartList.toString());
					while (setPartList.getNext() != null) {
						setPartList = webServiceObject.callNextSetParts(setPartList.getNext());
						results.addAll(setPartList.getResults());
						log.info(setPartList.toString());
					}

					List<Part> parts = new ArrayList<Part>();
					int totalParts = 0;
					HashMap<Part,Long> partHashMap = new HashMap<Part, Long>();
					for (Results result : results) {
						parts.add(result.getPart());
						totalParts += result.getQuantity();
						partHashMap.put(result.getPart(),Long.valueOf(String.valueOf(result.getQuantity())));
					}
					log.info(parts.get(3).getName());
					log.info(String.valueOf(totalParts));
					break;
				case 4:
					System.out.println("Enter Part Category: ");
					input = scanner.next();
					PartCategory partCategory = webServiceObject.callRebrickablePartCategory(input);
					log.info(partCategory.toString());
					break;
				case 5:
					System.out.println("Enter Color id: ");
					input = scanner.next();
					Color color = webServiceObject.callRebrickableColor(input);
					log.info(color.toString());
				case 0:
					return;
			}
		}
	}

	private static int chooseOptionFromMenu() {
		System.out.println("Choose Option: ");
		System.out.println("1. Part");
		System.out.println("2. Set details");
		System.out.println("3. Set part list");
		System.out.println("4. PartCategory");
		System.out.println("5. Color");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextInt();
	}
}
