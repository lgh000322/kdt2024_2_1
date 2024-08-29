package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.service.UserService;


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
    public <T> T execute(RequestData requestData) {
        return null;
    }
}
