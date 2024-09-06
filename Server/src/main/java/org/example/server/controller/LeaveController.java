package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
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

        switch (requestURL) {
            case MESSAGE_LEAVE_REQUEST -> {
                System.out.println("휴가 신청 실행");

            }
            case MessageTypeConst.MESSAGE_LEAVE_EDIT -> {
                System.out.println("휴가 변경 실행");

            }
            case MessageTypeConst.MESSAGE_LEAVE_SEARCH -> {
                System.out.println("휴가 조회 실행");

            }

        }

        return result;
    }
}