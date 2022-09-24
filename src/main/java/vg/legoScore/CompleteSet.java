package vg.legoScore;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.Part;
import vg.legoScore.rebrickableObjects.Results;
import vg.legoScore.rebrickableObjects.Set;
import vg.legoScore.rebrickableObjects.SetParts;
import vg.legoScore.webservices.RebrickableWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class CompleteSet {
    private RestTemplate restTemplate;
    private SetParts parts;

    private String legoSetNr;
    private String rebrickableSetNr;
    private int totalPartsQuantity;

    public Set setDetails;
    public HashMap<Part,Long> partQuantityMap = new HashMap<Part, Long>();

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
        parts = webServiceObject.callRebrickableSetParts(this.rebrickableSetNr);

        setPartQuantityMap(webServiceObject);
    }

    private void setPartQuantityMap(RebrickableWebService webServiceObject) {
        List<Results> results = new ArrayList<Results>();
        results.addAll(parts.getResults());
        while (parts.getNext() != null) {
            parts = webServiceObject.callNextSetParts(parts.getNext());
            results.addAll(parts.getResults());
        }

        List<Part> parts = new ArrayList<Part>();
        int totalParts = 0;
        for (Results result : results) {
            parts.add(result.getPart());
            totalParts += result.getQuantity();
            partQuantityMap.put(result.getPart(),Long.valueOf(String.valueOf(result.getQuantity())));
        }
        setTotalPartsQuantity(totalParts);
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
}
