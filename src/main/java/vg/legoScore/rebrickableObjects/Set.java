package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Set {

    private String name;
    private String set_num;
    private Long num_parts;

    public Set() {
    }

    public String getSet_num() {return this.set_num;}
    public void setSet_num(String set_num) {this.set_num=set_num;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public Long getNum_parts() {return num_parts;}
    public void setNum_parts(Long num_parts) {this.num_parts = num_parts;}

//    @Override
//    public String toString() {
//        ObjectMapper mapper = new ObjectMapper();
//        String json = null;
//        try {
//            json = mapper.writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return json;
//    }

//    @Override
//    public String toString() {
//        return "{" +
//                "set_num='" + set_num + '\'' +
//                ", name='" + name + '\'' +
//                ", num_parts=" + num_parts +
//                '}';
//    }

    @Override
    public String toString() {
        return name;
    }
}
