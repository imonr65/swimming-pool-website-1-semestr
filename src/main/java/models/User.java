package models;


import lombok.*;

import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private UUID id;
    private Long photoId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Role role;
}
