package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.SalaryService;
import org.example.server.service.UserService;

import java.sql.SQLException;

public class SalaryController implements Controller {
    private static SalaryController salaryController = null;
    private final SalaryService salaryService;

    public static SalaryController createOrGetSalaryController() {
        if (salaryController == null) {
            salaryController = new SalaryController(SalaryService.createOrGetSalaryService());
            System.out.println("AnswerController 싱글톤 생성");
            return salaryController;
        }

        System.out.println("AnswerController 싱글톤 반환");
        return salaryController;
    }

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }


    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_SALARY_SEARCH -> {
                System.out.println("급여내역 조회 실행");
                User user = (User) requestData.getData(); // 현재 접속한 유저정보를 가져온다.
                result = salaryService.SearchSalary(user); // 급여내역 조회 결과를 result에 저장
            }

            case MessageTypeConst.MESSAGE_SALARY_ADD -> {
                System.out.println("급여내역 추가 실행");
                User user = (User) requestData.getData(); // 현재 접속한 유저정보를 가져옴. 관리자 될것임.
                if (user.getRole() == Role.ADMIN) { // 관리자만이 등록할 수 있게 (월급을 주는 주체는 관리자니까)
                    result = salaryService.AddSalary(user); // 유저의 월급을 등록
                } else { //관리자가 아닐경우
                    System.out.println("관리자만 급여 내역을 추가할 수 있습니다.");
                }
            }

        }
        return result;
    }
}
