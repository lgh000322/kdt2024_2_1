package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = UserController.createOrGetUserController();


    @Test
    public void 로그인테스트() throws Exception{
        //given
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_LOGIN);
        User user = new User.Builder()
                .userId("dlrudgns")
                .password("dlrudgns")
                .role(Role.USER)
                .build();
        requestData.setData(user);
        //when
        ResponseData result = userController.execute(requestData);
        //then
        System.out.println("메세지: "+result.getMessageType()+" 데이터: "+result.getData());
    }
}