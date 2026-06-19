package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Coach;
import models.Workout;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoachAssignmentDto {
    private Coach coach;
    private List<Workout> assignedWorkouts;
    private List<Workout> availableWorkouts;

}
