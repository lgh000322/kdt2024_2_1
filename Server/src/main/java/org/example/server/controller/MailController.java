package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.mail.Mail;
import org.example.server.dto.MailReceivedData;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
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

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_MAIL_ADD -> {
                MailReceivedData mailReceivedData = (MailReceivedData) requestData.getData();
                result = mailService.mailSend(mailReceivedData);
            }
        }

        return result;
    }
}
