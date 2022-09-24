package vg.legoScore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartList {

    private Long count;
    private String next;
    private List<Results> results;

    public PartList() {
        results = new ArrayList<Results>();
    }

    public Long getCount() {return this.count;}
    public void setCount(Long count) {this.count=count;}

    public String getNext() {return this.next;}
    public void setNext(String name) {this.next = name;}

    public List<Results> getResults() {return results;}
    public void setResults(List<Results> results) {this.results = results;}

    public String toString() {
        return "PartList{" +
                "count='" + count + '\'' +
                ", next='" + next + '\'' +
                ", results=" + results.stream().count() +
                '}';
    }
}
