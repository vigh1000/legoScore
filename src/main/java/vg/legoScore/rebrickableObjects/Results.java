package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {

    private int id;
    private int quantity;
    private boolean is_spare;
    private int num_sets;
    private Part part;
    private Color color;

    public Results() {
    }

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public int getQuantity() {return this.quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public boolean getIs_spare() {return is_spare;}
    public void setIs_spare(boolean is_spare) {this.is_spare = is_spare;}

    public Part getPart() {return part;}
    public void setParts(Part part) {this.part = part;}

    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}

    public int getNum_sets() {return num_sets;}
    public void setNum_sets(int num_sets) {this.num_sets = num_sets;}

    @Override
    public String toString() {
        return "Results{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", is_spare=" + is_spare +
                ", num_sets=" + num_sets +
                '}';
    }
}
