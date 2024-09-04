package org.example.server.service;

import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.dto.BoardAndAnswer;
import org.example.server.dto.ResponseData;
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



        Board board = new Board.Builder()
                .title("rrr")
                .contents("rrrr")
                .userNum(16L)
                .createdDate(localDateTime)
                .build();


        ResponseData data = boardService.createBoard(board);
        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());

    }

    @Test
    void findBoardByTitle() {
    }

    @Test
    void findAllBoards() throws SQLException {
        ResponseData data = boardService.findAllBoards();

        List<Board> list = (List<Board>) data.getData();

        System.out.println("메세지 타입: " + data.getMessageType() + ", 데이터: " + data.getData());

        for (Board board : list) {
            System.out.println("게시글 번호 : " + board.getBoardNum());
            System.out.println("제목 : " + board.getTitle());
            System.out.println("내용 : " + board.getContents());
            System.out.println("사용자 : " + board.getUserNum());
            System.out.println("작성일자 :  " + board.getCreatedDate());
        }
    }

    @Test
    void findOneBoard() throws SQLException {
        ResponseData data = boardService.findOneBoard(3L);


        BoardAndAnswer boardAndAnswer = (BoardAndAnswer) data.getData();

        System.out.println("제목 : " + boardAndAnswer.getBoard().getTitle());
        System.out.println("내용 : " + boardAndAnswer.getBoard().getContents());
        System.out.println("사용자 : " + boardAndAnswer.getBoard().getUserNum());
        System.out.println("작성일자 :  " + boardAndAnswer.getBoard().getCreatedDate());
    }

    @Test
    void updateBoard() throws SQLException {

        Board board = new Board.Builder()
                .boardNum(4L)
                .title("고구마감자먹자")
                .contents("안먹어 ㅅㅂ")
                .build();

        ResponseData responseData = boardService.updateBoard(board);



    }
}