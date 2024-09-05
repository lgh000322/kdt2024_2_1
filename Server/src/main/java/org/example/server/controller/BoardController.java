package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.board.Board;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.BoardService;

import java.sql.SQLException;

import static org.example.server.consts.MessageTypeConst.MESSAGE_BOARD_LIST_SEARCH;


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
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;

        switch (requestURL) {
            case MESSAGE_BOARD_LIST_SEARCH -> {
                System.out.println("모든 게시판 조회 실행");

                result = boardService.findAllBoards();
            }
            case MessageTypeConst.MESSAGE_BOARD_ONE_SEARCH -> {
                System.out.println("특정 게시글 조회 실행");
                Long selectedBoardNum = (Long) requestData.getData();
                result = boardService.findOneBoard(selectedBoardNum);
            }
            case MessageTypeConst.MESSAGE_BOARD_ADD -> {
                System.out.println("게시글 작성 실행");
                Board createBoardInfo = (Board) requestData.getData();
                result = boardService.createBoard(createBoardInfo);
            }
            case MessageTypeConst.MESSAGE_BOARD_DELETE -> {
                System.out.println("게시글 삭제 조회 실행");
                Long removeBoardNum = (Long) requestData.getData();
                result = boardService.removeBoard(removeBoardNum);
            }
            case MessageTypeConst.MESSAGE_BOARD_UPDATE -> {
                System.out.println("게시글 수정 실행");
                Board updateBoardInfo = (Board) requestData.getData();
                result = boardService.updateBoard(updateBoardInfo);
            }
        }

        return result;
    }


}
