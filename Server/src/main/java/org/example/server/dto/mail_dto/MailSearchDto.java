package org.example.server.dto.mail_dto;

import org.example.server.domain.mail.MailType;

public class MailSearchDto {
    //클라이언트가 유저 이메일 보내준다고 가정함.
    private String email;


    //클라이언트가 어떤 메일함인지 메일타입을 보내준다고 가정함.
    private MailType mailType;

    //클라이언트의 제목 검색
    private String mailTitle;

    public String getMailTitle() {
        return mailTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MailType getMailType() {
        return mailType;
    }

    public void setMailType(MailType mailType) {
        this.mailType = mailType;
    }
}
