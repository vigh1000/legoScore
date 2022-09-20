package vg.legoScore;

import org.springframework.web.client.RestTemplate;

public abstract class RebrickableWebService {
    String key="d4f0a3eaa0fc59ffc6f425289e8640c2";
    RebrickableWebService(){

    }
    public abstract RebrickableWebService callRebrickable(String input, RestTemplate restTemplate);
}
