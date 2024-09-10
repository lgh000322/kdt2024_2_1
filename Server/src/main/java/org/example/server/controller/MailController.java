package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.mail_dto.MailReceivedData;
import org.example.server.dto.mail_dto.MailSearchDto;
import org.example.server.service.MailService;

import java.sql.SQLException;

public class MailController implements Controller {
    private static MailController mailController = null;
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    public static MailController createOrGetMailController() {
        if (mailController == null) {
            mailController = new MailController(MailService.createOrGetMailService());
            return mailController;
        }

        return mailController;
    }

    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new Gson();

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_MAIL_ADD -> {
                if (requestData.getData() instanceof LinkedTreeMap) {
                    System.out.println("메일 전송 실행");
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    MailReceivedData mailReceivedData = gson.fromJson(gson.toJson(map), MailReceivedData.class);
                    result = mailService.mailSend(mailReceivedData);
                }
            }
            case MessageTypeConst.MESSAGE_STORE_SEARCH -> {
                if (requestData.getData() instanceof LinkedTreeMap) {
                    System.out.println("전체 메일 조회 실행");
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    MailSearchDto mailSearchDto = gson.fromJson(gson.toJson(map), MailSearchDto.class);
                    result=mailService.mailSearchAll(mailSearchDto);
                }
            }
            case MessageTypeConst.MESSAGE_MAIL_ONE_SEARCH -> {
                if (requestData.getData() instanceof LinkedTreeMap) {
                    System.out.println("특정 메일 조회");
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    Long mailNum = (Long) map.get("mailNum");
                    result=mailService.mailSearchOne(mailNum);
                }
            }
        }

        return result;
    }
}
