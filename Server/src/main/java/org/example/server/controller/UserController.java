package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.adapter.LocalDateTypeAdapter;
import org.example.server.adapter.LocalTimeTypeAdapter;
import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UpdateUserDto;
import org.example.server.dto.user_dto.UserIdAndRole;
import org.example.server.dto.user_dto.UserJoinDto;
import org.example.server.dto.user_dto.UserLoginDto;
import org.example.server.service.UserServiceImpl;
import org.example.server.service.declare.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;


public class UserController implements Controller {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_JOIN -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("회원가입 실행");
                    UserJoinDto userJoinDto = gson.fromJson(gson.toJson(map), UserJoinDto.class);
                    result = userService.join(userJoinDto);
                }
            }
            case MessageTypeConst.MESSAGE_LOGIN -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("로그인 실행");
                    UserLoginDto userLoginDto = gson.fromJson(gson.toJson(map), UserLoginDto.class);
                    result = userService.login(userLoginDto);
                }
            }
            case MessageTypeConst.MESSAGE_LOGOUT -> {
                System.out.println("로그아웃 실행");
            }
            case MessageTypeConst.MESSAGE_SEARCH -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("특정 회원 조회");
                    UserIdAndRole userIdAndRole = gson.fromJson(gson.toJson(map), UserIdAndRole.class);
                    result = userService.findByUserId(userIdAndRole);
                }
            }
            case MessageTypeConst.MESSAGE_SEARCH_ADMIN -> {
                if (requestData.getData() instanceof String userName) {
                    System.out.println("회원 이름으로 회원 정보 조회");
                    result = userService.findByUserName(userName);
                }
            }
            case MessageTypeConst.MESSAGE_SEARCH_ALL -> {
                System.out.println("모든 회원 조회");
                result = userService.findAll();
            }
            case MessageTypeConst.MESSAGE_UPDATE -> {
                System.out.println("특정 회원 정보 수정");
                UpdateUserDto updateUserDto = gson.fromJson(gson.toJson(requestData.getData()), UpdateUserDto.class);
                result = userService.updateUser(updateUserDto);
            }
            case MessageTypeConst.MESSAGE_SEARCH_ALL_BYADMIN -> {
                System.out.println("모든 회원 조회 (관리자)");
                result = userService.findAllByAdmin();
            }
            case MessageTypeConst.MESSAGE_USER_ID_VALIDATION -> {
                if (requestData.getData() instanceof String userId) {
                    System.out.println("아이디 중복 검사");
                    // 바로 String으로 캐스팅
                    result = userService.idValidation(userId);  // userId 유효성 검사
                }
            }
            case MessageTypeConst.MESSAGE_SEARCH_ALL_USERNAME_AND_EMAIL -> {
                System.out.println("모든 회원의 이름과 이메일 조회");
                result = userService.findUsernameAndEmailAll();  // userId 유효성 검사
            }
        }

        return result;
    }
}