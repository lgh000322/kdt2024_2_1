package org.example.server.dto.board_dto;

import org.example.server.dto.answer_dto.AnswerInBoardDto;

import java.util.List;
import java.util.Map;

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
