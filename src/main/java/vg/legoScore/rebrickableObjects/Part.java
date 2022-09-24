package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Part {

    private String part_num;
    private String name;
    private String part_cat_id;

    public Part() {
    }

    public String getPart_num() {return this.part_num;}
    public void setPart_num(String part_num) {this.part_num=part_num;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getPart_cat_id() {return part_cat_id;}
    public void setPart_cat_id(String part_cat_id) {this.part_cat_id = part_cat_id;}

    @Override
    public String toString() {
        return "Part{" +
                "part_num='" + part_num + '\'' +
                ", name='" + name + '\'' +
                ", part_cat_id='" + part_cat_id + '\'' +
                '}';
    }
}
