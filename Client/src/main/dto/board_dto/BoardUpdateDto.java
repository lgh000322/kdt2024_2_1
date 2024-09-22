package main.dto.board_dto;

public class BoardUpdateDto {

    public Long getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(Long boardNum) {
        this.boardNum = boardNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    private Long boardNum;

    private String title;

    private String contents;




}
