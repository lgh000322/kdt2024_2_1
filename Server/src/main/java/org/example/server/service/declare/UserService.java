package org.example.server.service.declare;

import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UpdateUserDto;
import org.example.server.dto.user_dto.UserIdAndRole;
import org.example.server.dto.user_dto.UserJoinDto;
import org.example.server.dto.user_dto.UserLoginDto;

import java.sql.SQLException;

public interface UserService {
    ResponseData join(UserJoinDto user) throws SQLException;

    ResponseData login(UserLoginDto user) throws SQLException;

    ResponseData findByUserName(String userName) throws SQLException;

    ResponseData findByUserId(UserIdAndRole userIdAndRole) throws SQLException;

    ResponseData idValidation(String userId) throws SQLException;

    ResponseData findAll() throws SQLException;

    ResponseData findAllByAdmin() throws SQLException;

    ResponseData findUsernameAndEmailAll() throws SQLException;

    ResponseData updateUser(UpdateUserDto updateUserDto) throws SQLException;


}
