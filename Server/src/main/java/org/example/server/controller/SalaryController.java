package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.SalaryService;

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
        Gson gson = new Gson();

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_SALARY_SEARCH -> {
                System.out.println("급여내역 조회 실행");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User user = gson.fromJson(gson.toJson(map), User.class); //받아올 객체가 하나뿐이므로 이렇게 지정
                    result = salaryService.searchSalary(user); // 급여내역 조회 결과를 result에 저장
                }
            }

            case MessageTypeConst.MESSAGE_SALARY_ADD -> {
                System.out.println("급여내역 추가 실행");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User normalUser = gson.fromJson(gson.toJson(map.get("normalUser")), User.class); //일반직원
                    User adminUser = gson.fromJson(gson.toJson(map.get("admin")), User.class); //관리자

                    if (adminUser.getRole() == Role.ADMIN) { // 관리자만이 등록할 수 있게
                        result = salaryService.addSalary(normalUser); // 관리자가 대상 유저의 월급을 등록
                    } else { // 관리자가 아닐경우
                        System.out.println("관리자만 급여 내역을 추가할 수 있습니다.");
                        result = new ResponseData("권한 없음: 관리자만 급여 내역을 추가할 수 있습니다.", null);
                    }
                }
            }

        }
        return result;
    }
}
