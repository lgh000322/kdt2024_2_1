package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.mail.Mail;
import org.example.server.domain.mail.MailType;
import org.example.server.domain.mail.ReceivedMail;
import org.example.server.domain.user.User;
import org.example.server.dto.mail_dto.MailAllDto;
import org.example.server.dto.mail_dto.MailReceivedData;
import org.example.server.dto.mail_dto.MailSearchDto;
import org.example.server.dto.ResponseData;
import org.example.server.dto.mail_dto.UserAndMailStore;
import org.example.server.repository.MailRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MailService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;

    public MailService(MailRepository mailRepository, UserRepository userRepository) {
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.dataSource = DBUtils.createOrGetDataSource();
    }

    private static MailService mailService = null;
    private final DataSource dataSource;


    public static MailService createOrGetMailService() {
        if (mailService == null) {
            mailService = new MailService(MailRepository.createOrGetMailRepository(), UserRepository.createOrGetUserRepository());
            return mailService;
        }

        return mailService;
    }

    public ResponseData mailSend(MailReceivedData mailReceivedData) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = mailSendBizLogic(con, mailReceivedData);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    public ResponseData mailSearchAll(MailSearchDto mailSearchDto) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = mailSearchBizLogic(con, mailSearchDto);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    public ResponseData mailSearchOne(Long mailNum) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData=mailSearchOneBizLogic(con, mailNum);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    private ResponseData mailSearchOneBizLogic(Connection con, Long mailNum) throws SQLException {
        Optional<Mail> mailOne = mailRepository.findMailOne(con, mailNum);
        if (mailOne.isEmpty()) {
            return new ResponseData("특정 메일을 조회 실패", null);
        }
        return new ResponseData("특정 메일 조회 성공", mailOne.get());
    }

    private ResponseData mailSearchBizLogic(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        if (mailSearchDto.getMailType() == MailType.SEND) {
            Optional<List<Mail>> sendMailAll = mailRepository.findSendMailAll(con, mailSearchDto);
            if (sendMailAll.isEmpty()) {
                return new ResponseData("해당 유저의 송신메일함이 비어있습니다.", null);
            }

            return new ResponseData("유저의 송신메일함의 모든 메일 조회 성공", sendMailAll.get());
        } else {
            Optional<List<MailAllDto>> receivedMailAll = mailRepository.findReceivedMailAll(con, mailSearchDto);
            if (receivedMailAll.isEmpty()) {
                return new ResponseData("해당 유저의 수신메일함이 비어있습니다.", null);
            }
            return new ResponseData("유저의 수신 메일함의 모든 메일 조회 성공", receivedMailAll.get());
        }
    }

    private ResponseData mailSendBizLogic(Connection con, MailReceivedData mailReceivedData) throws SQLException {

        // 송신자 유저의 이메일로 해당 유저의 보내는 메일함에 대한 기본키를 가져온다.(mail_store SEND)
        String sendUserEmail = mailReceivedData.getSendUserEmail();
        UserAndMailStore mailStoreByUserEmailAndMailType1 = mailRepository.findMailStoreByUserEmailAndMailType(con, sendUserEmail, MailType.SEND);

        // 메일 내용을 저장한다. (mail)
        Mail mail = mailReceivedData.getMail();
        mail.changeMailStoreNum(mailStoreByUserEmailAndMailType1.getMailStoreNum());
        Long savedMailNum = mailRepository.mailSave(con, mail);

        // 수신자 유저의 이메일로 해당 유저의 받는 메일함에 대한 기본키를 가져온다 (mail_store RECEIVED, List형태의 메일함을 받아야 한다.)
        List<String> receivedUserEmails = mailReceivedData.getReceivedUserEmails();
        List<Long> userNums = new ArrayList<>();

        for (String receivedUserEmail : receivedUserEmails) {
            Optional<User> findUser = userRepository.findUserByEmail(con, receivedUserEmail);
            if (findUser.isPresent()) {
                Long userNum = findUser.get().getUserNum();
                userNums.add(userNum);
            }
        }

        // 각 수신자의 받는 메일함에 데이터를 저장한다.
        for (Long userNum : userNums) {
            ReceivedMail receivedMail = new ReceivedMail.Builder()
                    .userNum(userNum)
                    .mailNum(savedMailNum)
                    .build();

            mailRepository.receivedMailStoreSave(con, receivedMail);
        }

        return new ResponseData("성공", null);
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                System.out.println("커넥션 반환중 에러 발생");
            }
        }
    }
}
