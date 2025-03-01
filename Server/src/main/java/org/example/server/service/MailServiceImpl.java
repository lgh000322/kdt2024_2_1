package org.example.server.service;

import org.example.server.domain.mail.Mail;
import org.example.server.domain.mail.MailType;
import org.example.server.domain.mail.ReceivedMail;
import org.example.server.domain.user.User;
import org.example.server.dto.mail_dto.*;
import org.example.server.dto.ResponseData;
import org.example.server.repository.MailRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.declare.MailService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MailServiceImpl implements MailService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource;

    public MailServiceImpl(MailRepository mailRepository, UserRepository userRepository,DataSource dataSource) {
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.dataSource = dataSource;
    }

    @Override
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

    @Override
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

    @Override
    public ResponseData mailSearchOne(Long mailNum) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = mailSearchOneBizLogic(con, mailNum);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    @Override
    public ResponseData mailDeleteOne(MailDeleteDto mailDeleteDto) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = mailDeleteOneBizLogic(con, mailDeleteDto);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }


    private ResponseData mailDeleteOneBizLogic(Connection con, MailDeleteDto mailDeleteDto) throws SQLException {
        if (mailDeleteDto.getMailType() == MailType.SEND) {
            return mailRepository.changeMailIsDeleted(con, mailDeleteDto.getMailNum()) ?
                    new ResponseData("보낸 메일함에서 특정 메일 삭제 성공", null) :
                    new ResponseData("보낸 메일함에서 특정 메일 삭제 실패", null);
        } else {
            return mailRepository.changeReceivedMailIsDeleted(con, mailDeleteDto.getMailNum())
                    ? new ResponseData("받은 메일함에서 특정 메일 삭제 성공", null)
                    : new ResponseData("받은 메일함에서 특정 메일 삭제 실패", null);
        }

    }


    private ResponseData mailSearchOneBizLogic(Connection con, Long mailNum) throws SQLException {
        Optional<Mail> mailOne = mailRepository.findMailOne(con, mailNum);
        if (mailOne.isEmpty()) {
            return new ResponseData("특정 메일을 조회 실패", null);
        }
        return new ResponseData("특정 메일 조회 성공", mailOne.get());
    }

    private ResponseData mailSearchBizLogic(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        if (mailSearchDto.getMailType() == MailType.SEND && mailSearchDto.getMailTitle().equals("")) {
            Optional<List<MailAllDto>> sendMailAll = mailRepository.findSendMailAll(con, mailSearchDto);
            if (sendMailAll.isEmpty()) {
                return new ResponseData("해당 유저의 송신메일함이 비어있습니다.", null);
            }
            List<MailAllDto> mailAllDtos = sendMailAll.get();

            mailAllDtos.forEach(mailAllDto -> {
                Long mailNum = mailAllDto.getMailNum();
                String senderEmail = null;
                try {
                    senderEmail = mailRepository.findReceiverEmail(con, mailNum);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mailAllDto.changeUserEmail(senderEmail);  // 기존 mailAllDto 객체 수정
            });

            return new ResponseData("유저의 송신메일함의 모든 메일 조회 성공", mailAllDtos);
        } else if (mailSearchDto.getMailType() == MailType.SEND && !mailSearchDto.getMailTitle().equals("")) {
            Optional<List<MailAllDto>> sendMailAllByTitle = mailRepository.findSendMailAllByTitle(con, mailSearchDto);
            if (sendMailAllByTitle.isEmpty()) {
                return new ResponseData("해당 유저의 송신메일함이 비어있습니다.", null);
            }

            List<MailAllDto> mailAllDtos = sendMailAllByTitle.get();

            mailAllDtos.forEach(mailAllDto -> {
                Long mailNum = mailAllDto.getMailNum();
                String senderEmail = null;
                try {
                    senderEmail = mailRepository.findReceiverEmail(con, mailNum);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mailAllDto.changeUserEmail(senderEmail);  // 기존 mailAllDto 객체 수정
            });
            return new ResponseData("유저의 송신메일함의 모든 메일 조회 성공", mailAllDtos);

        } else if (mailSearchDto.getMailType() == MailType.RECEIVED && mailSearchDto.getMailTitle().equals("")) {
            Optional<List<MailAllDto>> receivedMailAll = mailRepository.findReceivedMailAll(con, mailSearchDto);
            if (receivedMailAll.isEmpty()) {
                return new ResponseData("해당 유저의 수신메일함이 비어있습니다.", null);
            }

            List<MailAllDto> mailAllDtos = receivedMailAll.get();

            mailAllDtos.forEach(mailAllDto -> {
                Long mailNum = mailAllDto.getMailNum();
                String senderEmail = null;
                try {
                    senderEmail = mailRepository.findSenderEmail(con, mailNum);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mailAllDto.changeUserEmail(senderEmail);  // 기존 mailAllDto 객체 수정
            });

            return new ResponseData("유저의 수신 메일함의 모든 메일 조회 성공", mailAllDtos);  // 수정된 기존 리스트 반환

        } else {
            Optional<List<MailAllDto>> receivedMailAllByTitle = mailRepository.findReceivedMailAllByTitle(con, mailSearchDto);
            if (receivedMailAllByTitle.isEmpty()) {
                return new ResponseData("해당 유저의 수신메일함이 비어있습니다.", null);
            }

            List<MailAllDto> mailAllDtos = receivedMailAllByTitle.get();

            mailAllDtos.forEach(mailAllDto -> {
                Long mailNum = mailAllDto.getMailNum();
                String senderEmail = null;
                try {
                    senderEmail = mailRepository.findSenderEmail(con, mailNum);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mailAllDto.changeUserEmail(senderEmail);  // 기존 mailAllDto 객체 수정
            });

            return new ResponseData("유저의 수신 메일함의 모든 메일 조회 성공", receivedMailAllByTitle.get());
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
