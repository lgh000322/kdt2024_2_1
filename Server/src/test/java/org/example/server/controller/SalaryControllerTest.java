package org.example.server.controller;

import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.SalaryAddData;
import org.example.server.service.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

class SalaryControllerTest {
    SalaryController salaryController;
    SalaryService salaryService;

    @BeforeEach
    void setup() {
        // SalaryService와 SalaryController의 싱글톤 인스턴스를 생성
        salaryService = SalaryService.createOrGetSalaryService();
        salaryController = SalaryController.createOrGetSalaryController();
    }

    @Test
    void 급여_내역_조회_테스트() throws SQLException {
        // 테스트용 User 객체 생성 (일반 유저)
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

        // 관리자(임관리)
        User adminUser = new User.Builder()
                .userId("임관리")
                .password("admin")
                .name("admin")
                .role(Role.ADMIN)  // Role.ADMIN
                .userNum(12L)  // user_num 12
                .build();

        // 일반 유저의 급여 내역 조회 요청
        RequestData userRequestData = new RequestData();
        userRequestData.setMessageType(MessageTypeConst.MESSAGE_SALARY_SEARCH);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정 (일반 유저)
        LinkedTreeMap<String, Object> userData = new LinkedTreeMap<>();
        userData.put("userId", user.getUserId());
        userData.put("name", user.getName());
        userData.put("role", user.getRole().name());

        userRequestData.setData(userData);

        // execute 메서드 실행 (일반 유저 급여 조회)
        ResponseData userResponseData = salaryController.execute(userRequestData);

        // 결과 검증 (일반 유저의 급여 내역 조회 결과)
        System.out.println("일반 유저 급여 조회 결과:");
        System.out.println("Message: " + userResponseData.getMessageType());
        System.out.println("Data: " + userResponseData.getData());


        // 관리자가 전체 급여 조회 요청
        RequestData adminRequestData = new RequestData();
        adminRequestData.setMessageType(MessageTypeConst.MESSAGE_SALARY_SEARCH);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정 (관리자)
        LinkedTreeMap<String, Object> adminData = new LinkedTreeMap<>();
        adminData.put("userId", adminUser.getUserId());  // 관리자 정보 추가
        adminData.put("role", adminUser.getRole().name());  // 관리자 Role 추가
        adminRequestData.setData(adminData);

        // execute 메서드 실행 (관리자 급여 조회)
        ResponseData adminResponseData = salaryController.execute(adminRequestData);

        // 결과 검증 (관리자의 급여 내역 조회 결과)
        System.out.println("관리자 급여 조회 결과:");
        System.out.println("Message: " + adminResponseData.getMessageType());
        System.out.println("Data: " + adminResponseData.getData());

    }

    @Test
    void 관리자_급여_추가_테스트() throws SQLException {
        // 관리자(임관리)
        User adminUser = new User.Builder()
                .userId("admin")
                .password("admin")
                .name("임관리")
                .role(Role.ADMIN)  // Role.ADMIN
                .userNum(12L)  // user_num 12
                .build();

        // 일반직원(오자차)
        User normalUser = new User.Builder()
                .userId("ojacha")
                .password("pass345")
                .name("오자차")
                .role(Role.USER)  // Role.USER
                .userNum(5L)  // user_num 5
                .build();

        // RequestData 생성 (MESSAGE_SALARY_ADD)
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_SALARY_ADD);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("normalUser", normalUser);
        data.put("admin", adminUser);

        requestData.setData(data);

        // execute 메서드 실행 (급여 추가)
        ResponseData responseData = salaryController.execute(requestData);

        // 결과 검증
        System.out.println("관리자 Message: " + responseData.getMessageType());
        System.out.println("관리자 Data: " + responseData.getData());
    }

    @Test
    void 비관리자_급여_추가_테스트() throws SQLException {
        // 일반 유저 정보 설정 (오자차)
        User normalUser = new User.Builder()
                .userId("ojacha")
                .password("pass345")
                .name("오자차")
                .role(Role.USER)  // Role.USER
                .userNum(5L)  // user_num 5
                .build();

        // RequestData 생성 (MESSAGE_SALARY_ADD)
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_SALARY_ADD);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("normalUser", normalUser);
        data.put("admin", normalUser);  // 일반 유저가 관리자 역할을 하려는 경우

        requestData.setData(data);

        // execute 메서드 실행 (비관리자가 급여 추가 시도)
        ResponseData responseData = salaryController.execute(requestData);

        // 결과 검증
        System.out.println("비관리자 비관리자 Message: " + responseData.getMessageType());
        System.out.println("비관리자 급여추가 Data: " + responseData.getData());
    }

}

