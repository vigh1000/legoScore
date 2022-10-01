package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartCategories {

    private Long count;
    private String next;
    private List<PartCategory> results;

    public PartCategories() {
        results = new ArrayList<PartCategory>();
    }

    public Long getCount() {return this.count;}
    public void setCount(Long count) {this.count=count;}

    public String getNext() {return this.next;}
    public void setNext(String name) {this.next = name;}

    public List<PartCategory> getResults() {return results;}
    public void setResults(List<PartCategory> results) {this.results = results;}

    @Override
    public String toString() {
        return "PartList{" +
                "count='" + count + '\'' +
                ", next='" + next + '\'' +
                ", results=" + results.stream().count() +
                //", allPartCategories=" + results +
                '}';
    }
}
