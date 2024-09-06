package org.example.server.controller;

import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkControllerTest {
    WorkService workService;
    WorkController workController;

    @BeforeEach
    void setup() {
        workService = WorkService.createOrGetWorkService(); // WorkService 싱글톤 생성
        workController = WorkController.createOrGetWorkController(); // WorkController 싱글톤 생성
    }

    @Test
    void 출근_테스트() throws SQLException {
        // 테스트용 User 객체 생성
        User user = new User.Builder()
                .userId("김가나")
                .password("pass123")
                .name("kimgana")
                .tel("123-456-7890")
                .email("gana@example.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(2L)
                .deptNum(2L)
                .build();

        // RequestData 생성
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_WORK_START);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("userId", user.getUserId());
        data.put("name", user.getName());
        data.put("role", user.getRole().name());

        requestData.setData(data);

        // execute 메서드 실행
        ResponseData responseData = workController.execute(requestData);

        System.out.println("Response Message: " + responseData.getMessageType());
        System.out.println("Response Data: " + responseData.getData());
    }

    @Test
    void 퇴근_테스트() throws SQLException {
        // 테스트용 User 객체 생성
        User user = new User.Builder()
                .userId("김가나")
                .password("pass123")
                .name("kimgana")
                .tel("123-456-7890")
                .email("gana@example.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(2L)
                .deptNum(2L)
                .build();

        // RequestData 생성
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_WORK_FINISH);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("userId", user.getUserId());
        data.put("name", user.getName());
        data.put("role", user.getRole().name());

        requestData.setData(data);

        // execute 메서드 실행
        ResponseData responseData = workController.execute(requestData);

        // 결과 출력 및 검증
        System.out.println("Response Message: " + responseData.getMessageType());
        System.out.println("Response Data: " + responseData.getData());
    }
    @Test
    void 근퇴내역_조회_테스트() throws SQLException {
        // 테스트용 User 객체 생성
        User user = new User.Builder()
                .userId("김가나")
                .password("pass123")
                .name("kimgana")
                .tel("123-456-7890")
                .email("gana@example.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(2L)
                .deptNum(2L)
                .build();

        // RequestData 생성
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_WORK_SEARCH);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("userId", user.getUserId());
        data.put("name", user.getName());
        data.put("role", user.getRole().name());

        requestData.setData(data);

        // execute 메서드 실행
        ResponseData responseData = workController.execute(requestData);

        // 결과 출력 및 검증
        System.out.println("Response Message: " + responseData.getMessageType());
        System.out.println("Response Data: " + responseData.getData());
    }
}