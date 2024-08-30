package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.UserService;

import java.sql.SQLException;


public class UserController implements Controller {
    private static UserController userController = null;
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }


    public static UserController createOrGetUserController() {
        if (userController == null) {
            userController = new UserController(UserService.createOrGetUserService());
            System.out.println("싱글톤 memberController 생성됨");
            return userController;
        }

        return userController;
    }

    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        User user = (User)requestData.getData();
        ResponseData result = null;

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_JOIN:
                System.out.println("회원가입 실행");
                result = userService.join(user);
                break;

            case MessageTypeConst.MESSAGE_LOGIN:
                System.out.println("로그인 실행");
                userService.login(user);
                break;

            case MessageTypeConst.MESSAGE_LOGOUT:
                System.out.println("로그아웃 실행");
                break;

            case MessageTypeConst.MESSAGE_SEARCH:
                System.out.println("특정 회원 조회");
                break;
        }

        return  result;
    }
}
