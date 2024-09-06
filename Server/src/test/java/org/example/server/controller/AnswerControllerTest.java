package org.example.server.controller;

import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.Role;
import org.example.server.domain.board.Board;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.service.AnswerService;
import org.example.server.service.WorkService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class AnswerControllerTest {
    AnswerService answerService = AnswerService.createOrGetAnswerService();

    /*@Test
    void 댓글쓰기() throws SQLException {

        User user = new User.Builder()
                .userNum(1L)
                .userId("김가나") //DB에 name이랑 바꿔 설정함
                .password("pass123")
                .name("kimgana")
                .tel("123-456-7890")
                .email("gana@example.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(2L)
                .deptNum(2L)
                .build();

        User user2 = new User.Builder()
                .userNum(2L)
                .userId("이다라") //DB에 name이랑 바꿔 설정함
                .password("pass456")
                .name("leedara")
                .tel("234-567-8901")
                .email("dara@example.com")
                .role(Role.USER)
                .remainedLeave(18)
                .positionNum(1L)
                .deptNum(1L)
                .build();

        BoardAnswer exBoardanswer = new BoardAnswer.Builder()
                .contents("usernum 2의 답글입니다")
                .boardNum(1L)
                .userNum(1L)
                .createdDate(LocalDate.now())
                .build();

        Board exBoard = new Board.Builder()
                .boardNum(1L)
                .title("첫번째 게시물")
                .contents("게시물 내용")
                .createdDate(LocalDate.parse("2024-09-01"))
                .userNum(1L)
                .build();

        //게시물에 답글등록
        ResponseData responseData = answerService.addAnswer(exBoard,exBoardanswer, user);

        // 결과를 출력
        System.out.println("answer_Message: " + responseData.getMessageType());
        System.out.println("answer_Data: " + responseData.getData());
    }*/
    

    @Test
    void 댓글조회() throws SQLException{
        Board exBoard = new Board.Builder()
                .boardNum(1L)
                .title("첫번째 게시물")
                .contents("게시물 내용")
                .createdDate(LocalDate.parse("2024-09-01"))
                .userNum(1L)
                .build();

        //게시물에 답글등록 boardNum으로 조회함
        ResponseData responseData = answerService.searchAnswer(exBoard);

        // 결과를 출력
        System.out.println("answer_Message: " + responseData.getMessageType());
        System.out.println("answer_Data: " + responseData.getData());
    }

}