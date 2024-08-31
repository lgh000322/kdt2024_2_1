package org.example.server.domain.board;

import java.time.LocalDate;

public class BoardAnswer {
    private Long answerNum;

    private String contents;

    private LocalDate createdDate;

    private Long boardNum;

    private Long userNum;

    private BoardAnswer(Builder builder) {
        this.answerNum=builder.answerNum;
        this.contents = builder.contents;
        this.createdDate = builder.createdDate;
        this.boardNum=builder.boardNum;
        this.userNum = builder.userNum;
    }

    public static class Builder{
        private Long answerNum;

        private String contents;

        private LocalDate createdDate;

        private Long boardNum;

        private Long userNum;

        public Builder answerNum(Long answerNum) {
            this.answerNum = answerNum;
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

        public Builder boardNum(Long boardNum) {
            this.boardNum = boardNum;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public BoardAnswer build() {
            return new BoardAnswer(this);
        }

    }

    public Long getAnswerNum() {
        return answerNum;
    }

    public String getContents() {
        return contents;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Long getBoardNum() {
        return boardNum;
    }

    public Long getUserNum() {
        return userNum;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/development_KimGyeonghun
