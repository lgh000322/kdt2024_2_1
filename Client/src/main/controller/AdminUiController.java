package main.controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminUiController {

    @FXML
    private TableView<String> employeeTable;

    @FXML
    private TableView<?> leaveTable;

    @FXML
    private TableView<?> salaryTable;

    @FXML
    private Button userBoardDeleteBtn;

    @FXML
    private Button userBoardSearchBtn;

    @FXML
    private Button userEditSearchBtn;

    @FXML
    private Button userLeaveSearchBtn;

    @FXML
    private Button userSalarySearchBtn;

    @FXML
    private TableColumn<?, ?> userboardNum;

    @FXML
    private TableColumn<?, ?> userboarddateNum;

    @FXML
    private TableColumn<?, ?> userboardtitleNum;

    @FXML
    private TableColumn<?, ?> userboardwriterNum;

    @FXML
    private TableColumn<String, Integer> usereditNum;

    @FXML
    private TableColumn<String, String> usereditName;

    @FXML
    private TableColumn<String, String> usereditPhone;
    
    @FXML
    private TableColumn<?, ?> usereditDept;

    @FXML
    private TableColumn<?, ?> usereditPosition;

    @FXML
    private TableColumn<?, ?> usereditEmail;

    @FXML
    private TableColumn<?, ?> userleaveCount;

    @FXML
    private TableColumn<?, ?> userleaveName;

    @FXML
    private TableColumn<?, ?> userleaveNum;

    @FXML
    private TableColumn<?, ?> userleavePhone;
    
    @FXML
    private TableColumn<?, ?> userleaveDept;

    @FXML
    private TableColumn<?, ?> userleavePosition;

    @FXML
    private TableColumn<?, ?> usersalaryName;

    @FXML
    private TableColumn<?, ?> usersalaryNum;

    @FXML
    private TableColumn<?, ?> usersalaryPayment;

    @FXML
    private TableColumn<?, ?> usersalaryPhone;
    
    @FXML
    private TableColumn<?, ?> usersalaryDept;

    @FXML
    private TableColumn<?, ?> usersalaryPosition;
    @FXML
    public void initialize() {
        // 컬럼과 Employee 속성 바인딩
        usereditNum.setCellValueFactory(new PropertyValueFactory<>("num"));
        usereditName.setCellValueFactory(new PropertyValueFactory<>("name"));
        usereditPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        usereditDept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        usereditPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        usereditEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        employeeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 더블 클릭 감지
                openAdminUserManagement();
            }
        });
    }
        
        private void openAdminUserManagement() {
            try {
                // FXML 파일 로드
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/admin_ui/AdminUserManagement.fxml"));
                Parent root = loader.load();

                // 새 스테이지 생성
                Stage stage = new Stage();
                stage.setTitle("Admin User Management");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}