package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.domain.user.User;
import main.dto.RequestData;
import main.dto.ResponseData;
import main.dto.UserJoinDto;
import main.util.ServerConnectUtils;

public class RegisterController {

	@FXML
	private TextField userId;

	@FXML
	private Button checkId;

	@FXML
	private PasswordField userPwd;

	@FXML
	private TextField userName;

	@FXML
	private TextField userTel1;

	@FXML
	private TextField userEmail;

	@FXML
	private Button registerBtn;

	public void handleCheckId() {

	}

	public void handleRegisterBtn() throws IOException {
	    /**
	     * 서버랑 연결
	     */
	    ServerConnectUtils serverConnectUtils = new ServerConnectUtils();
	    serverConnectUtils.connect();

	    /**
	     * 데이터를 주고받기 위해 stream을 받아옴
	     */
	    DataOutputStream dos = serverConnectUtils.getDataOutputStream();
	    DataInputStream dis = serverConnectUtils.getDataInputStream();

	    /**
	     * json 변환, json에서 객체로 매핑하기 위한 gson 선언
	     */
	    Gson gson = new Gson();

	    /**
	     * requestData의 data에 넣어줄 객체를 생성
	     */
	    UserJoinDto userJoinDto=new UserJoinDto();
	    userJoinDto.setUserId(userId.getText());
	    userJoinDto.setPassword(userPwd.getText());
	    userJoinDto.setName(userName.getText());
	    userJoinDto.setTel(userTel1.getText());
	    userJoinDto.setEmail(userEmail.getText());
	    
	    /**
	     * requestData 생성
	     */
	    RequestData requestData = new RequestData();
	    requestData.setData(userJoinDto);
	    requestData.setMessageType(MessageTypeConst.MESSAGE_JOIN);

	    /**
	     * 서버에 json 문자열을 보내고
	     * 서버에서 json 문자열을 받고, 해당 json 문자열을 객체로 변환
	     * responseData의 messageType의로 회원가입이 정상적으로 이뤄어졌는지 판단.
	     */
	    String jsonSendStr = gson.toJson(requestData);
	    try {
	        dos.writeUTF(jsonSendStr);
	        dos.flush();
	        String jsonReceivedStr = dis.readUTF();
	        ResponseData responseData = gson.fromJson(jsonReceivedStr, ResponseData.class);
	        String messageType = responseData.getMessageType();
	        
	        if (messageType.contains("성공")) {
	            Platform.runLater(() -> {
	                try {
	                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/login_ui/LoginUi.fxml"));
	                    Parent loginRoot = fxmlLoader.load();

	                    Stage loginStage = new Stage();
	                    loginStage.setTitle("로그인");
	                    loginStage.setScene(new Scene(loginRoot));

	                    Stage currentStage = (Stage) registerBtn.getScene().getWindow();
	                    currentStage.hide();

	                    loginStage.show();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });
	        } else {
	            Platform.runLater(() -> {
	                RegisterFailAlert("회원가입 실패", "회원가입에 실패했습니다. 입력한 정보를 다시 확인해주세요.");
	            });
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        serverConnectUtils.close();
	    }
	}

	private void RegisterFailAlert(String title, String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	
}
