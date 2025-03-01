package org.example.server.controller.front_controller;

import org.example.server.container.ApplicationContext;
import org.example.server.controller.*;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

import java.sql.SQLException;

public class FrontController {

    private final ApplicationContext applicationContext;

    public FrontController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ResponseData execute(RequestData requestData) throws SQLException {
        Controller controller;

        if (requestData.getMessageType().contains("user")) {
            controller = applicationContext.getBean(UserController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("board")) {
            controller = applicationContext.getBean(BoardController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("mail")) {
            controller = applicationContext.getBean(MailController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("salary")) {
            controller = applicationContext.getBean(SalaryController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("work")) {
            controller = applicationContext.getBean(WorkController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("answer")) {
            controller = applicationContext.getBean(AnswerController.class);
            return controller.execute(requestData);
        }

        if (requestData.getMessageType().contains("leave")) {
            controller = applicationContext.getBean(LeaveController.class);
            return controller.execute(requestData);
        }

        return new ResponseData("제공되지 않는 경로", null);

    }

}