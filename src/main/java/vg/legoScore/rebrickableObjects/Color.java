package vg.legoScore.rebrickableObjects;

public class Color {

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

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", name=" + name +
                ", is_trans=" + is_trans+
                '}';
    }
}
