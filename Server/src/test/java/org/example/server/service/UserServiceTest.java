package org.example.server.service;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = UserService.createOrGetUserService();

    @Test
    void join_test() throws SQLException {
        User user = new User.Builder()
                .userId("dlrudgns")
                .password("dlrudgns")
                .name("이경훈")
                .tel("010-1111-1111")
                .email("aaa@aaa.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(1L)
                .deptNum(1L)
                .build();

        ResponseData data = userService.join(user);

        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());
    }

    @Test
    void login_test() throws SQLException {
        User user = new User.Builder()
                .userId("dlrudgns")
                .password("dlrudgns")
                .role(Role.USER)
                .build();

        ResponseData data = userService.login(user);

        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());
    }

    @Test
    void findUserById() throws SQLException {
        User user = new User.Builder()
                .userId("dlrudgns")
                .password("dlrudgns")
                .role(Role.USER)
                .build();

        ResponseData data = userService.findByUserId(user, null);
        User findUserRoleUser = (User) data.getData();

        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + findUserRoleUser.getUserId());
    }

}