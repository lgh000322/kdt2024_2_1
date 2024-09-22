package main.dto.board_dto;

public class BoardInfoDto2 {

    private String boardTitle;
    private String boardContents;
    private String boardUserId;
    private String boardUserName;
    private Long userNum;
    
    public Long getUserNum() {
    	return userNum;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public String getBoardContents() {
        return boardContents;
    }

    public String getBoardUserId() {
        return boardUserId;
    }

    public String getBoardUserName() { return boardUserName; }

    private BoardInfoDto2(Builder builder) {
        this.boardTitle = builder.boardTitle;
        this.boardUserId = builder.boardUserId;
        this.boardContents = builder.boardContents;
        this.boardUserName = builder.boardUserName;
        this.userNum=builder.userNum;

    }

    public static class Builder {
        private String boardTitle;
        private String boardContents;
        private String boardUserId;
        private String boardUserName;
        private Long userNum;
        
        public Builder userNum(Long userNum) {
        	this.userNum=userNum;
        	return this;
        }

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

        public Builder boardUserName(String boardUserName) {
            this.boardUserName = boardUserName;
            return this;
        }


        public BoardInfoDto2 build() {
            return new BoardInfoDto2(this);
        }
    }
}
