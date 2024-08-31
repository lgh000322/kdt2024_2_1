package org.example.server.controller;

import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.SalaryService;
import org.example.server.service.UserService;

import java.sql.SQLException;

public class SalaryController implements Controller {
    private static SalaryController salaryController = null;
    private final SalaryService salaryService;
    private final UserService userService;

    public static SalaryController createOrGetSalaryController() {
        if (salaryController == null) {
            salaryController = new SalaryController(SalaryService.createOrGetSalaryService(),UserService.createOrGetUserService());
            System.out.println("AnswerController 싱글톤 생성");
            return salaryController;
        }

        System.out.println("AnswerController 싱글톤 반환");
        return salaryController;
    }

    public SalaryController(SalaryService salaryService, UserService userservice) {
        this.salaryService = salaryService;
        this.userService = userservice;
    }



    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        if(requestData.getMessageType().contains("search")) {
            User user = (User)requestData.getData(); //현재 접속한 유저정보를 가져온다.
            salaryService.SearchSalary(user);
        }
        return null;
    }
}
