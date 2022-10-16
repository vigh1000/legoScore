package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSetListResults {

    private int list_id;
    private int quantity;
    private boolean include_spares;
    private Set set;

    public UserSetListResults() {
    }

    public int getList_id() {return this.list_id;}
    public void setList_id(int list_id) {this.list_id = list_id;}

    public int getQuantity() {return this.quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public boolean getInclude_spares() {return include_spares;}
    public void setInclude_spares(boolean include_spares) {this.include_spares = include_spares;}

    public Set getSet() {return set;}
    public void setSet(Set set) {this.set = set;}

    @Override
    public String toString() {
        return "Results{" +
                "list_id=" + list_id +
                ", quantity=" + quantity +
                ", include_spares=" + include_spares +
                ", Set{" + set +
                '}';
    }
}
