package dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PriceDto {
    private String name;
    private Integer categoryId;
    private Integer priceForOne;
    private String period;
    private Integer peopleTypeId;
}
