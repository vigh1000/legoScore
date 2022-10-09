package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Color implements Comparable<Color> {

    private int id;
    private String name;
    private boolean is_trans;

    public Color() {
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public boolean isIs_trans() {return is_trans;}
    public void setIs_trans(boolean is_trans) {this.is_trans = is_trans;}

//    @Override
//    public String toString() {
//        return "\"Color\": {" +
//                "\"id\"=" + id +
//                ", name='" + name + '\'' +
//                ", is_trans=" + is_trans +
//                '}';
//    }

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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Color o) {
        return Integer.compare(id, o.getId());
    }

    @Override
    public boolean equals(Object color) {
        Color c = (Color) color;
        return id==c.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
