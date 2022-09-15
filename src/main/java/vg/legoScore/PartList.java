package vg.legoScore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartList {

    private Long count;
    private String next;

    private List<Part> results;

    public PartList() {
        results = new ArrayList<>();
    }

    public Long getCount() {return this.count;}
    public void setCount(Long count) {this.count=count;}

    public String getNext() {return this.next;}
    public void setNext(String name) {this.next = name;}

    public List<Part> getResults() {return results;}
    public void setResults(ArrayList results) {this.results = results;}

    @Override
    public String toString() {
        return "PartList{" +
                "count='" + count + '\'' +
                ", next='" + next + '\'' +
                ", results=" + results.stream().count() +
                '}';
    }
}
