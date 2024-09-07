package org.example.server.service;

import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.dto.*;
import org.example.server.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    BoardService boardService = BoardService.createOrGetBoardService();

    @Test
    void removeBoard() throws SQLException {
        ResponseData data = boardService.removeBoard(3L);
        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());

    }

    @Test
    void createBoard() throws SQLException {

        // 현재 LocalDateTime 생성
        LocalDate localDateTime = LocalDate.now();



        BoardSaveDto board = new BoardSaveDto();

        board.setContents("고구구ㅜ가ㅜㄴ아룸;ㅣㅇ루");
        board.setCreatedDate(localDateTime);
        board.setTitle("ㅎㅇ?");
        board.setUserNum(16L);

        ResponseData data = boardService.createBoard(board);
        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());

    }

    @Test
    void findBoardByTitle() {
    }

    @Test
    void findAllBoards() throws SQLException {

        String title2 = "ㅎ";
        String title3 = null;

        ResponseData data = boardService.findAllBoards(title2);

        List<BoardFindAllDto> list = (List<BoardFindAllDto>) data.getData();

        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());

        for (BoardFindAllDto board : list) {
            System.out.println("게시글 번호 : " + board.getBoardNum());
            System.out.println("제목 : " + board.getTitle());

            System.out.println("사용자 : " + board.getUserId());
            System.out.println("작성일자 :  " + board.getCreatedDate());
            System.out.println("-----------------------------------------------");

        }
    }

    /*@Test
    void findOneBoard() throws SQLException {
        ResponseData data = boardService.findOneBoard(3L);


        BoardAndAnswer boardAndAnswer = (BoardAndAnswer) data.getData();

        System.out.println("제목 : " + boardAndAnswer.getBoard().getTitle());
        System.out.println("내용 : " + boardAndAnswer.getBoard().getContents());
        System.out.println("사용자 : " + boardAndAnswer.getBoard().getUserNum());
        System.out.println("작성일자 :  " + boardAndAnswer.getBoard().getCreatedDate());
    }*/

    @Test
    void updateBoard() throws SQLException {

        BoardUpdateDto board = new BoardUpdateDto();

        board.setBoardNum(5L);
        board.setContents("ㅇㄻㄴㅇㄻㄴㅇㄻㅇㄴㄹ");
        board.setTitle("sdfksdfklsjdflkajsdklflsd");
        ResponseData responseData = boardService.updateBoard(board);

        ResponseData data = boardService.updateBoard(board);
        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());


    }
}
