package main.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import main.dto.answer_dto.AnswerInBoardDto;
import main.dto.board_dto.BoardAndAnswer;
import main.dto.work_dto.WorkRecord;

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
}
