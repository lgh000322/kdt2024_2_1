package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.example.server.adapter.LocalDateTypeAdapter;
import org.example.server.adapter.LocalTimeTypeAdapter;
import org.example.server.consts.MessageTypeConst;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.board_dto.BoardSaveDto;
import org.example.server.dto.board_dto.BoardUpdateDto;
import org.example.server.service.BoardService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();

        switch (requestURL) {
            case MESSAGE_BOARD_LIST_SEARCH -> {
                System.out.println("모든 게시판 조회 실행");

                if (requestData.getData() instanceof String) {
                    String title = (String) requestData.getData();
                    result = boardService.findAllBoards(title);
                }



            }
            case MessageTypeConst.MESSAGE_BOARD_ONE_SEARCH -> {
                System.out.println("특정 게시글 조회 실행");
                if (requestData.getData() instanceof Double) {
                    Double num = (Double) requestData.getData();
                    Long boardNum = num.longValue(); // 소수점 이하가 버려짐
                    result = boardService.findOneBoard(boardNum);
                } else {
                    result = new ResponseData("잘못된 데이터 타입", null);
                    // 예외 처리 또는 로깅
                    System.out.println("잘못된 데이터 타입입니다.");
                }
            }
            case MessageTypeConst.MESSAGE_BOARD_ADD -> {
                System.out.println("게시글 작성 실행");

                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    BoardSaveDto boardSaveDto = gson.fromJson(gson.toJson(map), BoardSaveDto.class);

                    result = boardService.createBoard(boardSaveDto);
                }

            }
            case MessageTypeConst.MESSAGE_BOARD_DELETE -> {
                System.out.println("게시글 삭제 조회 실행");
                if (requestData.getData() instanceof Long) {
                    Long removeBoardNum = (Long) requestData.getData();
                    result = boardService.removeBoard(removeBoardNum);
                } else {
                    result = new ResponseData("잘못된 데이터 타입", null);
                    // 예외 처리 또는 로깅
                    System.out.println("잘못된 데이터 타입입니다.");
                }

            }
            case MessageTypeConst.MESSAGE_BOARD_UPDATE -> {
                System.out.println("게시글 수정 실행");
                if (requestData.getData() instanceof LinkedTreeMap) {
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    BoardUpdateDto boardUpdateDto = gson.fromJson(gson.toJson(map), BoardUpdateDto.class);
                    result = boardService.updateBoard(boardUpdateDto);
                } else {
                    result = new ResponseData("잘못된 데이터 타입", null);
                    // 예외 처리 또는 로깅
                    System.out.println("잘못된 데이터 타입입니다.");
                }
            }
        }

        return result;
    }


}
