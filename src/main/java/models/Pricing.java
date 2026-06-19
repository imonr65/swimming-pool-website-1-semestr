package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pricing {
    private Integer id;
    private String name;
    private Category category;
    private Integer priceForOne;
    private String period;
    private PeopleType peopleType;
}
