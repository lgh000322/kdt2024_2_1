package org.example.server.dto.board_dto;

import org.example.server.dto.answer_dto.AnswerInBoardDto;

import java.util.List;

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
