package vg.legoScore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.RestTemplate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartCategory extends RebrickableWebService {

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
    public PartCategory callRebrickable(String input, RestTemplate restTemplate) {
        return restTemplate.getForObject(
                "https://rebrickable.com/api/v3/lego/part_categories/" + input + "/?key=" + key, PartCategory.class);
    }

    @Override
    public String toString() {
        return "PartCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", part_count=" + part_count +
                '}';
    }

}
