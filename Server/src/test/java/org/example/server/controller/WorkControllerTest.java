package org.example.server.controller;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.service.WorkService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class WorkControllerTest {
    WorkService workService = WorkService.createOrGetWorkService();

    @Test
    void 출근 () throws SQLException {
        // User 객체를 생성하여 테스트에 사용
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


        //근퇴기록 모두 조회
        // searchSalary 메서드를 호출하여 ResponseData를 받음
        //ResponseData responseData = workService.SearchWork(user);



        //출근해서 현재 시스템시간으로 출근시간 업데이트
        //startTime과 status와 workDate userNum만 객체를 생성하여 해당 정보만 업데이트 시켰기때문에 longnum과 퇴근시간은 null값으로 나올것임
        //DB에는 퇴근시간과 status가 제대로 조회되었음. 오늘날짜로 조회하기때문에 다른날에 값이 바뀔일 없음.
        //ResponseData responseData = workService.StartWork(user);

        //퇴근하여 현재 시스템시간으로 퇴근시간 업데이트
        ResponseData responseData = workService.EndWork(user);



        // 결과를 출력
        System.out.println("Work_Message: " + responseData.getMessageType());
        System.out.println("Work_Data: " + responseData.getData());

    }
}