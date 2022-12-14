package vg.legoScore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class LegoScoreApplication {

	private static final Logger log = LoggerFactory.getLogger(LegoScoreApplication.class);

	private static final String NEXT_LINE = "..nextLine..";

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
	public String set2(@RequestParam(value = "setNr", defaultValue = "71761") String setNr, @RequestParam(value= "key", required = false) String key, RestTemplate restTemplate) {
		RebrickableWebService webServiceObject;
		if (key==null) {
			webServiceObject = new RebrickableWebService(restTemplate);
		} else {
			webServiceObject = new RebrickableWebService(restTemplate, key);
		}
		PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
		String returnString = allPartCategories.toString();
		return returnString;
	}

	@GetMapping("/cat")
	public String cat(RestTemplate restTemplate) {
		String returnString = restTemplate.getForObject(
				"https://catfact.ninja/fact", String.class);
		return returnString;
	}

	@GetMapping("/userSetList")
	public String userSetList(@RequestParam(value= "userToken") String userToken, @RequestParam(value = "listID") String listID, RestTemplate restTemplate) throws JsonProcessingException {
		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
		List<String> setNumbers = getSetNumbersFromUserList(listID, userToken, webServiceObject);

		List<CompleteSet> completeSetList = getCompleteSetsList(webServiceObject, setNumbers);

		ArrayList<String> returnList = new ArrayList<>();
		for (CompleteSet completeSet : completeSetList) {
			returnList.add(completeSet.getSetDetails().toString() + NEXT_LINE);
			returnList.add("----------------------------------------" + NEXT_LINE);
			returnList.add("SpaaaackScore: " + String.format("%.2f",completeSet.getTotalLegoScore()) + NEXT_LINE);
			returnList.add("SpaaaackScore2: " + String.format("%.2f",completeSet.getTotalLegoScore2()) + NEXT_LINE);
			returnList.add("SpaaaackScore3: " + String.format("%.2f",completeSet.getTotalLegoScore3()) + NEXT_LINE);
			returnList.add("Ratio unique parts to total parts: " + String.format("%.2f", completeSet.getRatioUniquePartsToTotalParts()) + NEXT_LINE);
			returnList.add("Total quantity including spare parts: " + String.valueOf(completeSet.getTotalPartsQuantity()) + NEXT_LINE);
			returnList.add("Number of colors in this set: " + String.valueOf(completeSet.getPartsPerColorMapSize()) + NEXT_LINE);
			returnList.add("Number of different part categories in this set: " + String.valueOf(completeSet.getPartsPerCategoryMapSize()) + NEXT_LINE);
			returnList.add("----------------------------------------" + NEXT_LINE);
		}
		returnList.add("Done");

		return returnList.toString().replaceAll(NEXT_LINE + ", ","<br />");

		//return String.valueOf(completeSetList.size());
	}

	@GetMapping("/userSetListJSON")
	public String userSetListJSON(@RequestParam(value= "userToken") String userToken, @RequestParam(value = "listID") String listID, RestTemplate restTemplate) throws JsonProcessingException {
		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
		List<String> setNumbers = getSetNumbersFromUserList(listID, userToken, webServiceObject);

		List<CompleteSet> completeSetList = getCompleteSetsList(webServiceObject, setNumbers);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(completeSetList);
		return json;

	}

	@GetMapping("/userPartList")
	public String userPartList(@RequestParam(value= "userToken") String userToken, @RequestParam(value = "listID") String listID, RestTemplate restTemplate) throws JsonProcessingException {
		if (listID == null) return "Keine SetNummer angegeben";
		if (userToken == null) return "Kein UserToken angegeben";

		RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
		CompleteSet completeSet = new CompleteSet(webServiceObject, listID, userToken);

		ArrayList<String> returnList = new ArrayList<>();
		//returnList.add(completeSet.getSetDetails().toString() + NEXT_LINE);
		//TODO: PartListDetails noch hinzu. Eigene Klasse weil doch nicht kompatibel mit Set?!?
		returnList.addAll(fillReturnList(completeSet, webServiceObject));
		return returnList.toString().replaceAll(NEXT_LINE + ", ","<br />");
	}

	@GetMapping("/setJSON")
	public String setJSON(@RequestParam(value = "setNr") String setNr, @RequestParam(value = "key", required = false) String key, RestTemplate restTemplate) throws JsonProcessingException {
		if (setNr == null) return "Keine SetNummer angegeben";

		RebrickableWebService webServiceObject;
		if (key==null) {
			webServiceObject = new RebrickableWebService(restTemplate);
		} else {
			webServiceObject = new RebrickableWebService(restTemplate, key);
		}

		String input = setNr;
		if (input.length() == 5) input = input + "-1";
		if (input.length() == 4) input = input + "-1";

		CompleteSet completeSet = new CompleteSet(input, webServiceObject);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(completeSet);
		return json;
	}

	@GetMapping("/set")
	public String set(@RequestParam(value = "setNr") String setNr, @RequestParam(value = "key", required = false) String key, RestTemplate restTemplate) {
		if (setNr == null) return "Keine SetNummer angegeben";

		RebrickableWebService webServiceObject;
		if (key==null) {
			webServiceObject = new RebrickableWebService(restTemplate);
		} else {
			webServiceObject = new RebrickableWebService(restTemplate, key);
		}

		String input = setNr;
		if (input.length() == 5) input = input + "-1";
		if (input.length() == 4) input = input + "-1";

		CompleteSet completeSet = new CompleteSet(input, webServiceObject);

		ArrayList<String> returnList = new ArrayList<>();
		returnList.add(completeSet.getSetDetails().toString() + NEXT_LINE);
		returnList.addAll(fillReturnList(completeSet, webServiceObject));
		return returnList.toString().replaceAll(NEXT_LINE + ", ","<br />");
	}

	private List<String> fillReturnList(CompleteSet completeSet, RebrickableWebService webServiceObject) {
		PartCategories allPartCategories = webServiceObject.callRebrickablePartCategories();
		List<String> returnList = new ArrayList<>();
		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("SpaaaackScore: " + String.format("%.2f",completeSet.getTotalLegoScore()) + NEXT_LINE);
		returnList.add("SpaaaackScore2: " + String.format("%.2f",completeSet.getTotalLegoScore2()) + NEXT_LINE);
		returnList.add("SpaaaackScore3: " + String.format("%.2f",completeSet.getTotalLegoScore3()) + NEXT_LINE);
		returnList.add("Ratio unique parts to total parts: " + String.format("%.2f", completeSet.getRatioUniquePartsToTotalParts()) + NEXT_LINE);
		returnList.add("Total quantity including spare parts: " + String.valueOf(completeSet.getTotalPartsQuantity()) + NEXT_LINE);

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List parts per StudArea: " + getAmountForThisMap(completeSet.getPartsPerStudAreaMap().values()) +  NEXT_LINE);
		returnList.addAll(getPartsPerStudAreaSortedByValue(completeSet.getPartsPerStudAreaMap(), completeSet.getTotalPartsQuantity()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List parts per StudAreaCategory: " + getAmountForThisMap(completeSet.getPartsPerStudAreaCategoryMap().values()) + NEXT_LINE);
		returnList.addAll(getPartsPerStudAreaCategorySortedByKey(completeSet.getPartsPerStudAreaCategoryMap(), completeSet.getTotalPartsQuantity()));

		returnList.add("-----------------------------------------" + NEXT_LINE);
		returnList.add("Number of colors in this set: " + String.valueOf(completeSet.getPartsPerColorMapSize()) + NEXT_LINE);
		returnList.addAll(getPartsPerColorSortedByValue(completeSet.getPartsPerColorMap(), completeSet.getTotalPartsQuantity()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("Number of different part categories in this set: " + String.valueOf(completeSet.getPartsPerCategoryMapSize()) + NEXT_LINE);
		returnList.addAll(getPartsPerCategorySortedByValue(completeSet.getPartsPerCategoryMap(), allPartCategories, completeSet.getTotalPartsQuantity()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List parts with 3rd dimension in Name (no Bricks with Thickness 1 etc.): " + getAmountForThisMap(completeSet.getPartsWithThirdDimensionInName().values()) + NEXT_LINE);
		returnList.addAll(getListOfSomePartSetSortedByValue(completeSet.getPartsWithThirdDimensionInName()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List Bricks/Parts with Thickness 1: " + getAmountForThisMap(completeSet.getBricksOfAllCategoriesWithHeightOne().values()) + NEXT_LINE);
		returnList.addAll(getListOfSomePartSetSortedByValue(completeSet.getBricksOfAllCategoriesWithHeightOne()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List Brackets (only which are counted as two Parts): " + getAmountForThisMap(completeSet.getBracketsCountedAsTwoParts().values()) + NEXT_LINE);
		returnList.addAll(getListOfSomePartSetSortedByValue(completeSet.getBracketsCountedAsTwoParts()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("List unscored parts: " + getAmountForThisMap(completeSet.getUnscoredPartsMap().values()) + NEXT_LINE);
		returnList.addAll(getListOfSomePartSetSortedByValue(completeSet.getUnscoredPartsMap()));

		returnList.add("----------------------------------------" + NEXT_LINE);
		returnList.add("Done");

		return returnList;
	}

	private Integer getAmountForThisMap(Collection<Integer> valuesForThisMap) {
		return valuesForThisMap.stream()
				.mapToInt(Integer::intValue).sum();
	}

	private Collection<String> getPartsPerStudAreaCategorySortedByKey(HashMap<Integer, Integer> partsPerStudAreaCategoryMap, int totalParts) {
		return partsPerStudAreaCategoryMap.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue() + "......" + (int) (Float.valueOf(entry.getValue())/totalParts*100) + " %" + NEXT_LINE)
				.collect(Collectors.toList());
	}

	private static List<String> getPartsPerStudAreaSortedByValue(HashMap<String, Integer> partPerStudAreaMap, int totalParts) {
		return partPerStudAreaMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.map(entry -> entry.getKey() + ": " + entry.getValue() + "......." + (int) (Float.valueOf(entry.getValue())/totalParts*100) + " %" + NEXT_LINE)
				.collect(Collectors.toList());
	}

	private static List<String> getPartsPerColorSortedByValue(HashMap<Color, Integer> partsPerColorMap, int totalParts) {
		return partsPerColorMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.map(entry -> entry.getKey().getName() + ": " + entry.getValue() + "......." + (int) (Float.valueOf(entry.getValue())/totalParts*100) + " %" +NEXT_LINE)
				.collect(Collectors.toList());
	}

	private static List<String> getPartsPerCategorySortedByValue(HashMap<Long, Integer> partsPerCategoryMap, PartCategories allPartCategories, int totalParts) {
		return partsPerCategoryMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.map(entry -> allPartCategories.getPartCategoriesAsMap().get(entry.getKey()) + ": " + entry.getValue() + "......." + (int) (Float.valueOf(entry.getValue())/totalParts*100) + " %" + NEXT_LINE)
				.collect(Collectors.toList());
	}

	private static List<String> getListOfSomePartSetSortedByValue(Map<Part, Integer> partsQuantityMap) {
		return partsQuantityMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.map(entry -> entry.getKey().getName() + ": " + entry.getValue() + NEXT_LINE)
				.collect(Collectors.toList());
	}

	private static List<CompleteSet> getCompleteSetsList(RebrickableWebService webServiceObject, List<String> setNumbers) {
		List<CompleteSet> completeSetList = new ArrayList<>();
		for (String entry : setNumbers) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			CompleteSet completeSet = new CompleteSet(entry, webServiceObject);
			completeSetList.add(completeSet);
		}
		return completeSetList;
	}

	private static UserSetList getUserSetList(String userToken, String listID, RebrickableWebService webServiceObject) {
		UserSetList userSetList = webServiceObject.callRebrickableUserSetListViaListID(userToken, listID);
		return userSetList;
	}

	private static List<String> getSetNumbersFromUserList(String listID, String userToken, RebrickableWebService webServiceObject) {
		UserSetList userSetList = getUserSetList(userToken, listID, webServiceObject);
		return userSetList.getResults().stream()
				.map(entry -> entry.getSet().getSet_num())
				.collect(Collectors.toList());
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
