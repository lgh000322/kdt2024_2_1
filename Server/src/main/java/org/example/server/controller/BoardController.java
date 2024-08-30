package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.BoardService;

public class BoardController implements Controller {

    private static BoardController boardController = null;
    private final BoardService boardService;

    // 의존성 주입.
    public static BoardController createOrGetBoardController() {
        // 보드 컨트롤러가 없으면.
        if (boardController == null) {
            // 보드 컨트롤러에 보드 서비스를 주입한다.
            boardController = new BoardController(BoardService.createOrGetBoardService());
            System.out.println("boardController 싱글톤 생성");
            return boardController;
        }

        // 보드 컨트롤러가 있으면 기존 컨트롤러 반환
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
