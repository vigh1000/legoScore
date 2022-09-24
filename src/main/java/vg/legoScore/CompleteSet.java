package vg.legoScore;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.Set;
import vg.legoScore.rebrickableObjects.SetParts;
import vg.legoScore.webservices.RebrickableWebService;

public class CompleteSet {
    private String legoSetNr;
    private String rebrickableSetNr;

    private RestTemplate restTemplate;

    public Set setDetails;
    public SetParts parts;

    public CompleteSet(RestTemplate restTemplate) {

    }

    public CompleteSet(int legoSetNr, RestTemplate restTemplate) {
        setLegoSetNr(String.valueOf(legoSetNr));
        setRebrickableSetNrViaLegoSetNr(this.legoSetNr);
    }

    public CompleteSet(String rebrickableSetNr, RestTemplate restTemplate) {
        RebrickableWebService webServiceObject = new RebrickableWebService(restTemplate);
        setRebrickableSetNr(rebrickableSetNr);
        setLegoSetNr(rebrickableSetNr.substring(0,6));
        setDetails = webServiceObject.callRebrickableSet(this.rebrickableSetNr);
        parts = webServiceObject.callRebrickableSetParts(this.rebrickableSetNr);
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
}
