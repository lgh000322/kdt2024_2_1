package main.domain.board;

import java.time.LocalDate;

public class Board {
    private Long boardNum;

    private String title;

    private String contents;

    private LocalDate createdDate;

    private Long userNum;

    private Board(Builder builder) {
        this.boardNum = builder.boardNum;
        this.title = builder.title;
        this.contents = builder.contents;
        this.createdDate = builder.createdDate;
        this.userNum = builder.userNum;
    }

    public static class Builder{
        private Long boardNum;

        private String title;

        private String contents;

        private LocalDate createdDate;

        private Long userNum;

        public Builder boardNum(Long boardNum) {
            this.boardNum = boardNum;
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

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
    public Long getBoardNum() {
        return boardNum;
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

    public Long getUserNum() {
        return userNum;
    }
}
