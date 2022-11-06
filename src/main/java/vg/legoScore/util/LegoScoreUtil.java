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

    public static Float getMeasurementAsFloat(String measurementAsString) {
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

    public static float getTwoDimensionScore(Map<String, Integer> partsPerStudAreaMap) {
        float twoDimensionScore = 0.0f;
        for (Map.Entry<String, Integer> partEntry : partsPerStudAreaMap.entrySet()) {
                float partScore = getPartScoreFromPartName(partEntry.getKey());
            twoDimensionScore += partScore * partEntry.getValue();
        }
        return twoDimensionScore;
    }

    public static float getThirdDimensionScore(Map<Part, Integer> partsWithPotentialThirdDimension) {
        float thirdDimensionScore = 0.0f;

        //TODO: Brackets: Bracket 1 x 2 - 2 x 4

        //TODO: Bricks:
        //      Brick 2x2 => Normale Dicke, also 3 Plates  => Arghhhh, normale Bricks tauchen in dieser Liste garnicht auf... (Bricks, Bricks Arched, Bricks Sloped....)
        //                                                          => na dann eigene Liste für Bricks, wo wir die Bricks aller Kategorien reinpacken
        //      Brick 2x2x3 => dreifache Dicke, also 9 Plates


        return thirdDimensionScore;
    }
}
