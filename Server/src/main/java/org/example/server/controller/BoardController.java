package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.BoardService;

public class BoardController implements Controller {

    private static BoardController boardController = null;
    private final BoardService boardService;

    public static BoardController createOrGetBoardController() {
        if (boardController == null) {
            boardController = new BoardController(BoardService.createOrGetBoardService());
            System.out.println("boardController 싱글톤 생성");
            return boardController;
        }

        System.out.println("boardController 싱글톤 반환");
        return boardController;
    }

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }



    @Override
    public ResponseData execute(RequestData requestData) {
        return null;
    }
}
