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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.domain.board.Board;
import main.domain.board.BoardAnswer;
import main.domain.user.Role;
import main.domain.user.User;
import main.dto.ResponseData;
import main.dto.answer_dto.AnswerInBoardDto;
import main.dto.board_dto.BoardAndAnswer;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

public class QnAShowController {
	

	private Long keyNo;

	private Long boardNum;
	
	private Long userNum;


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

    public void setBoardAndAnswerData(BoardAndAnswer boardAndAnswer,Long boardNum,Long userNum) {
        if (boardAndAnswer != null && boardAndAnswer.getBoardInfoDto() != null) {
            PostUser.setText(boardAndAnswer.getBoardInfoDto().getBoardUserName());
            ReceivedTitle.setText(boardAndAnswer.getBoardInfoDto().getBoardTitle());
            ReceivedContents.setText(boardAndAnswer.getBoardInfoDto().getBoardContents());
            this.boardNum=boardNum;
            this.userNum=userNum;
            
            // 댓글 목록 설정
            List<AnswerInBoardDto> list = boardAndAnswer.getAnswerInBoard();
            answerList.clear();
            for(int i=0;i<list.size();i++) {
            	AnswerInBoardDto answerInBoardDto=list.get(i);
            	answerList.add(answerInBoardDto);
            }
            
            Platform.runLater(()->{
            	CommentListView.setItems(answerList);
            });
        }
    }

    // 댓글 저장 버튼 클릭 시 처리하는 메서드
    public void handleAnswerSaveBtn() throws IOException {
        // 서버와의 통신 준비
        CommunicationUtils communicationUtils = new CommunicationUtils();
        ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();
        DataOutputStream dos = serverConnectUtils.getDataOutputStream();
        DataInputStream dis = serverConnectUtils.getDataInputStream();

        Board board=new Board.Builder()
        		.userNum(userNum)
        		.build();
        
        User user=new User.Builder()
        		.userId(UserInfoSavedUtil.getUserId())
        		.role(UserInfoSavedUtil.getRole())
        		.userNum(UserInfoSavedUtil.getUserInfo().getUserNum())
        		.build();
        
        BoardAnswer boardAnswer=new BoardAnswer.Builder()
        		.contents(AnswerContents.getText())
        		.boardNum(boardNum)
        		.userNum(UserInfoSavedUtil.getUserInfo().getUserNum())
        		.createdDate(LocalDate.now())
        		.build();
        

        // 서버에서 처리할 수 있도록 Board, User, Answer 정보를 하나의 객체로 포장하여 전송
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("board", board);
        requestData.put("writeUser", user);
        requestData.put("boardAnswer", boardAnswer);

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
            	AnswerSaveAlert("댓글 저장 실패", "관리자 또는 게시자만 댓글을 달 수 있습니다.");
                System.out.println("댓글 저장 실패: " + messageType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverConnectUtils.close();
        }
    }
    
    // keyNo를 설정하는 메서드
    public void setKeyNo(Long keyNo) {
        this.keyNo = keyNo;
    }
    
    private void AnswerSaveAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}