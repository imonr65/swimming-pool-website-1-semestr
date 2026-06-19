package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private Long photoId;
    private String name;
    private ProductCategory productCategory;
    private String description;
    private Integer price;
    private Boolean isAvailable;
}
