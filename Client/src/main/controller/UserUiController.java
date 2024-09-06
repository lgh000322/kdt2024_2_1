package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import main.dto.UserInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserUiController {
	/* 좌측 사용자 정보 및 현재시간 창 */
    @FXML
    private ImageView userImage;

    @FXML
    private Label userName = new Label();

    @FXML
    private Label userTel1 = new Label();

    @FXML
    private Label userEmail = new Label();

    @FXML
    private Label userDept = new Label();
    
    @FXML
    private Label userPosition = new Label();
    
    @FXML
    private Label currentTime;

    
    /* 로그아웃 버튼 */
    @FXML
    private Button logout;
    
    
    /* 근태기록 탭 */
    @FXML
    private DatePicker selectWorkDate;

    @FXML
    private ComboBox<String> workComboList;
    
    @FXML
    private Button startWork;

    @FXML
    private Button endWork;

    @FXML
    private Button leaveEarly;

    
    /* 휴가신청 탭 */
    @FXML
    private Button leaveRequest;
    

    /* 급여내역 탭 */
    @FXML
    private DatePicker selectMoneyDate;

    
    /* 메일함 탭 */
    
    
    /* Q&A 탭 */
    @FXML
    private TextField qnaTitle;

    @FXML
    private Button titleSearchBtn;

    @FXML
    private Button writePost;
    
    @FXML
    private Button updatePost;
    
    @FXML
    private Button deletePost;

    /* 현재시간 표시 */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @FXML
    public void initialize() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            currentTime.setText(now.format(formatter));
        }),
        new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        
        workComboList.getItems().addAll("출근", "결근", "조퇴");
    }
    
    /* 좌측 사용자 정보 표시 */
    public void setUserData(UserInfo userInfo) {
    	userName.setText(userInfo.getName());
    	userTel1.setText(userInfo.getTel());
    	userEmail.setText(userInfo.getEmail());
    	userDept.setText(userInfo.getDeptName());
    	userPosition.setText(userInfo.getPositionName());
    }
    
    
}
