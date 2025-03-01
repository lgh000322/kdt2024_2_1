package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.adapter.LocalDateTypeAdapter;
import org.example.server.adapter.LocalTimeTypeAdapter;
import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.mail_dto.MailDeleteDto;
import org.example.server.dto.mail_dto.MailReceivedData;
import org.example.server.dto.mail_dto.MailSearchDto;
import org.example.server.service.MailServiceImpl;
import org.example.server.service.declare.MailService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MailController implements Controller {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_MAIL_ADD -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("메일 전송 실행");
                    MailReceivedData mailReceivedData = gson.fromJson(gson.toJson(map), MailReceivedData.class);
                    result = mailService.mailSend(mailReceivedData);
                }
            }
            case MessageTypeConst.MESSAGE_STORE_SEARCH -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("전체 메일 조회 실행");
                    MailSearchDto mailSearchDto = gson.fromJson(gson.toJson(map), MailSearchDto.class);
                    result = mailService.mailSearchAll(mailSearchDto);
                }
            }
            case MessageTypeConst.MESSAGE_MAIL_ONE_SEARCH -> {
                System.out.println("특정 메일 조회");
                Double data = (Double) requestData.getData();
                Long mailNum = data.longValue();
                result = mailService.mailSearchOne(mailNum);
            }
            case MessageTypeConst.MESSAGE_MAIL_ONE_DELETE -> {
                if (requestData.getData() instanceof LinkedTreeMap<?, ?> map) {
                    System.out.println("특정 메일 삭제");
                    MailDeleteDto mailDeleteDto = gson.fromJson(gson.toJson(map), MailDeleteDto.class);
                    result = mailService.mailDeleteOne(mailDeleteDto);
                }
            }
        }

        return result;
    }
}
