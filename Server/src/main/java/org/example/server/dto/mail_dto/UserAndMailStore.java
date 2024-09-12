package org.example.server.dto.mail_dto;

public class UserAndMailStore {
    private Long mailStoreNum;

    private Long receivedUserNum;

    public Long getMailStoreNum() {
        return mailStoreNum;
    }

    public void setMailStoreNum(Long mailStoreNum) {
        this.mailStoreNum = mailStoreNum;
    }

    public Long getReceivedUserNum() {
        return receivedUserNum;
    }

    public void setReceivedUserNum(Long receivedUserNum) {
        this.receivedUserNum = receivedUserNum;
    }
}
