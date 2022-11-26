package vg.legoScore.util;

import vg.legoScore.rebrickableObjects.Part;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegoScoreUtil {
    private LegoScoreUtil(){
    }

    public static float getPartScoreFromPartName(String partName) {
        List<String> splitName = List.of(partName.split(" "));
        String partWidth = splitName.get(splitName.indexOf("x") - 1);
        String partLength = splitName.get(splitName.indexOf("x") + 1);

        return (getMeasurementAsFloat(partWidth) * getMeasurementAsFloat(partLength)) * 2;
    }

    public static float getMeasurementAsFloat(String measurementAsString) {
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

    private static String getPartThickness(String partName) {
        List<String> splittedName = List.of(partName.split(" "));
        String thirdDimension = splittedName.get(splittedName.lastIndexOf("x") + 1);
        return thirdDimension;
    }

    public static float getTwoDimensionScore(Map<String, Integer> partsPerStudAreaMap) {
        float twoDimensionScore = 0.0f;
        for (Map.Entry<String, Integer> partEntry : partsPerStudAreaMap.entrySet()) {
            float partScore = getPartScoreFromPartName(partEntry.getKey());
            twoDimensionScore += partScore * partEntry.getValue();
        }
        return twoDimensionScore;
    }

    public static float getThirdDimensionScore(Map<Part, Integer> partsWithPotentialThirdDimension, Map<Part, Integer> bricksWithHeightOne, Map<Part, Integer> bracketsCountedAsTwoParts) {
        float thirdDimensionScore = 0.0f;

        thirdDimensionScore += getScoreForBracketsCountedAsTwoParts(bracketsCountedAsTwoParts);
        thirdDimensionScore += getScoreForBricksWithHeightOne(bricksWithHeightOne);
        thirdDimensionScore += getScoreForPartsWithThirdDimension(partsWithPotentialThirdDimension);

        return thirdDimensionScore;
    }

    private static float getScoreForBracketsCountedAsTwoParts(Map<Part, Integer> bracketsCountedAsTwoParts) {
        //Zweites Mal getPartScoreFromPartName aufrufen mit gesplittetem Bracket Namen; Erster Teil schon in normalem Score berücksichtigt
        //Bracket 1 x 2 - 3 x 4
        float score = 0.0f;
        for (Map.Entry<Part, Integer> partIntegerEntry : bracketsCountedAsTwoParts.entrySet()) {
            List<String> splittedName = List.of(partIntegerEntry.getKey().getName().split(" "));
            String partWidthSecond = splittedName.get(splittedName.indexOf("-") + 1);
            String partLengthSecond = splittedName.get(splittedName.indexOf("-") + 3);
            score += getPartScoreFromPartName(partWidthSecond + " x " + partLengthSecond) * partIntegerEntry.getValue();
        }
        return score;
    }

    private static float getScoreForPartsWithThirdDimension(Map<Part, Integer> partsWithThirdDimensionInName) {
        float score = 0.0f;
        score = partsWithThirdDimensionInName.entrySet().stream()
                .map(entry -> getPartScoreFromPartName(entry.getKey().getName()) * 0.5f * getMeasurementAsFloat(getPartThickness(entry.getKey().getName())) * entry.getValue())
                .reduce(0.0f, Float::sum);
        return score;
    }

    private static float getScoreForBricksWithHeightOne(Map<Part, Integer> bricksWithHeightOne) {
        float score = 0.0f;
        score = bricksWithHeightOne.entrySet().stream()
                .map(entry -> getPartScoreFromPartName(entry.getKey().getName()) * 0.5f * entry.getValue())
                .reduce(0.0f, Float::sum);
        return score;
    }
}
