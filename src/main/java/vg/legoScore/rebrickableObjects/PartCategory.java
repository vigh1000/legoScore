package vg.legoScore.rebrickableObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartCategory implements Comparable<PartCategory> {

    private Long id;
    private String name;
    private Long part_count;

    public PartCategory() {
    }

    public Long getId() {return this.id;}
    public void setId(Long id) {this.id=id;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public Long getPart_count() {return part_count;}

    public void setPart_count(Long part_count) {this.part_count = part_count;}

    @Override
    public String toString() {
        return "PartCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", part_count=" + part_count +
                '}';
    }

    @Override
    public int compareTo(@NotNull PartCategory o) {
        return id.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object partCategory) {
        PartCategory pc = (PartCategory) partCategory;
        return id.equals(pc.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
