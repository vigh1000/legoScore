package vg.legoScore;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;
import vg.legoScore.rebrickableObjects.Set;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.*;

import static vg.legoScore.util.LegoScoreUtil.*;

public class CompleteSet {
    private RestTemplate restTemplate;

    private String legoSetNr;
    private String rebrickableSetNr;
    private int totalPartsQuantity;
    private float ratioUniquePartsToTotalParts;
    private float totalLegoScore;
    private float totalLegoScore2;
    private float totalLegoScore3;

    private Set setDetails;
    private final HashMap<Part, Integer> partListQuantityMap = new HashMap<>();
    private final HashMap<Color, Integer> partsPerColorMap = new HashMap<>();
    private final HashMap<String, Integer> partsPerCategoryMap = new HashMap<>();
    private final HashMap<String, Integer> partsPerStudAreaMap = new HashMap<>();
    private final HashMap<Part, Integer> unscoredPartsMap = new HashMap<>();
    private Map<Part, Integer> partsWithThirdDimensionInName = new HashMap<>();

    // Product of StudArea for each Category
    private final List<Integer> studAreaCategoryList = List.of(2,5,10,30,9999999);
    // Value of Category for Score2
    private final List<Integer> studAreaValueList = List.of(1,3,8,18,40);
    private final HashMap<Integer, Integer> partsPerStudAreaCategoryMap = new HashMap<>();

    private final List<String> brickCategories = List.of("3", "5", "6", "7", "8", "11", "20", "37");
    private final HashMap<Part, Integer> bricksOfAllCategoriesWithHeightOne = new HashMap<>();
    private final HashMap<Part, Integer> bracketsCountedAsTwoParts = new HashMap<>();

    public CompleteSet(RebrickableWebService webServiceObject, String listID, String userToken) {
        setRebrickableSetNr(listID);
        setDetails = webServiceObject.callRebrickableUserPartListDetailsViaListID(userToken, listID);
        setPartMaps1(webServiceObject, userToken);
        finalizeInitialisation();
    }

    public CompleteSet(String rebrickableSetNr, RebrickableWebService webServiceObject) {
        setRebrickableSetNr(rebrickableSetNr);
        setLegoSetNr(rebrickableSetNr.substring(0,5));
        setDetails = webServiceObject.callRebrickableSet(this.rebrickableSetNr);
        setPartMaps1(webServiceObject);
        finalizeInitialisation();
    }

    private void finalizeInitialisation() {
        setPartMaps2();
        setRatioUniquePartsToTotalParts();
        setTotalLegoScore();
        setTotalLegoScore2();
        setTotalLegoScore3();
    }

    private void setPartMaps1(RebrickableWebService webServiceObject) {
        SetPartList setPartList = webServiceObject.callRebrickableSetParts(this.rebrickableSetNr);
        setPartMaps1(webServiceObject, setPartList);
    }

    private void setPartMaps1(RebrickableWebService webServiceObject, String userToken) {
        SetPartList setPartList = webServiceObject.callRebrickableUserPartListViaListID(userToken, this.rebrickableSetNr);
        setPartMaps1(webServiceObject, setPartList);
    }

    private void setPartMaps1(RebrickableWebService webServiceObject, SetPartList setPartList) {
        while (true) {
            List<Results> results = new ArrayList<Results>();
            results.addAll(setPartList.getResults());
            for (Results result : results) {
                addToTotalPartsQuantity(result.getQuantity());
                partListQuantityMap.put(result.getPart(),result.getQuantity());
                partsPerColorMap.merge(result.getColor(), result.getQuantity(), Integer::sum);
                partsPerCategoryMap.merge(result.getPart().getPart_cat_id(), result.getQuantity(), Integer::sum);
            }
            if (setPartList.getNext() != null) setPartList = webServiceObject.callNextSetParts(setPartList.getNext());
            else break;
        }
    }

    private void setPartMaps2() {
        for (Map.Entry<Part, Integer> partEntry : partListQuantityMap.entrySet()) {
            String partName = partEntry.getKey().getName();

            //TODO: Hier k??nnen dann die einzelnen Kategorien rausgefiltert und berechnet werden.
            //      Vielleicht eigene Maps je Kategorie und die dann berechnen im Score?
            if (partEntry.getKey().getPart_cat_id().equalsIgnoreCase("29")) {
                unscoredPartsMap.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
                continue;
            }
            if (!partName.contains(" x ")) {
                unscoredPartsMap.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
                continue;
            }

            if (brickCategories.contains(partEntry.getKey().getPart_cat_id()) && !partName.matches(".*( x .* x ).*")) {
                bricksOfAllCategoriesWithHeightOne.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
            } else if (partName.startsWith("Bracket") && partName.matches(".*( x .* x ).*") && partName.contains(" - ")) {
                bracketsCountedAsTwoParts.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
            } else if (partName.matches(".*( x .* x ).*")) {
                partsWithThirdDimensionInName.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
            }

            List<String> splitName = List.of(partName.split(" "));
            String partWidth = splitName.get(splitName.indexOf("x") - 1);
            String partLength = splitName.get(splitName.indexOf("x") + 1);

            float partScore = getPartScoreFromPartName(partName);
            if (partScore != 0.0) {
                partsPerStudAreaMap.merge(partWidth + " x " + partLength, partEntry.getValue(), Integer::sum);

                //Fill partsPerStudAreaCategoryMap
                for (int i = 0; i < studAreaCategoryList.size(); i++) {
                    if ((partScore / 2) <= studAreaCategoryList.get(i)) {
                        partsPerStudAreaCategoryMap.merge(i, partEntry.getValue(), Integer::sum);
                        break;
                    }
                }
            } else {
                unscoredPartsMap.merge(partEntry.getKey(), partEntry.getValue(), Integer::sum);
            }
        }
    }

    public String getLegoSetNr() {
        return legoSetNr;
    }
    public void setLegoSetNr(String legoSetNr) {
        this.legoSetNr = legoSetNr;
    }
    public String getRebrickableSetNr() {
        return rebrickableSetNr;
    }
    public void setRebrickableSetNr(String rebrickableSetNr) {
        this.rebrickableSetNr = rebrickableSetNr;
    }
    public void setRebrickableSetNrViaLegoSetNr(String legoSetNr) {
        this.rebrickableSetNr = legoSetNr + "-1";
    }

    public int getTotalPartsQuantity() { return totalPartsQuantity;}
    private void setTotalPartsQuantity(int totalPartsQuantity) { this.totalPartsQuantity = totalPartsQuantity;}
    private void addToTotalPartsQuantity(int quantityToAdd) {totalPartsQuantity += quantityToAdd;}

    public float getRatioUniquePartsToTotalParts() {
        return ratioUniquePartsToTotalParts;
    }
    private void setRatioUniquePartsToTotalParts() {
        this.ratioUniquePartsToTotalParts = partListQuantityMap.size() / (float)totalPartsQuantity;
    }

    public float getTotalLegoScore() {
        return totalLegoScore;
    }
    private void setTotalLegoScore() {
        totalLegoScore += getTwoDimensionScore(partsPerStudAreaMap);
        totalLegoScore += getThirdDimensionScore(partsWithThirdDimensionInName, bricksOfAllCategoriesWithHeightOne, bracketsCountedAsTwoParts);
        totalLegoScore = totalLegoScore / totalPartsQuantity;
    }

    public float getTotalLegoScore2() {
        return totalLegoScore2;
    }
    private void setTotalLegoScore2() {
        for (Map.Entry<Integer, Integer> studAreaCategoryEntry : partsPerStudAreaCategoryMap.entrySet()) {
            totalLegoScore2 += studAreaValueList.get(studAreaCategoryEntry.getKey()) * studAreaCategoryEntry.getValue();
        }
        totalLegoScore2 += getThirdDimensionScore(partsWithThirdDimensionInName, bricksOfAllCategoriesWithHeightOne, bracketsCountedAsTwoParts);
        totalLegoScore2 = totalLegoScore2 / totalPartsQuantity;
    }

    public float getTotalLegoScore3() {
        return totalLegoScore3;
    }
    private void setTotalLegoScore3() {
        totalLegoScore3 = partsPerStudAreaCategoryMap.get(0);
        totalLegoScore3 += partsPerStudAreaMap.entrySet().stream()
                .filter(entry -> !entry.getKey().matches("^(1 x 1|1 x 2|2 x 1)"))
                .map(entry -> getPartScoreFromPartName(entry.getKey()) * entry.getValue())
                .reduce(0f, Float::sum);
        totalLegoScore3 += getThirdDimensionScore(partsWithThirdDimensionInName, bricksOfAllCategoriesWithHeightOne, bracketsCountedAsTwoParts);
        totalLegoScore3 = totalLegoScore3 / totalPartsQuantity;
    }

    public HashMap<Long, Integer> getPartsPerCategoryMap () {
        HashMap<Long, Integer> partsPerCategoryMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : this.partsPerCategoryMap.entrySet()) {
            partsPerCategoryMap.put(Long.valueOf(entry.getKey()), entry.getValue());
        }
        return partsPerCategoryMap;
    }

    public List<Integer> getStudAreaCategoryList() {
        return studAreaCategoryList;
    }
    public List<Integer> getStudAreaValueList() {
        return studAreaValueList;
    }

    public HashMap<String, Integer> getPartsPerStudAreaMap() {
        return partsPerStudAreaMap;
    }

    public HashMap<Part, Integer> getUnscoredPartsMap() {
        return unscoredPartsMap;
    }

    public HashMap<Integer, Integer> getPartsPerStudAreaCategoryMap() {
        return partsPerStudAreaCategoryMap;
    }
    public int getPartsPerCategoryMapSize() {
        return partsPerCategoryMap.size();
    }

    public HashMap<Color, Integer> getPartsPerColorMap() {
        return partsPerColorMap;
    }
    public int getPartsPerColorMapSize() {
        return partsPerColorMap.size();
    }

    public Set getSetDetails() {
        return setDetails;
    }

    public Map<Part, Integer> getPartsWithThirdDimensionInName() {
        return partsWithThirdDimensionInName;
    }

    public List<String> getBrickCategories() {
        return brickCategories;
    }

    public HashMap<Part, Integer> getBricksOfAllCategoriesWithHeightOne() {
        return bricksOfAllCategoriesWithHeightOne;
    }

    public HashMap<Part, Integer> getBracketsCountedAsTwoParts() {
        return bracketsCountedAsTwoParts;
    }
}
