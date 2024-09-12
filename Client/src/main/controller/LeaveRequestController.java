package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.consts.MessageTypeConst;
import main.dto.ResponseData;
import main.dto.leave_dto.ForRequestLeaveDto;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

public class LeaveRequestController {
    @FXML
    private DatePicker leaveStartDate;
    
    @FXML
    private DatePicker leaveEndDate;
    
    @FXML
    private Button leaveRequestBtn;
    
    @FXML
    public void initialize() {
        // 현재 날짜 이후로만 선택할 수 있도록 DatePicker 제한 설정
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        // 현재 날짜보다 이전 날짜는 비활성화
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d3d3d3;"); // 비활성화된 날짜를 시각적으로 구분
                        }
                    }
                };
            }
        };

        // DatePicker에 날짜 제한 설정 적용
        leaveStartDate.setDayCellFactory(dayCellFactory);
        leaveEndDate.setDayCellFactory(dayCellFactory);
    }
    
    public void handleleaveRequestBtn() throws IOException {
        // DatePicker에서 선택된 시작일과 종료일 가져오기
        LocalDate startDate = leaveStartDate.getValue();
        LocalDate endDate = leaveEndDate.getValue();

        if (startDate != null && endDate != null) {
            // 선택된 날짜를 문자열로 변환하여 출력 (필요에 따라 서버로 전송)
            System.out.println("휴가 시작일: " + startDate);
            System.out.println("휴가 종료일: " + endDate);

            // 서버에 데이터를 전송하는 로직 추가 가능
            sendLeaveRequest(startDate, endDate);
        } else {
           Platform.runLater(() -> {
            LeaveFailAlert("휴가신청 실패", "날짜를 모두 선택해주세요.");
         });
        }
    }
    
    private void sendLeaveRequest(LocalDate startDate, LocalDate endDate) throws IOException {
        // 서버로 휴가 신청 데이터를 전송하는 로직
        CommunicationUtils communicationUtils = new CommunicationUtils();
        ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

        DataOutputStream dos = serverConnectUtils.getDataOutputStream();
        DataInputStream dis = serverConnectUtils.getDataInputStream();

        // 휴가 신청 데이터 (사용자의 휴가 신청 데이터를 생성)
        ForRequestLeaveDto forRequestLeaveDto = new ForRequestLeaveDto.Builder()
                .requestDate(LocalDate.now())  // 신청일을 현재 날짜로 설정
                .startDate(startDate)          // DatePicker에서 선택한 시작일을 전달
                .endDate(endDate)              // DatePicker에서 선택한 종료일을 전달
                .userId(UserInfoSavedUtil.getUserId())  // 사용자 ID 설정
                .build();  // 빌더로 객체 생성

        // 객체를 JSON으로 변환하여 서버에 전송
        String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LEAVE_REQUEST, forRequestLeaveDto);

        try {
            // 서버에 데이터 전송
            communicationUtils.sendServer(jsonSendStr, dos);
            String jsonReceivedStr = dis.readUTF();

            // 서버로부터 응답 처리
            ResponseData<ForRequestLeaveDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, ForRequestLeaveDto.class);
            String messageType = responseData.getMessageType();
            if (messageType.contains("성공")) {
                System.out.println("휴가 신청이 성공적으로 처리되었습니다.");
                Stage stage = (Stage) leaveRequestBtn.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("휴가 신청 중 오류가 발생했습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverConnectUtils.close();
        }
    }
    private void LeaveFailAlert(String title, String message) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
   }
}
