package vg.legoScore.webservices;

import org.springframework.web.client.RestTemplate;
import vg.legoScore.rebrickableObjects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RebrickableWebService {
    private String key;
    private RestTemplate restTemplate = null;
//    private String localAppdataFolder = System.getenv("LOCALAPPDATA");
//    private final File keyFile = new File(localAppdataFolder + "\\key.txt");

    public RebrickableWebService(RestTemplate restTemplate) {
        //setKey(keyFile);
        this.key = System.getenv("REBRICKABLEAPIKEY");
        setRestTemplate(restTemplate);
    }
    public RebrickableWebService(RestTemplate restTemplate, String key) {
        this.key=key;
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

    public SetPartList callRebrickableSetParts(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/sets/" + input + "/parts/?key=" + getKey(), SetPartList.class);
    }

    public SetPartList callNextSetParts(String nextUrl) {
        return restTemplate.getForObject(
                nextUrl, SetPartList.class);
    }

    public Set callRebrickableSet(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/sets/" + input + "/?key=" + getKey() + "&inc_color_details=0" + "&page_size=1000", Set.class);
    }

    public PartCategories callRebrickablePartCategories() {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/part_categories/?key=" + getKey(), PartCategories.class);
    }

    public PartCategory callRebrickablePartCategory(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/part_categories/" + input + "/?key=" + getKey(), PartCategory.class);
    }

    public Color callRebrickableColor(String input) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/colors/" + input + "/?key=" + getKey(), Color.class);
    }

    public UserSetList callRebrickableUserSetListViaListID(String userToken, String list_id) {
        System.out.println("https://rebrickable.com/api/v3/users/" + userToken + "/setlists/" + list_id + "/sets" + "/?key=" + getKey());
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/users/" + userToken + "/setlists/" + list_id + "/sets" + "/?key=" + getKey(), UserSetList.class);
    }
}
