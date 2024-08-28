package org.example.server.controller.front_controller;

import org.example.server.controller.Controller;
import org.example.server.controller.MemberController;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

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
        nextController.put("MemberController", MemberController.createOrGetMemberController());
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
    public ResponseData execute(RequestData requestData){
        ResponseData result = null;
        Controller controller;

        if (requestData.getMessageType().contains("member")) {
            controller = nextController.get("MemberController");
            result = controller.execute(requestData);
        }


        return result;

    }

}
