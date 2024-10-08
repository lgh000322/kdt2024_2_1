package main.dto.board_dto;

import java.time.LocalDate;

public class BoardFindAllDto {
	  private Long boardNum;

	    private String title;

	    private String userId;

	    private LocalDate createdDate;

	    private Long userNum;

	    private BoardFindAllDto(Builder builder) {
	        this.boardNum = builder.boardNum;
	        this.title = builder.title;
	        this.userId = builder.userId;
	        this.createdDate = builder.createdDate;
	        this.userNum=builder.userNum;
	    }

	    public Long getUserNum() {
	        return userNum;
	    }

	    public LocalDate getCreatedDate() {
	        return createdDate;
	    }



	    public String getUserId() {
	        return userId;
	    }


	    public String getTitle() {
	        return title;
	    }



	    public Long getBoardNum() {
	        return boardNum;
	    }

	    public static class Builder{
	        private Long boardNum;
	        private String title;
	        private String userId;
	        private LocalDate createdDate;
	        private Long userNum;

	        public Builder userNum(Long userNum) {
	            this.userNum=userNum;
	            return this;
	        }

	        public Builder boardNum(Long boardNum) {
	            this.boardNum = boardNum;
	            return this;
	        }

	        public Builder title(String title) {
	            this.title = title;
	            return this;
	        }

	        public Builder userId(String userId) {
	            this.userId = userId;
	            return this;
	        }

	        public Builder createdDate(LocalDate createdDate) {
	            this.createdDate = createdDate;
	            return this;
	        }

	        public BoardFindAllDto build() {return new BoardFindAllDto(this);}
	    }


}
