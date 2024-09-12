package org.example.server.service;

import org.example.server.domain.mail.Mail;
import org.example.server.dto.MailReceivedData;
import org.example.server.dto.ResponseData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MailServiceTest {

    MailService mailService = MailService.createOrGetMailService();

    @Test
    public void 메일저장테스트() throws Exception{
        //given
        MailReceivedData mailReceivedData = new MailReceivedData();
        Mail mail = new Mail.Builder()
                .title("title")
                .contents("내용")
                .createdDate(LocalDate.now())
                .build();

        String sendUserEmail = "aaa@aaa.com";

        List<String> receivedUserEmail = new ArrayList<>();
        receivedUserEmail.add("bbb@bbb.com");

        mailReceivedData.setMail(mail);
        mailReceivedData.setSendUserEmail(sendUserEmail);
        mailReceivedData.setReceivedUserEmails(receivedUserEmail);
        //when

        ResponseData result = mailService.mailSend(mailReceivedData);

        //then

        System.out.println("메세지: "+result.getMessageType()+" 데이터: "+result.getData());
    }

}