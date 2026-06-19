package dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private Long categoryId;
    private Long photoId;
    private String description;
    private Integer price;
    private Boolean isAvailable;
}
