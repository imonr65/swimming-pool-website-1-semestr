package models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Role {
    private Integer id;
    private String name;
}
