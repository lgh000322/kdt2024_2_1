package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.WorkService;

import java.sql.SQLException;


public class WorkController implements Controller {
    private static WorkController workController = null;
    private final WorkService workService;

    public static WorkController createOrGetWorkController() {
        if (workController == null) {
            workController = new WorkController(WorkService.createOrGetWorkService());
            System.out.println("WorkController 싱글톤 생성");
            return workController;
        }

        System.out.println("WorkController 싱글톤 반환");
        return workController;
    }

    public WorkController(WorkService workService) {
        this.workService = workService;
    }



    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new Gson();

        switch(requestURL){
            case MessageTypeConst.MESSAGE_WORK_SEARCH -> {
                System.out.println("근퇴 내역 조회");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User user = gson.fromJson(gson.toJson(map), User.class); //받아올 객체가 하나뿐이므로 이렇게 지정
                    result = workService.SearchWork(user); // 급여내역 조회 결과를 result에 저장
                }
            }

            case MessageTypeConst.MESSAGE_WORK_START -> {
                System.out.println("출근");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User user = gson.fromJson(gson.toJson(map), User.class); //받아올 객체가 하나뿐이므로 이렇게 지정
                    result = workService.StartWork(user); // 급여내역 조회 결과를 result에 저장
                }
            }

            case MessageTypeConst.MESSAGE_WORK_FINISH -> {
                System.out.println("퇴근");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User user = gson.fromJson(gson.toJson(map), User.class); //받아올 객체가 하나뿐이므로 이렇게 지정
                    result = workService.EndWork(user); // 급여내역 조회 결과를 result에 저장
                }
            }

            case MessageTypeConst.MESSAGE_WORK_OUT_EARLY -> {
                System.out.println("조퇴");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    User user = gson.fromJson(gson.toJson(map), User.class); //받아올 객체가 하나뿐이므로 이렇게 지정
                    result = workService.EarlyOutWork(user); // 급여내역 조회 결과를 result에 저장
                }
            }


        }

        return result;
    }
}
