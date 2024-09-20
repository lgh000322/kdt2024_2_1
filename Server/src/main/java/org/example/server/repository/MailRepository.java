package org.example.server.repository;

import org.example.server.domain.mail.Mail;
import org.example.server.domain.mail.MailStore;
import org.example.server.domain.mail.MailType;
import org.example.server.domain.mail.ReceivedMail;
import org.example.server.dto.mail_dto.MailAllDto;
import org.example.server.dto.mail_dto.MailSearchDto;
import org.example.server.dto.mail_dto.UserAndMailStore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MailRepository {
    private static MailRepository mailRepository = null;

    public static MailRepository createOrGetMailRepository() {
        if (mailRepository == null) {
            mailRepository = new MailRepository();
            return mailRepository;
        }

        return mailRepository;
    }

    public void mailStoreSave(Connection con, MailStore mailStore) throws SQLException {
        String sql = "insert into mail_store (mail_type, user_num) values (?, ?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);

            // 각 파라미터를 set
            pstmt.setString(1, mailStore.getMailType().toString());
            pstmt.setLong(2, mailStore.getUserNum());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, null);
        }
    }

    public Long mailSave(Connection con, Mail mail) throws SQLException {
        String sql = "insert into mail (title, contents, created_date, mail_store_num)" +
                " values(?, ?, ?, ?)";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // 각 파라미터를 set
            pstmt.setString(1, mail.getTitle());
            pstmt.setString(2, mail.getContents());
            pstmt.setDate(3, Date.valueOf(mail.getCreatedDate()));
            pstmt.setLong(4, mail.getMailStoreNum());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }else {
                throw new SQLException("Creating mail failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public void receivedMailStoreSave(Connection con, ReceivedMail receivedMail) throws SQLException {
        String sql = "insert into received_mail (user_num, mail_num) values (?, ?)";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // 각 파라미터를 set
            pstmt.setLong(1, receivedMail.getUserNum());
            pstmt.setLong(2,receivedMail.getMailNum());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, null);
        }
    }

    public String findSenderEmail(Connection con,Long mailNum) throws SQLException {
        String sql = "select user.email from mail" +
                " left join mail_store on mail.mail_store_num = mail_store.mail_store_num" +
                " left join user on mail_store.user_num = user.user_num" +
                " where mail.mai_num = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result=null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, mailNum);

            rs = pstmt.executeQuery();


            if (rs.next()) {
                result = rs.getString("user.email");
            }

            return result;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public String findReceiverEmail(Connection con,Long mailNum) throws SQLException {
        String sql = "select user.email from mail" +
                " left join received_mail on mail.mai_num = received_mail.mail_num" +
                " left join user on user.user_num = received_mail.user_num" +
                " where mail.mai_num = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result=null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, mailNum);

            rs = pstmt.executeQuery();


            if (rs.next()) {
                result = rs.getString("user.email");
            }

            return result;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public UserAndMailStore findMailStoreByUserEmailAndMailType(Connection con, String userEmail, MailType mailType) throws SQLException {
        String sql = "select * from mail_store" +
                " inner join user on mail_store.user_num = user.user_num" +
                " where user.email = ? and mail_store.mail_type = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            pstmt.setString(2, mailType.name());
            rs = pstmt.executeQuery();

            UserAndMailStore userAndMailStore = new UserAndMailStore();

            if (rs.next()) {
                userAndMailStore.setMailStoreNum(rs.getLong("mail_store_num"));
                userAndMailStore.setReceivedUserNum(rs.getLong("user.user_num"));
            }

            return userAndMailStore;

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<List<MailAllDto>> findSendMailAll(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        String sql = "select mail.mai_num, mail.title, mail.created_date, user.email" +
                " from mail" +
                " left join mail_store on mail.mail_store_num = mail_store.mail_store_num" +
                " left join user on mail_store.user_num = user.user_num" +
                " where user.email = ?" +
                " order by mail.mai_num desc";


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MailAllDto> result = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mailSearchDto.getEmail());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MailAllDto mailAllDto=new MailAllDto.Builder()
                        .mailNum(rs.getLong("mail.mai_num"))
                        .title(rs.getString("mail.title"))
                        .createdDate(rs.getDate("mail.created_date").toLocalDate())
                        .userEmail(rs.getString("user.email"))
                        .build();

                result.add(mailAllDto);
            }

            if (result.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(result);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<List<MailAllDto>> findSendMailAllByTitle(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        String sql = "select mail.mai_num, mail.title, mail.created_date, user.email" +
                " from mail" +
                " left join mail_store on mail.mail_store_num = mail_store.mail_store_num" +
                " left join user on mail_store.user_num = user.user_num" +
                " where user.email = ? and mail.title like ?" +
                " order by mail.mai_num desc";


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MailAllDto> result = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mailSearchDto.getEmail());
            pstmt.setString(2, "%"+mailSearchDto.getMailTitle()+"%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MailAllDto mailAllDto=new MailAllDto.Builder()
                        .mailNum(rs.getLong("mail.mai_num"))
                        .title(rs.getString("mail.title"))
                        .createdDate(rs.getDate("mail.created_date").toLocalDate())
                        .userEmail(rs.getString("user.email"))
                        .build();

                result.add(mailAllDto);
            }

            if (result.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(result);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<List<MailAllDto>> findReceivedMailAll(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        String sql = "select mail.mai_num, mail.title, mail.created_date" +
                " from mail" +
                " left join received_mail on mail.mai_num = received_mail.mail_num" +
                " left join user on received_mail.user_num = user.user_num" +
                " where user.email = ?" +
                " order by mail.mai_num desc";


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MailAllDto> result = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mailSearchDto.getEmail());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MailAllDto mailAllDto=new MailAllDto.Builder()
                        .mailNum(rs.getLong("mail.mai_num"))
                        .title(rs.getString("mail.title"))
                        .createdDate(rs.getDate("mail.created_date").toLocalDate())
                        .build();


                result.add(mailAllDto);
            }

            if (result.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(result);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<List<MailAllDto>> findReceivedMailAllByTitle(Connection con, MailSearchDto mailSearchDto) throws SQLException {
        String sql = "select mail.mai_num, mail.title, mail.created_date, user.email" +
                " from mail" +
                " left join received_mail on mail.mai_num = received_mail.mail_num" +
                " left join user on received_mail.user_num = user.user_num" +
                " where user.email = ? and mail.title like ?" +
                " order by mail.mai_num desc";


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MailAllDto> result = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mailSearchDto.getEmail());
            pstmt.setString(2, "%"+mailSearchDto.getMailTitle()+"%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MailAllDto mailAllDto=new MailAllDto.Builder()
                        .mailNum(rs.getLong("mail.mai_num"))
                        .title(rs.getString("mail.title"))
                        .createdDate(rs.getDate("mail.created_date").toLocalDate())
                        .userEmail(rs.getString("user.email"))
                        .build();


                result.add(mailAllDto);
            }

            if (result.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(result);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<Mail> findMailOne(Connection con, Long mailNum) throws SQLException {

        String sql = "select * from mail where mai_num = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, mailNum);
            rs = pstmt.executeQuery();
            Mail mail = null;
            if (rs.next()) {
                mail = new Mail.Builder()
                        .title(rs.getString("title"))
                        .contents(rs.getString("contents"))
                        .createdDate(rs.getDate("created_date").toLocalDate())
                        .build();
            }
            return Optional.ofNullable(mail);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    private static void close(PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
