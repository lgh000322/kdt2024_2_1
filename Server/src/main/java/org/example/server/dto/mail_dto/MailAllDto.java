package org.example.server.dto.mail_dto;

import java.time.LocalDate;

public class MailAllDto {
    private Long mailNum;

    private String title;

    private String contents;

    private LocalDate createdDate;

    private String userEmail;

    private Long mailStoreNum;

    public void changeUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    private MailAllDto(Builder builder) {
        this.mailNum=builder.mailNum;
        this.title=builder.title;
        this.contents=builder.contents;
        this.createdDate=builder.createdDate;
        this.mailStoreNum = builder.mailStoreNum;
        this.userEmail =builder.userEmail;
    }

    public static class Builder{
        private Long mailNum;

        private String userEmail;

        private String title;

        private String contents;

        private LocalDate createdDate;

        private Long mailStoreNum;

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }
        public Builder mailNum(Long mailNum) {
            this.mailNum = mailNum;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder createdDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder mailStoreNum(Long mailStoreNum) {
            this.mailStoreNum = mailStoreNum;
            return this;
        }

        public MailAllDto build() {
            return new MailAllDto(this);
        }
    }

    public Long getMailNum() {
        return mailNum;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Long getMailStoreNum() {
        return mailStoreNum;
    }

    public void changeMailStoreNum(Long mailStoreNum) {
        this.mailStoreNum = mailStoreNum;
    }
}
