package org.example.server.controller;

import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.AdminWorkData;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.UserWorkData;
import org.example.server.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

        System.out.println("출근 Response Message: " + responseData.getMessageType());
        System.out.println("출근 Response Data: " + responseData.getData());
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
        System.out.println("퇴근 Response Message: " + responseData.getMessageType());
        System.out.println("퇴근 Response Data: " + responseData.getData());
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
        // responseData.getData()가 UserWorkData 리스트라면 다음과 같이 출력할 수 있습니다.
        List<UserWorkData> workDataList = (List<UserWorkData>) responseData.getData();

        // 결과 출력 및 검증
        System.out.println("일반직원 근퇴 조회 Response Message: " + responseData.getMessageType());
        // 리스트를 순회하며 각 UserWorkData 객체의 값을 출력
        for (UserWorkData workData : workDataList) {
            System.out.println("Response Message (일반직원 근퇴 조회):  " + workData);  // toString을 명시적으로 호출
        }
    }

    @Test
    void 관리자_근퇴내역_조회_테스트() throws SQLException {
        // 테스트용 관리자 객체 생성
        User adminUser = new User.Builder()
                .userId("임관리")
                .password("admin")
                .name("admin")
                .tel("000-000-0000")
                .email("admin@example.com")
                .role(Role.ADMIN)
                .remainedLeave(10)
                .positionNum(6L)
                .deptNum(1L)
                .build();

        // RequestData 생성
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_WORK_SEARCH);

        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("userId", adminUser.getUserId());  // 관리자 ID
        data.put("name", adminUser.getName());      // 관리자 이름
        data.put("role", adminUser.getRole().name());  // 관리자의 역할

        requestData.setData(data);

        // execute 메서드 실행 (관리자 근퇴 내역 조회)
        ResponseData responseData = workController.execute(requestData);

        // responseData.getData()가 UserWorkData 리스트라면 다음과 같이 출력할 수 있습니다.
        List<AdminWorkData> workDataList = (List<AdminWorkData>) responseData.getData();

        // 결과 출력 및 검증
        System.out.println("Response Message (관리자 직원들 조회): " + responseData.getMessageType());
        // 리스트를 순회하며 각 UserWorkData 객체의 값을 출력
        for (AdminWorkData workData : workDataList) {
            System.out.println("Response Message (관리자 직원들 조회):  " + workData.toString());  // toString을 명시적으로 호출
        }
    }
}