package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coach {
    private Integer id;
    private User user;
    private int experience;
    private String description;
    private String specialty;
    private String biography;
    private double rating;
}
