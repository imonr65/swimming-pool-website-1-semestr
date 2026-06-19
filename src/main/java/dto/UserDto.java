package dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

}