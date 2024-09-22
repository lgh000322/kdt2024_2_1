package main.dto.board_dto;

import java.util.List;

import main.dto.answer_dto.AnswerInBoardDto;

public class BoardAndAnswer {

    private BoardInfoDto2 boardInfoDto;
    private List<AnswerInBoardDto> answerInBoard;


    public BoardInfoDto2 getBoardInfoDto() {
        return boardInfoDto;
    }

    public List<AnswerInBoardDto> getAnswerInBoard() {
        return answerInBoard;
    }

    public BoardAndAnswer(BoardInfoDto2 boardInfoDto, List<AnswerInBoardDto> answerInBoard) {
        this.boardInfoDto = boardInfoDto;
        this.answerInBoard = answerInBoard;
    }
}
