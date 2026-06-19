package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
    private String email;
    private Long photoId;
    private String firstName;
    private String lastName;
    private String phone;

}
