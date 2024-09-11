package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.leave_dto.ForFindLeaveDto;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.leave_dto.ForRequestLeaveDto;
import org.example.server.dto.leave_dto.ForUpdateLeaveDto;
import org.example.server.service.LeaveService;

import java.sql.SQLException;

import static org.example.server.consts.MessageTypeConst.MESSAGE_LEAVE_REQUEST;

public class LeaveController implements Controller {

    private static LeaveController leaveController = null;
    private final LeaveService leaveService;

   //의존성 주입.
    public static LeaveController createOrGetLeaveController() {

        if (leaveController == null) {
            leaveController = new LeaveController(LeaveService.createOrGetLeaveService());
            System.out.println("LeaveController 싱글톤 생성");
            return leaveController;
        }

        //기존 컨트롤러가 있으면 기존 컨트롤러 반환
        System.out.println("LeaveController 싱글톤 반환");
        return leaveController;
    }

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveController.leaveService;
    }


    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new Gson();

        switch (requestURL) {
            case MESSAGE_LEAVE_REQUEST -> {
                System.out.println("휴가 신청 실행");
                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    ForRequestLeaveDto forRequestLeaveDto = gson.fromJson(gson.toJson(map), ForRequestLeaveDto.class);
                    result = leaveService.requestLeave(forRequestLeaveDto);
                }

            }
            case MessageTypeConst.MESSAGE_LEAVE_EDIT -> {
                System.out.println("휴가 변경 실행");

                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    ForUpdateLeaveDto forUpdateLeaveDto = gson.fromJson(gson.toJson(map), ForUpdateLeaveDto.class);
                    result = leaveService.updateLeaveLog(forUpdateLeaveDto);
                }
            }
            case MessageTypeConst.MESSAGE_LEAVE_SEARCH -> {
                System.out.println("휴가 조회 실행");

                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    ForFindLeaveDto forFindLeaveDto = gson.fromJson(gson.toJson(map), ForFindLeaveDto.class);
                    result = leaveService.findLeaveLog(forFindLeaveDto);
                }

            }

        }

        return result;
    }
}
