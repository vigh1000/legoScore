package vg.legoScore;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class RebrickableWebService {
    private String key;
    private File keyFile = new File("C:\\temp\\RebrickableAPIkey.txt");
    RebrickableWebService() {
        setKey(keyFile);
    }
    protected String getKey() {
        return key;
    }
    public void setKey(File keyFile) {
        Scanner keyScanner;
        try {
            keyScanner = new Scanner(keyFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        key = keyScanner.nextLine();
    }

    public abstract RebrickableWebService callRebrickable(String input, RestTemplate restTemplate);
}
