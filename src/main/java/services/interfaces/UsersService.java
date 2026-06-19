package services.interfaces;

import dto.UserDto;
import dto.UserProfileDto;
import models.User;

import java.sql.SQLException;
import java.util.List;

public interface UsersService {
    List<User> getAllUsers() throws SQLException;

    void addUser(UserDto form) throws SQLException;

    User getUserByEmail(String email) throws SQLException;

    UserProfileDto getUserProfileByEmail(String email) throws SQLException;

    void updateUserProfile(UserProfileDto form) throws SQLException;

    void updateUserPhoto(String email, Long photoId) throws SQLException;

    void updateRole(String email, int roleId) throws SQLException;
}
