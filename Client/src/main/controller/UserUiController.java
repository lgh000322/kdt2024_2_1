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

    @FXML
    private ImageView userImage;

    @FXML
    private Label userName;

    @FXML
    private Label userTel1;

    @FXML
    private Label userEmail;

    @FXML
    private Label userDept;
    
    @FXML
    private Label userPosition;
    
    @FXML
    private Label currentTime;

    @FXML
    private Button deletePost;

    @FXML
    private Button endWork;

    @FXML
    private Button leaveEarly;

    @FXML
    private Button leaveRequest;

    @FXML
    private Button logout;

    @FXML
    private TextField qnaTitle;

    @FXML
    private DatePicker selectMoneyDate;

    @FXML
    private DatePicker selectWorkDate;

    @FXML
    private Button startWork;

    @FXML
    private Button titleSearchBtn;

    @FXML
    private Button updatePost;

    @FXML
    private ComboBox<String> workComboList;

    @FXML
    private Button writePost;
    
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
    
    public void setUserData(UserInfo userInfo) {
    	userName.setText(userInfo.getName());
    	userTel1.setText(userInfo.getTel());
    	userEmail.setText(userInfo.getEmail());
    	userDept.setText(userInfo.getDeptName());
    	userPosition.setText(userInfo.getPositionName());
    }
}
