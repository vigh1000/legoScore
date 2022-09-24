package vg.legoScore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Set {

    private String set_num;
    private String name;
    private Long num_parts;

    public Set() {
    }

    public String getSet_num() {return this.set_num;}
    public void setSet_num(String set_num) {this.set_num=set_num;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public Long getNum_parts() {return num_parts;}
    public void setNum_parts(Long num_parts) {this.num_parts = num_parts;}

    @Override
    public String toString() {
        return "Set{" +
                "set_num='" + set_num + '\'' +
                ", name='" + name + '\'' +
                ", num_parts=" + num_parts +
                '}';
    }
}
