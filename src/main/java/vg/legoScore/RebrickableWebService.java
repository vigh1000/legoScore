package vg.legoScore;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RebrickableWebService {
    private String key;
    private RestTemplate restTemplate = null;
    private final File keyFile = new File("C:\\temp\\RebrickableAPIkey.txt");

    RebrickableWebService(RestTemplate restTemplate) {
        setKey(keyFile);
        setRestTemplate(restTemplate);
    }

    private String getKey() {return key;}
    private void setKey(File keyFile) {
        Scanner keyScanner;
        try {
            keyScanner = new Scanner(keyFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        key = keyScanner.nextLine();
    }

    public RestTemplate getRestTemplate() {return restTemplate;}
    public void setRestTemplate(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    public Part callRebrickablePart(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/parts/" + input + "/?key=" + getKey(), Part.class);
    }

    public PartList callRebrickablePartList(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/sets/" + input + "/parts/?key=" + getKey(), PartList.class);
    }

    public PartList callNextPartList(String nextUrl) {
        return restTemplate.getForObject(
                nextUrl, PartList.class);
    }

    public Set callRebrickableSet(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/sets/" + input + "/?key=" + getKey(), Set.class);
    }

    public PartCategory callRebrickablePartCategotry(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/part_categories/" + input + "/?key=" + getKey(), PartCategory.class);
    }
}
