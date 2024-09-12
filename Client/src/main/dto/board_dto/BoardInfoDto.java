package main.dto.board_dto;

public class BoardInfoDto {

    private String boardTitle;
    private String boardContents;
    private String boardUserId;
    public String getBoardTitle() {
        return boardTitle;
    }


    public String getBoardContents() {
        return boardContents;
    }

    public String getBoardUserId() {
        return boardUserId;
    }

    private BoardInfoDto(Builder builder) {
        this.boardTitle = builder.boardTitle;
        this.boardUserId = builder.boardUserId;
        this.boardContents = builder.boardContents;

    }

    public static class Builder {
        private String boardTitle;
        private String boardContents;
        private String boardUserId;


        public Builder boardTitle(String boardTitle) {
            this.boardTitle = boardTitle;
            return this;
        }

        public Builder boardContents(String boardContents) {
            this.boardContents = boardContents;
            return this;
        }

        public Builder boardUserId(String boardUserId) {
            this.boardUserId = boardUserId;
            return this;
        }


        public BoardInfoDto build() {
            return new BoardInfoDto(this);
        }
    }
}
