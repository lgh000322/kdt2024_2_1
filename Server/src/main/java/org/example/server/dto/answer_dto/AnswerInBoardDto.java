package org.example.server.dto;

public class AnswerInBoardDto {
    private String answerUserId;
    private String answerContent;


    public String getAnswerUserId() {
        return answerUserId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public AnswerInBoardDto(Builder builder) {
        this.answerContent = builder.answerContent;
        this.answerUserId = builder.answerUserId;
    }

    public static class Builder {
        private String answerUserId;
        private String answerContent;

        public Builder answerUserId(String answerUserId) {
            this.answerUserId = answerUserId;
            return this;
        }

        public Builder answerContent(String answerContent) {
            this.answerContent = answerContent;
            return this;
        }

        public AnswerInBoardDto build() {return new AnswerInBoardDto(this);}

    }
}
