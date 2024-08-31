package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.AnswerService;

public class AnswerController implements Controller {
    private static AnswerController answerController = null;
    private final AnswerService answerService;

    public static AnswerController createOrGetAnswerController() {
        if (answerController == null) {
            answerController = new AnswerController(AnswerService.createOrGetAnswerService());
            System.out.println("AnswerController 싱글톤 생성");
            return answerController;
        }

        System.out.println("AnswerController 싱글톤 반환");
        return answerController;
    }

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }



    @Override
    public ResponseData execute(RequestData requestData) {
        ResponseData result = null;
        Controller controller;

        //댓글 조회
       /* if(requestData.getMessageType().contains("search")){
            answerService.AnswerSearch();
            result = controller.execute(requestData);
        }*/

        //댓글 달기 and 관리자만 할 수 있도록
        if(requestData.getMessageType().contains("add") && requestData.getMessageType().contains("admin")){
            answerService.AnswerAdd();
        }


        return result;
    }
}
