package org.example.server.domain.mail;

public class MailStore {
    private Long mailStoreNum;

    private MailType mailType;

    private Long userNum;

    private MailStore(Builder builder) {
        this.mailStoreNum=builder.mailStoreNum;
        this.mailType=builder.mailType;
        this.userNum = builder.userNum;
    }

    public static class Builder{
        private Long mailStoreNum;

        private MailType mailType;

        private Long userNum;

        public Builder mailStoreNum(Long mailStoreNum) {
            this.mailStoreNum = mailStoreNum;
            return this;
        }

        public Builder mailType(MailType mailType) {
            this.mailType = mailType;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public MailStore build() {
            return new MailStore(this);
        }
    }

    public Long getMailStoreNum() {
        return mailStoreNum;
    }

    public MailType getMailType() {
        return mailType;
    }

    public Long getUserNum() {
        return userNum;
    }
}
