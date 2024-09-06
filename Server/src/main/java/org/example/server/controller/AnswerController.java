package org.example.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.LinkedTreeMap;
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
import java.time.LocalDate;

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

    // Gson 생성 시 LocalDate 직렬화/역직렬화 추가
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> context.serialize(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
            .create();

    @Override
    public ResponseData execute(RequestData requestData) throws SQLException {
        String requestURL = requestData.getMessageType();
        ResponseData result = null;

        switch (requestURL) {
            case MessageTypeConst.MESSAGE_ANSWER_ADD -> {
                System.out.println("답글추가");
                if(requestData.getData() instanceof LinkedTreeMap){ //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();

                    // map에 있는 객체정보들을 각각 객체로 변환 키값을 아래와같이 받아와야함.
                    Board board = gson.fromJson(gson.toJson(map.get("board")), Board.class);
                    User user = gson.fromJson(gson.toJson(map.get("writeUser")), User.class);
                    BoardAnswer boardAnswer = gson.fromJson(gson.toJson(map.get("boardAnswer")), BoardAnswer.class);

                    //해당 게시판을 작성한 유저이거나 관리자만 추가할 수 있게
                    if (board.getUserNum().equals(user.getUserNum()) || user.getRole() == Role.ADMIN) {
                        result = answerService.addAnswer(board, boardAnswer, user); // 둘중 한사람이 댓글을 작성
                        } else { //그 외의 사람의 경우 댓글작성 실패
                            result = new ResponseData("관리자 또는 게시자만 댓글을 달 수 있습니다.", null);
                        }
                    }
                }

            case MessageTypeConst.MESSAGE_ANSWER_SEARCH -> {
                System.out.println("댓글조회");
                if(requestData.getData() instanceof LinkedTreeMap) { //프론트로부터 requestData에 linkedtreemap으로 넘어옴
                    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) requestData.getData();
                    Board board = gson.fromJson(gson.toJson(map), Board.class); //받아올 객체가 하나뿐이므로 map만지정

                    if (board.getBoardNum() != null) {
                        result = answerService.searchAnswer(board);
                    } else {
                        System.out.println("해당 게시물 번호가 존재하지않습니다.");
                        result = new ResponseData("해당 게시물이 존재하지 않습니다.", null);
                    }
                }
            }

        }

        return result;
    }
}
