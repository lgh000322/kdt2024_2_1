package org.example.server.controller.front_controller;

import org.example.server.controller.*;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {
    private static FrontController frontController = null;
    private final static Map<String, Controller> nextController = new HashMap<>();

    /**
     * 다음에 올 수 있는 컨트롤러의 목록을 추가해줘야 한다.
     */
    private FrontController() {
        System.out.println("묵시적 프론트 컨트롤러 실행");
        nextController.put("UserController", UserController.createOrGetUserController());
        nextController.put("BoardController", BoardController.createOrGetBoardController());
        nextController.put("MailController", MailController.createOrGetMailController());
        nextController.put("SalaryController", SalaryController.createOrGetSalaryController());
        nextController.put("WorkController", WorkController.createOrGetWorkController());
        nextController.put("AnswerController", AnswerController.createOrGetAnswerController());
    }

    /**
     * 싱글톤 패턴으로 FrontController 인스턴스를 반환해주는 메소드
     * @return
     */
    public static FrontController createOrGetFrontController() {
        if (frontController == null) {
            frontController = new FrontController();
            return frontController;
        }

        return frontController;
    }

    /**
     * FrontController의 실질적인 실행 메소드. 각각의 세부 컨트롤러에선 ResponseData를 반환해주어야 한다.
     * @param requestData 클라이언트로부터 받은 데이터를 의미한다.
     * @return 이전에 설정한 ResponseData의 인스턴스를 반환한다.
     */
    public ResponseData execute(RequestData requestData) throws SQLException {
        ResponseData result = null;
        Controller controller;

        if (requestData.getMessageType().contains("user")) {
            controller = nextController.get("UserController");
            result = controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("board")) {
            controller = nextController.get("BoardController");
            result = controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("mail")) {
            controller = nextController.get("MailController");
            result=controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("salary")) {
            controller = nextController.get("SalaryController");
            result=controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("work")) {
            controller = nextController.get("WorkController");
            result=controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("answer")) {
            controller = nextController.get("AnswerController");
            result=controller.execute(requestData);
        }


        return result;

    }

}