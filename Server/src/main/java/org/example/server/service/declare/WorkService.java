package org.example.server.service.declare;


import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UserSearchDto;

import java.sql.Connection;
import java.sql.SQLException;

public interface WorkService {

    ResponseData SearchWork(UserSearchDto user) throws SQLException;

    ResponseData StartWork(User user) throws SQLException;

    ResponseData EndWork(User user) throws SQLException;

    ResponseData EarlyOutWork(User user) throws SQLException;
}
