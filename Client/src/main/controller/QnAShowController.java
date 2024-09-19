package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.consts.MessageTypeConst;
import main.domain.board.Board;
import main.domain.user.User;
import main.dto.ResponseData;
import main.dto.answer_dto.AnswerInBoardDto;
import main.dto.board_dto.BoardAndAnswer;
import main.dto.board_dto.BoardSaveDto;
import main.dto.user_dto.UserRoleDto;
import main.dto.work_dto.WorkRecord;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

public class QnAShowController {

    @FXML
    private TextArea ReceivedContents;

    @FXML
    private Label ReceivedTitle;

    @FXML
    private Label PostUser;

    @FXML
    private Label PostDate;
    
    @FXML
    private TextField AnswerContents;
    
    @FXML
    private Button AnswerSaveBtn;

    @FXML
    private ListView<AnswerInBoardDto> CommentListView; // 댓글 리스트는 AnswerInBoardDto로 관리
    @FXML
	private ObservableList<AnswerInBoardDto> answerList = FXCollections.observableArrayList();

    public void setBoardAndAnswerData(BoardAndAnswer boardAndAnswer) {
        if (boardAndAnswer != null && boardAndAnswer.getBoardInfoDto() != null) {
            PostUser.setText(boardAndAnswer.getBoardInfoDto().getBoardUserName());
            ReceivedTitle.setText(boardAndAnswer.getBoardInfoDto().getBoardTitle());
            ReceivedContents.setText(boardAndAnswer.getBoardInfoDto().getBoardContents());
            
            // 댓글 목록 설정
            List<AnswerInBoardDto> list = boardAndAnswer.getAnswerInBoard();
            if (list != null && !list.isEmpty()) {
                answerList.clear(); // 기존의 데이터를 초기화
                answerList.addAll(list); // 새 데이터를 추가
                CommentListView.setItems(answerList);
            } else {
                System.out.println("댓글 목록이 없습니다.");
            }
        } else {
            System.out.println("게시글 정보가 없습니다.");
        }
    }
    
    public void handleAnswerSaveBtn() throws IOException {
        // 서버와의 통신 준비
        CommunicationUtils communicationUtils = new CommunicationUtils();
        ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();
        DataOutputStream dos = serverConnectUtils.getDataOutputStream();
        DataInputStream dis = serverConnectUtils.getDataInputStream();

        // 댓글 정보를 설정
        AnswerInBoardDto answerInBoardDto = new AnswerInBoardDto.Builder()
            .answerUserId(UserInfoSavedUtil.getUserId()) // 작성자 ID
            .answerContent(AnswerContents.getText()) // 댓글 내용
            .build();

        // 서버에 보낼 Board, User, BoardAnswer 데이터를 설정
        User user = new User.Builder()
            .userNum(UserInfoSavedUtil.getUserInfo().getUserNum())
            .role(UserInfoSavedUtil.getRole())
            .build();
        
        // 게시글 번호를 정확히 설정해야 합니다.
        Board board = new Board.Builder()
                .boardNum(null) // 게시글 번호 설정
                .build();
        
        // 서버에서 처리할 수 있도록 Board, User, Answer 정보를 하나의 객체로 포장하여 전송
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("board", board);
        requestData.put("writeUser", user);
        requestData.put("boardAnswer", answerInBoardDto);

        // JSON 변환 및 전송
        String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_ANSWER_ADD, requestData);

        try {
            communicationUtils.sendServer(jsonSendStr, dos);
            String jsonReceivedStr = dis.readUTF();

            // 서버로부터의 응답 처리
            ResponseData<AnswerInBoardDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, AnswerInBoardDto.class);
            String messageType = responseData.getMessageType();

            if (messageType.contains("성공")) {
                // 성공 시 댓글 리스트에 추가
                answerList.add(responseData.getData());
                CommentListView.setItems(answerList);
                Platform.runLater(() -> {
                    Stage stage = (Stage) AnswerSaveBtn.getScene().getWindow();
                    stage.close();
                });
            } else {
                System.out.println("댓글 저장 실패: " + messageType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverConnectUtils.close();
        }
    }


}