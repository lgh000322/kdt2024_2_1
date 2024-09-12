package org.example.server.controller;

import com.google.gson.internal.LinkedTreeMap;
import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.Role;
import org.example.server.domain.board.Board;
import org.example.server.domain.user.User;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.AnswerService;
import org.example.server.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class AnswerControllerTest {
    AnswerController answerController;
    AnswerService answerService;

    @BeforeEach
    void setup() {
        // AnswerService와 AnswerController의 싱글톤 인스턴스를 생성
        answerService = AnswerService.createOrGetAnswerService();
        answerController = AnswerController.createOrGetAnswerController();
    }

    @Test
    void 댓글쓰기_테스트() throws SQLException {
        // User 객체 생성 (댓글 작성자)
        User user = new User.Builder()
                .userNum(1L)
                .userId("김가나")
                .password("pass123")
                .name("kimgana")
                .tel("123-456-7890")
                .email("gana@example.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(2L)
                .deptNum(2L)
                .build();

        // Board 객체 생성 (게시물)
        Board exBoard = new Board.Builder()
                .boardNum(1L)
                .title("첫번째 게시물")
                .contents("게시물 내용")
                .createdDate(LocalDate.parse("2024-09-01"))
                .userNum(1L)
                .build();

        // BoardAnswer 객체 생성 (댓글)
        BoardAnswer exBoardAnswer = new BoardAnswer.Builder()
                .contents("첫 번째 게시물에 대한 댓글")
                .boardNum(1L)
                .userNum(1L)  // 댓글 작성자
                .createdDate(LocalDate.now())
                .build();

        // RequestData 생성 (MESSAGE_ANSWER_ADD)
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_ANSWER_ADD);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("board", exBoard);
        data.put("writeUser", user);
        data.put("boardAnswer", exBoardAnswer);

        requestData.setData(data);

        // execute 메서드 실행 (댓글 추가)
        ResponseData responseData = answerController.execute(requestData);

        // 결과 검증
        System.out.println("add answer_Message: " + responseData.getMessageType());
        System.out.println("add answer_Data: " + responseData.getData());
    }

    @Test
    void 댓글조회_테스트() throws SQLException {
        // Board 객체 생성 (조회할 게시물)
        Board exBoard = new Board.Builder()
                .boardNum(1L)
                .title("첫번째 게시물")
                .contents("게시물 내용")
                .createdDate(LocalDate.parse("2024-09-01"))
                .userNum(1L)
                .build();

        // RequestData 생성 (MESSAGE_ANSWER_SEARCH)
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_ANSWER_SEARCH);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("boardNum", exBoard.getBoardNum());

        requestData.setData(data);

        // execute 메서드 실행 (댓글 조회)
        ResponseData responseData = answerController.execute(requestData);

        // 결과 검증
        System.out.println("search answer_Message: " + responseData.getMessageType());
        System.out.println("search answer_Data: " + responseData.getData());
    }

    @Test
    void 관리자가_댓글쓰기_테스트() throws SQLException {
        // 관리자 User 객체 생성
        User adminUser = new User.Builder()
                .userNum(12L)
                .userId("임관리")
                .password("admin")
                .name("admin")
                .tel("000-000-0000")
                .email("admin@example.com")
                .role(Role.ADMIN)
                .remainedLeave(10)
                .positionNum(6L)
                .deptNum(6L)
                .build();

        // Board 객체 생성 (게시물)
        Board exBoard = new Board.Builder()
                .boardNum(1L)
                .title("첫번째 게시물")
                .contents("게시물 내용")
                .createdDate(LocalDate.parse("2024-09-01"))
                .userNum(1L)
                .build();

        // BoardAnswer 객체 생성 (댓글)
        BoardAnswer exBoardAnswer = new BoardAnswer.Builder()
                .contents("관리자가 작성한 댓글")
                .boardNum(1L)
                .userNum(adminUser.getUserNum())
                .createdDate(LocalDate.now())
                .build();

        // RequestData 생성 (MESSAGE_ANSWER_ADD)
        RequestData requestData = new RequestData();
        requestData.setMessageType(MessageTypeConst.MESSAGE_ANSWER_ADD);

        // 데이터를 LinkedTreeMap에 담아서 RequestData에 설정
        LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();
        data.put("board", exBoard);
        data.put("writeUser", adminUser);
        data.put("boardAnswer", exBoardAnswer);

        requestData.setData(data);

        // execute 메서드 실행 (댓글 추가)
        ResponseData responseData = answerController.execute(requestData);

        // 결과 검증
        System.out.println("admin add answer_Message: " + responseData.getMessageType());
        System.out.println("admin add answer_Data: " + responseData.getData());
    }
}