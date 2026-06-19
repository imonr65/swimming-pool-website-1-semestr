package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDto {
    private String description;
    private int peopleTypeId;
    private int categoryId;
    private int levelId;
    private boolean hasCoach;
}
