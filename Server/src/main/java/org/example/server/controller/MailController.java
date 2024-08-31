package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.MailService;

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
    public ResponseData execute(RequestData requestData) {
        return null;
    }
}