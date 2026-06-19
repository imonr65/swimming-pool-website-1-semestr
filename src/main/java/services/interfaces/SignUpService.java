package services.interfaces;


import dto.UserDto;

import java.sql.SQLException;

public interface SignUpService {

    void signUp(UserDto form) throws SQLException;
}
