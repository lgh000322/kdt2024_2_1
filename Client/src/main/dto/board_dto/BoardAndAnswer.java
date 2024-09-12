package main.dto.board_dto;

import java.util.List;

import main.dto.answer_dto.AnswerInBoardDto;

public class BoardAndAnswer {



    private BoardInfoDto boardInfoDto;
    private List<AnswerInBoardDto> answerInBoard;


    public BoardInfoDto getBoardInfoDto() {
        return boardInfoDto;
    }

    public List<AnswerInBoardDto> getAnswerInBoard() {
        return answerInBoard;
    }



    public BoardAndAnswer(BoardInfoDto boardInfoDto, List<AnswerInBoardDto> answerInBoard) {
        this.boardInfoDto = boardInfoDto;
        this.answerInBoard = answerInBoard;
    }








}
