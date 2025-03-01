package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.adapter.LocalDateTypeAdapter;
import org.example.server.adapter.LocalTimeTypeAdapter;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UserSearchDto;
import org.example.server.service.WorkServiceImpl;
import org.example.server.service.declare.WorkService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;


public class WorkController implements Controller {
    private final WorkService workService;

    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();

        switch(requestURL){
            case MessageTypeConst.MESSAGE_WORK_SEARCH -> {
                System.out.println("근퇴 내역 조회");
                if (requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    UserSearchDto user = gson.fromJson(gson.toJson(map), UserSearchDto.class); //받아올 객체가 하나뿐이므로 이렇게 지정
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
