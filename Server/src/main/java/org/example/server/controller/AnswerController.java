package org.example.server.controller;

import org.example.server.consts.MessageTypeConst;
import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.AnswerData;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.SalaryAddData;
import org.example.server.service.AnswerService;

import java.sql.SQLException;

public class AnswerController implements Controller {
    private static AnswerController answerController = null;
    private final AnswerService answerService;

    public static AnswerController createOrGetAnswerController() {
        if (answerController == null) {
            answerController = new AnswerController(AnswerService.createOrGetAnswerService());
            System.out.println("AnswerController 싱글톤 생성");
            return answerController;
        }

        System.out.println("AnswerController 싱글톤 반환");
        return answerController;
    }

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }



    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_ANSWER_ADD -> {
                System.out.println("답글추가");
                AnswerData salaryAddData = (AnswerData) requestData.getData(); // 관리자와 대상 유저 정보를 함께 받아옴
                Board board = salaryAddData.getBoard(); //댓글을 작성할 게시판
                User user = salaryAddData.getWriteUser(); //현재 접속한 클라이언트 유저 정보(작성자 혹은 관리자)
                BoardAnswer boardAnswer = salaryAddData.getBoardAnswer(); //작성한 댓글정보

                //해당 게시판을 작성한 유저이거나 관리자만 추가할 수 있게
                if (board.getUserNum().equals(user.getUserNum()) || user.getRole() == Role.ADMIN) {
                    result = answerService.addAnswer(board, boardAnswer, user); // 둘중 한사람이 댓글을 작성
                    } else { //그 외의 사람의 경우 댓글작성 실패
                        System.out.println("해당 게시판의 작성자 혹은 관리자가 아닙니다.");
                        result = new ResponseData("관리자 또는 게시자만 댓글을 달 수 있습니다.", null);
                    }
                }

            case MessageTypeConst.MESSAGE_ANSWER_SEARCH -> {
                System.out.println("댓글조회");
                Board board = (Board)requestData.getData(); //선택한 게시판의 정보를 가져옴

                if(board.getBoardNum()!=null){
                    result = answerService.searchAnswer(board);
                }
                else{
                    System.out.println("해당 게시물 번호가 존재하지않습니다.");
                    result = new ResponseData("해당 게시물이 존재하지 않습니다.",null);
                }
            }

        }

        return result;
    }
}
