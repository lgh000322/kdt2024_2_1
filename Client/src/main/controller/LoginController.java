package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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
import main.domain.user.User;
import main.dto.RequestData;
import main.dto.ResponseData;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserLoginDto;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

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
			CommunicationUtils communicationUtils = new CommunicationUtils();

			ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

			/**
			 * 데이터를 주고받기 위해 stream을 받아옴
			 */
			DataOutputStream dos = serverConnectUtils.getDataOutputStream();
			DataInputStream dis = serverConnectUtils.getDataInputStream();

			/**
			 * json 변환, json에서 객체로 매핑하기 위한 gson 선언
			 */
//			Gson gson = new Gson();

			/**
			 * requestData의 data에 넣어줄 객체를 생성
			 */
			UserLoginDto userLoginDto = new UserLoginDto();
			userLoginDto.setUserId(userId.getText());
			userLoginDto.setPassword(userPwd.getText());
			if (userCheck.isSelected()) {
				userLoginDto.setRole(Role.USER);
			} else if (adminCheck.isSelected()) {
				userLoginDto.setRole(Role.ADMIN);
			} else {
				userLoginDto.setRole(null);
			}

			/**
			 * requestData 생성
//			 */
//			RequestData requestData = new RequestData();
//			requestData.setData(userLoginDto);
//			requestData.setMessageType(MessageTypeConst.MESSAGE_LOGIN);

			String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LOGIN, userLoginDto);

			try {
				communicationUtils.sendServer(jsonSendStr, dos);
				String jsonReceivedStr = dis.readUTF();
				
				ResponseData<UserInfo> responseData=communicationUtils.jsonToResponseData(jsonReceivedStr, UserInfo.class);
				String messageType = responseData.getMessageType();

				if (messageType.contains("성공")) {
					UserInfo userInfo = responseData.getData();
					
					//클라이언트에 유저 정보를 저장해주는 로직 추가
					UserInfoSavedUtil.setUserInfo(userInfo);
					UserInfoSavedUtil.setUserId(userLoginDto.getUserId());
					UserInfoSavedUtil.setRole(userLoginDto.getRole());

					if (userLoginDto.getRole() == Role.USER) {

						Platform.runLater(() -> {
							try {
 								FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/user_ui/UserUi.fxml"));
								Parent loginRoot = fxmlLoader.load();

								UserUiController userUiController = fxmlLoader.getController();
								userUiController.setUserData(userInfo);
								
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
