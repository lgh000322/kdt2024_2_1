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

    // 게시글 정보를 UI에 설정하는 메소드
    public void setBoardAndAnswerData(BoardAndAnswer boardAndAnswer) {
        PostUser.setText(boardAndAnswer.getBoardInfoDto().getBoardUserName());
        ReceivedTitle.setText(boardAndAnswer.getBoardInfoDto().getBoardTitle());
        ReceivedContents.setText(boardAndAnswer.getBoardInfoDto().getBoardContents());
        List<AnswerInBoardDto> List = boardAndAnswer.getAnswerInBoard();
        for(int i=0; i<List.size(); i++) {
        	AnswerInBoardDto answerInBoardDto = List.get(i);
        	answerList.add(answerInBoardDto);
        }
        CommentListView.setItems(answerList);
    }
}
