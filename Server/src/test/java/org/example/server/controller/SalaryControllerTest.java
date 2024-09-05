package org.example.server.controller;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.SalaryAddData;
import org.example.server.service.SalaryService;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.Optional;

class SalaryControllerTest {
    SalaryService service = SalaryService.createOrGetSalaryService();

    //해당 유저에 대한 월급내역을 가져오는것을 확인할 수 있음.
    /*@Test
    void 조회() throws SQLException {
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

        //  접속한 유저의 월급 내역 조회
        // searchSalary 메서드를 호출하여 ResponseData를 받음
        //ResponseData responseData = service.searchSalary(user);


        //   관리자가 특정 유저에게 월급내역을 추가(월급주는것)
        ResponseData responseData = service.addSalary(user);

        // 결과를 출력
        System.out.println("Message: " + responseData.getMessageType());
        System.out.println("Data: " + responseData.getData());
    }*/


    @Test
    void 관리자_급여추가_테스트() throws SQLException {
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

        // SalaryAddData 객체 생성 (관리자와 직원 정보 함께 전달)
        SalaryAddData salaryAddData = new SalaryAddData(adminUser, normalUser);

        // 관리자가 급여를 추가하는 로직 테스트
        ResponseData result = service.addSalary(salaryAddData.getNormalUser());

        // 결과를 출력
        System.out.println("Admin Salary ADD Message: " + result.getMessageType());
        System.out.println("Admin Salary ADD Data: " + result.getData());

    }

    @Test
    void 비관리자가_급여추가_테스트() throws SQLException {
        // 일반 유저 정보 설정 (오자차)
        User normalUser = new User.Builder()
                .userId("ojacha")
                .password("pass345")
                .name("오자차")
                .role(Role.USER)  // Role.USER
                .userNum(5L)  // user_num 5
                .build();

        // 일반 유저가 급여를 추가하려는 경우
        SalaryAddData salaryAddData = new SalaryAddData(normalUser, normalUser); // 일반 유저가 자신에게 급여 추가 시도

        ResponseData result;
        if (salaryAddData.getAdminUser().getRole() == Role.ADMIN) {
            result = service.addSalary(salaryAddData.getNormalUser());
        } else {
            // 관리자가 아닌 경우 권한 없음 처리
            result = new ResponseData("권한 없음: 관리자만 급여 내역을 추가할 수 있습니다.", null);
        }

        // 결과를 출력
        System.out.println("not Admin Salary ADD Message: " + result.getMessageType());
        System.out.println("not Admin Salary ADD Data: " + result.getData());

    }

}