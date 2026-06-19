package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoachDto {
    private String name;
    private User user;
    private String specialization;
    private Integer experience;
    private String email;
    private String description;
    private String biography;
}
