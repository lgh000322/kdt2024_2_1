package org.example.server.dto;

import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;

import java.util.List;

public class BoardAndAnswer {
    private Board board;
    private List<BoardAnswer> answers;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<BoardAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<BoardAnswer> answers) {
        this.answers = answers;
    }

    public BoardAndAnswer(Board board, List<BoardAnswer> answers) {
        this.board = board;
        this.answers = answers;
    }

}
