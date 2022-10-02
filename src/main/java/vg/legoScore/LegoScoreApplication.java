package vg.legoScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;
import vg.legoScore.rebrickableObjects.Set;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.*;

@SpringBootApplication
@RestController
public class LegoScoreApplication {

	private static final Logger log = LoggerFactory.getLogger(LegoScoreApplication.class);

	public static void main(String[] args) {SpringApplication.run(LegoScoreApplication.class, args);}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
////			firstTry(restTemplate);
//
//			RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
//			PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
//			log.info(allPartCategories.toString());
//			log.info(allPartCategories.getPartCategoriesAsMap().toString());
//
//			Scanner scanner = new Scanner(System.in);
//			while (true) {
//				String input;
//				System.out.println("Enter set number: ");
//				input = scanner.next();
//
//				if (input.length()==5) input = input + "-1";
//
//				CompleteSet completeSet = new CompleteSet(input, restTemplate);
//				log.info(completeSet.setDetails.toString());
//				log.info("Total quantity from PartList: " + String.valueOf(completeSet.getTotalPartsQuantity()));
//				log.info("Total quantity from Set Details: " + completeSet.setDetails.getNum_parts().toString());
//				log.info("Ratio unique parts to total parts: " + String.valueOf(completeSet.getRatioUniquePartsToTotalParts()));
//
//				log.info("-----------------------------------------");
//				log.info("Number of colors in this set: " + String.valueOf(completeSet.partsPerColorMap.size()));
//
//				for (Map.Entry<Color, Integer> colorEntry : completeSet.partsPerColorMap.entrySet()) {
//					log.info("'" + colorEntry.getKey().getName() + "': " + colorEntry.getValue());
//				}
//
//				log.info("----------------------------------------");
//				log.info("Number of different part categories in this set: " + String.valueOf(completeSet.partsPerCategoryMap.size()));
//
//				for (Map.Entry<Long, Integer> categoryEntry : completeSet.getPartsPerCategoryMap().entrySet()) {
//					log.info("'" + allPartCategories.getPartCategoriesAsMap().get(categoryEntry.getKey()) + "': " + categoryEntry.getValue());
//				}
//
//				log.info("----------------------------------------");
//				for (Map.Entry<Part, Integer> partEntry : completeSet.partListQuantityMap.entrySet()) {
//					log.info("'" + partEntry.getKey().getName() + "': " + partEntry.getValue());
//				}
//
//				log.info("----------------------------------------");
//				for (Map.Entry<String, Integer> scoreCatEntry : completeSet.getPartsPerStudAreaMap().entrySet()) {
//					log.info("'" + scoreCatEntry.getKey() + "': " + scoreCatEntry.getValue());
//				}
//
//				log.info("----------------------------------------");
//				for (Map.Entry<String, Integer> unscoredEntry : completeSet.getUnscoredPartsMap().entrySet()) {
//					log.info("'" + unscoredEntry.getKey() + "': " + unscoredEntry.getValue());
//				}
//
//				log.info("----------------------------------------");
//				log.info("Spaaaaaaaack Score for this Set: " + completeSet.getTotalLegoScore());
//
//				log.info("Done");
//			}
//		};
//	}


	@RequestMapping("/")
	String sayHello() {
		return "Hallo Spaaaaack";
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
	@GetMapping("/set2")
	public String set2(@RequestParam(value = "setNr", defaultValue = "71761") String setNr, @RequestParam(value= "key") String key,RestTemplate restTemplate) {
		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate, key);
		PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
		String returnString = allPartCategories.toString();
		return returnString;
	}

	@GetMapping("/cat")
	public String cat(RestTemplate restTemplate) {
		if (true) {
			return "Ja klar...";
		}
		String returnString = restTemplate.getForObject(
				"https://catfact.ninja/fact", String.class);
		return returnString;
	}

	@GetMapping("/set")
	public String set(@RequestParam(value = "setNr", defaultValue = "71761") String setNr, @RequestParam(value = "key") String key, RestTemplate restTemplate) {

		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate, key);
		PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
//			log.info(allPartCategories.toString());
//			log.info(allPartCategories.getPartCategoriesAsMap().toString());

		String input = setNr;
		if (input.length() == 5) input = input + "-1";

		CompleteSet completeSet = new CompleteSet(input, webServiceObject);
		return String.valueOf(completeSet.getTotalLegoScore());
////				log.info(completeSet.setDetails.toString());
////				log.info("Total quantity from PartList: " + String.valueOf(completeSet.getTotalPartsQuantity()));
////				log.info("Total quantity from Set Details: " + completeSet.setDetails.getNum_parts().toString());
////				log.info("Ratio unique parts to total parts: " + String.valueOf(completeSet.getRatioUniquePartsToTotalParts()));
//
////				log.info("-----------------------------------------");
////				log.info("Number of colors in this set: " + String.valueOf(completeSet.partsPerColorMap.size()));
//
//		for (Map.Entry<Color, Integer> colorEntry : completeSet.partsPerColorMap.entrySet()) {
////					log.info("'" + colorEntry.getKey().getName() + "': " + colorEntry.getValue());
//		}
//
////				log.info("----------------------------------------");
////				log.info("Number of different part categories in this set: " + String.valueOf(completeSet.partsPerCategoryMap.size()));
//
//		for (Map.Entry<Long, Integer> categoryEntry : completeSet.getPartsPerCategoryMap().entrySet()) {
////					log.info("'" + allPartCategories.getPartCategoriesAsMap().get(categoryEntry.getKey()) + "': " + categoryEntry.getValue());
//		}
//
////				log.info("----------------------------------------");
//		for (Map.Entry<Part, Integer> partEntry : completeSet.partListQuantityMap.entrySet()) {
////					log.info("'" + partEntry.getKey().getName() + "': " + partEntry.getValue());
//		}
//
////				log.info("----------------------------------------");
//		for (Map.Entry<String, Integer> scoreCatEntry : completeSet.getPartsPerStudAreaMap().entrySet()) {
////					log.info("'" + scoreCatEntry.getKey() + "': " + scoreCatEntry.getValue());
//		}
//
////				log.info("----------------------------------------");
//		for (Map.Entry<String, Integer> unscoredEntry : completeSet.getUnscoredPartsMap().entrySet()) {
////					log.info("'" + unscoredEntry.getKey() + "': " + unscoredEntry.getValue());
//		}
//
////				log.info("----------------------------------------");
////				log.info("Spaaaaaaaack Score for this Set: " + completeSet.getTotalLegoScore());
//
////				log.info("Done");
	}

	private static void firstTry(RestTemplate restTemplate) {
		String key = "1234";
		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate, key);
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
