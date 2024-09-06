package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.domain.user.Role;
import main.dto.RequestData;
import main.dto.ResponseData;
import main.dto.UserLoginDto;

import main.util.ServerConnectUtils;

public class LoginController {

	@FXML
	private CheckBox adminCheck;

	@FXML
	private CheckBox userCheck;

	@FXML
	private Button loginBtn;

	@FXML
	private Button registerBtn;

	@FXML
	private TextField userId;

	@FXML
	private PasswordField userPwd;

	@FXML
	public void initialize() {
		adminCheck.setOnAction(event -> {
			if (adminCheck.isSelected()) {
				userCheck.setSelected(false);
			}
		});

		userCheck.setOnAction(event -> {
			if (userCheck.isSelected()) {
				adminCheck.setSelected(false);
			}
		});
	}

	public void handleLoginBtn() throws IOException {
		if ((userId.getText() == null || userId.getText().equals(""))
				|| (userPwd.getText() == null || userPwd.getText().equals(""))) {
			Platform.runLater(() -> {
				LoginFailAlert("로그인 실패", "로그인 정보를 입력해주세요.");
			});
		} else {
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
		    UserLoginDto userLoginDto = new UserLoginDto();
		    userLoginDto.setUserId(userId.getText());
		    userLoginDto.setPassword(userPwd.getText());
		    if (userCheck.isSelected()) {
		    	userLoginDto.setRole(Role.USER);
		    } else {
		    	userLoginDto.setRole(Role.ADMIN);
		    }
		    
		    /**
		     * requestData 생성
		     */
		    RequestData requestData = new RequestData();
		    requestData.setData(userLoginDto);
		    requestData.setMessageType(MessageTypeConst.MESSAGE_LOGIN);
		    
		    String jsonSendStr = gson.toJson(requestData);
		    
		    try {
		        dos.writeUTF(jsonSendStr);
		        dos.flush();
		        String jsonReceivedStr = dis.readUTF();
		        ResponseData responseData = gson.fromJson(jsonReceivedStr, ResponseData.class);
		        String messageType = responseData.getMessageType();
		        // TypeToken을 사용하여 Map 타입으로 변환
		        Type type = new TypeToken<Map<String, Object>>() {}.getType();
		        // responseData의 데이터를 Map으로 파싱
		        Map<String, Object> map = gson.fromJson(gson.toJson(responseData.getData()), type);
		        	
	        	String userId = (String)map.get("userId");
	        	String password= (String)map.get("userPwd");
		        	
		        if (messageType.contains("성공")) {
		        	if (userLoginDto.getRole() == Role.USER) {
		        		Platform.runLater(() -> {
			                try {
			                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/user_ui/UserUi.fxml"));
			                    Parent loginRoot = fxmlLoader.load();

			                    Stage loginStage = new Stage();
			                    loginStage.setTitle("인사 시스템 (사용자)");
			                    loginStage.setScene(new Scene(loginRoot));

			                    Stage currentStage = (Stage) loginBtn.getScene().getWindow();
			                    currentStage.hide();

			                    loginStage.show();
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
			            });
		        	} else {
		        		Platform.runLater(() -> {
			                try {
			                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/admin_ui/AdminUi.fxml"));
			                    Parent loginRoot = fxmlLoader.load();

			                    Stage loginStage = new Stage();
			                    loginStage.setTitle("인사 시스템 (관리자)");
			                    loginStage.setScene(new Scene(loginRoot));

			                    Stage currentStage = (Stage) loginBtn.getScene().getWindow();
			                    currentStage.hide();

			                    loginStage.show();
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
			            });
		        	}
		        } else {
		            Platform.runLater(() -> {
		                LoginFailAlert("로그인 실패", "입력하신 정보가 맞지 않습니다. 다시 입력해주세요.");
		            });
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        serverConnectUtils.close();
		    }
		}
	}

	public void handleRegisterBtn() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/login_ui/RegisterUi.fxml"));
			Parent registerRoot = fxmlLoader.load();

			Stage registerStage = new Stage();
			registerStage.setTitle("회원가입");
			registerStage.setScene(new Scene(registerRoot));

			Stage currentStage = (Stage) registerBtn.getScene().getWindow();
			currentStage.hide();

			registerStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void LoginFailAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
