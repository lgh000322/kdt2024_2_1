package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UserJoinDto;
import org.example.server.dto.user_dto.UserLoginDto;
import org.example.server.service.UserService;

import java.sql.SQLException;


public class UserController implements Controller {
    private static UserController userController = null;
    private final UserService userService;

    //간단한 캐시 처리를 위한 threadLocal 변수
    private ThreadLocal<User> threadLocalUser = new ThreadLocal<>();

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
        ResponseData result = null;
        Gson gson = new Gson();

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_JOIN -> {
                System.out.println("회원가입 실행");
                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    UserJoinDto userJoinDto = gson.fromJson(gson.toJson(map), UserJoinDto.class);
                    result = userService.join(userJoinDto);
                }
            }
            case MessageTypeConst.MESSAGE_LOGIN -> {
                System.out.println("로그인 실행");
                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    UserLoginDto userLoginDto = gson.fromJson(gson.toJson(map), UserLoginDto.class);
                    result = userService.login(userLoginDto);
                }
            }
            case MessageTypeConst.MESSAGE_LOGOUT -> {
                System.out.println("로그아웃 실행");
                threadLocalUser.remove();
            }
            case MessageTypeConst.MESSAGE_SEARCH -> {
                System.out.println("특정 회원 조회");
                User data = (User) requestData.getData();
                userService.findByUserId(data, threadLocalUser);
            }
            case MessageTypeConst.MESSAGE_SEARCH_ALL -> {
                System.out.println("모든 회원 조회");
                userService.findAll(null);
            }
        }

        return result;
    }
}