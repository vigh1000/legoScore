package vg.legoScore;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteSet {
    private RestTemplate restTemplate;

    private String legoSetNr;
    private String rebrickableSetNr;
    private int totalPartsQuantity;
    private float ratioUniquePartsToTotalParts;
    private float totalLegoScore;

    public Set setDetails;
    public HashMap<Part, Integer> partListQuantityMap = new HashMap<>();
    public HashMap<Color, Integer> partsPerColorMap = new HashMap<>();
    public HashMap<String, Integer> partsPerCategoryMap = new HashMap<>();
    public HashMap<String, Integer> partsPerStudAreaMap = new HashMap<>();

    public CompleteSet(RebrickableWebService webServiceObject) {

    }

    public CompleteSet(int legoSetNr, RebrickableWebService webServiceObject) {
        setLegoSetNr(String.valueOf(legoSetNr));
        setRebrickableSetNrViaLegoSetNr(this.legoSetNr);
    }

    public CompleteSet(String rebrickableSetNr, RebrickableWebService webServiceObject) {
        setRebrickableSetNr(rebrickableSetNr);
        setLegoSetNr(rebrickableSetNr.substring(0,5));
        setDetails = webServiceObject.callRebrickableSet(this.rebrickableSetNr);

        setPartListQuantityMap(webServiceObject);
        setRatioUniquePartsToTotalParts();
        setTotalLegoScore();
    }

    private void setPartListQuantityMap(RebrickableWebService webServiceObject) {
        SetPartList setPartList = webServiceObject.callRebrickableSetParts(this.rebrickableSetNr);
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
    public void setTotalPartsQuantity(int totalPartsQuantity) { this.totalPartsQuantity = totalPartsQuantity;}
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
    public void setTotalLegoScore() {
        for (Map.Entry<Part, Integer> partEntry : partListQuantityMap.entrySet()) {
            String partName = partEntry.getKey().getName();
            if (partEntry.getKey().getPart_cat_id().equalsIgnoreCase("29")) {
                continue;
            }

            if (partName.contains(" x ")) {
                List<String> splitName = List.of(partName.split(" "));
                String partWidth = splitName.get(splitName.indexOf("x") - 1);
                String partLength = splitName.get(splitName.indexOf("x") + 1);

                float partScore = (getMeasurementAsFloat(partWidth) * getMeasurementAsFloat(partLength)) * 2;
                if (partScore != 0.0) {
                    partsPerStudAreaMap.merge(partWidth + " x " + partLength, partEntry.getValue(), Integer::sum);
                }
                //System.out.println(totalLegoScore + " " + " " + partScore + " " + partWidthInFloat + " " + partLengthInFloat + " " + partEntry.getValue() + " "+ partName);
                totalLegoScore += partScore * partEntry.getValue();
            }
        }
        totalLegoScore = totalLegoScore / totalPartsQuantity;
    }

    private Float getMeasurementAsFloat(String measurementAsString) {
        try {
            if (measurementAsString.contains("/")) {
                List<String> splitMeasurement = List.of(measurementAsString.split("/"));
                measurementAsString = String.valueOf(Float.parseFloat(splitMeasurement.get(0)) / Float.parseFloat(splitMeasurement.get(1)));
            }
            return Float.parseFloat(measurementAsString);
        } catch (Exception ex) {
            return 0.0f;
        }
    }

    public HashMap<Long, Integer> getPartsPerCategoryMap () {
        HashMap<Long, Integer> partsPerCategoryMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : this.partsPerCategoryMap.entrySet()) {
            partsPerCategoryMap.put(Long.valueOf(entry.getKey()), entry.getValue());
        }
        return partsPerCategoryMap;
    }

    public HashMap<String, Integer> getPartsPerStudAreaMap() {
        return partsPerStudAreaMap;
    }

    public HashMap<String, Integer> getUnscoredPartsMap() {
        HashMap<String, Integer> unscoredPartsMap = new HashMap<>();

        for (Map.Entry<Part, Integer> partEntry : partListQuantityMap.entrySet()) {
            String partName = partEntry.getKey().getName();

            if (!partName.contains(" x ")) {
                unscoredPartsMap.merge(partName, partEntry.getValue(), Integer::sum);
            }
        }
        return unscoredPartsMap;
    }
}
