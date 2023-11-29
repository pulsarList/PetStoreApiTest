package projectApi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PetData {

    private Long id;
    private Category category;
    private String name;
    private ArrayList<String> photoUrls;
    private ArrayList<Tag> tags;
    private String status;


    @NoArgsConstructor
    @AllArgsConstructor
    public @Data
    static class Category {
        public Integer id;
        public String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public @Data
    static class Tag {
        public Integer id;
        public String name;
    }
}
