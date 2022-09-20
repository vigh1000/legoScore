package vg.legoScore;

import org.springframework.web.client.RestTemplate;

public abstract class RebrickableWebService {
    public abstract RebrickableWebService callRebrickable(String input, RestTemplate restTemplate);
}
