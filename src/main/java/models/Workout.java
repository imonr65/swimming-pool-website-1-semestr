package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    private Integer id;
    private String description;

    private PeopleType peopleType;
    private Category category;
    private Level level;
    private boolean hasCoach;
}
