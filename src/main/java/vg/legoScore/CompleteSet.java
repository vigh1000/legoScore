package vg.legoScore;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompleteSet {
    private RestTemplate restTemplate;

    private String legoSetNr;
    private String rebrickableSetNr;
    private int totalPartsQuantity;
    private float ratioUniquePartsToTotalParts;

    public Set setDetails;
    public HashMap<Part, Integer> setPartListQuantityMap = new HashMap<>();
    public HashMap<Color, Integer> setPartsPerColorMap = new HashMap<>();
    public HashMap<String, Integer> setPartsPerCategoryMap = new HashMap<>();

    public CompleteSet(RestTemplate restTemplate) {

    }

    public CompleteSet(int legoSetNr, RestTemplate restTemplate) {
        setLegoSetNr(String.valueOf(legoSetNr));
        setRebrickableSetNrViaLegoSetNr(this.legoSetNr);
    }

    public CompleteSet(String rebrickableSetNr, RestTemplate restTemplate) {
        RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
        setRebrickableSetNr(rebrickableSetNr);
        setLegoSetNr(rebrickableSetNr.substring(0,5));
        setDetails = webServiceObject.callRebrickableSet(this.rebrickableSetNr);

        setSetPartListQuantityMap(webServiceObject);
        setRatioUniquePartsToTotalParts();
    }

    private void setSetPartListQuantityMap(RebrickableWebService webServiceObject) {
        SetPartList setPartList = webServiceObject.callRebrickableSetParts(this.rebrickableSetNr);
        while (true) {
            List<Results> results = new ArrayList<Results>();
            results.addAll(setPartList.getResults());
            for (Results result : results) {
                addToTotalPartsQuantity(result.getQuantity());
                setPartListQuantityMap.put(result.getPart(),result.getQuantity());
                setPartsPerColorMap.merge(result.getColor(), result.getQuantity(), Integer::sum);
                //TODO: Nicht nur die ID als String zur Map, sondern das ganze PartCategory Objekt
                //      Dafür müssen wir uns wohl erstmal alle Kategorien holen.
                setPartsPerCategoryMap.merge(result.getPart().getPart_cat_id(), result.getQuantity(), Integer::sum);
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
        this.ratioUniquePartsToTotalParts = setPartListQuantityMap.size() / (float)totalPartsQuantity;
    }
}
