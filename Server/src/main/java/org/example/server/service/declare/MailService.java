package org.example.server.service.declare;

import org.example.server.dto.ResponseData;
import org.example.server.dto.mail_dto.MailDeleteDto;
import org.example.server.dto.mail_dto.MailReceivedData;
import org.example.server.dto.mail_dto.MailSearchDto;

import java.sql.SQLException;

public interface MailService {
    ResponseData mailSend(MailReceivedData mailReceivedData) throws SQLException;

    ResponseData mailSearchAll(MailSearchDto mailSearchDto) throws SQLException;

    ResponseData mailSearchOne(Long mailNum) throws SQLException;

    ResponseData mailDeleteOne(MailDeleteDto mailDeleteDto) throws SQLException;

}
