package org.example.server.domain.mail;

public class ReceivedMail {
    private Long receivedNum;

    private Long userNum;

    private Long mailNum;

    private ReceivedMail(Builder builder) {
        this.receivedNum = builder.receivedNum;
        this.userNum = builder.userNum;
        this.mailNum = builder.mailNum;
    }

    public static class Builder{
        private Long receivedNum;

        private Long userNum;

        private Long mailNum;

        public Builder receivedNum(Long receivedNum) {
            this.receivedNum = receivedNum;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public Builder mailNum(Long mailNum) {
            this.mailNum = mailNum;
            return this;
        }

        public ReceivedMail build() {
            return new ReceivedMail(this);
        }
    }

    public Long getReceivedNum() {
        return receivedNum;
    }

    public Long getUserNum() {
        return userNum;
    }

    public Long getMailNum() {
        return mailNum;
    }
}
