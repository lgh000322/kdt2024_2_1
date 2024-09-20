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
import main.domain.user.Role;
import main.domain.user.User;
import main.dto.RequestData;
import main.dto.ResponseData;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserJoinDto;
import main.util.CommunicationUtils;
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

	public void handleCheckId() throws IOException {
		if (userId.getText() == null || userId.getText().trim().isEmpty()) {
	        RegisterAlert("중복검사 실패", "아이디를 입력해주세요.");
	        return;
		} else if (userId.getText().length() < 8) {
            RegisterAlert("중복검사 실패", "아이디가 8글자 이상이어야 합니다.");
            return;
        }
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

		String id = userId.getText();

		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_USER_ID_VALIDATION, id);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			ResponseData<UserInfo> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					UserInfo.class);
			String messageType = responseData.getMessageType();
			System.out.println("서버로 부터 받은 messageType= " + messageType);
			if (messageType.contains("성공")) {
				RegisterAlert("사용 불가능(실패)", "이미 존재하는 아이디 입니다. 다른 아이디를 사용해주세요.");
			} else {
				RegisterAlert("사용 가능(성공)", "사용 가능한 아이디 입니다.");
				userId.setEditable(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	public void handleRegisterBtn() throws IOException {
		if (userPwd.getText().length() < 8) {
            RegisterAlert("회원가입 실패", "비밀번호가 8글자 이상이어야 합니다.");
            return;
        } else if (userName.getText() == null || userName.getText().trim().isEmpty()) {
        	RegisterAlert("회원가입 실패", "이름을 입력해주세요.");
        	return;
        } else if (userTel1.getText() == null || userTel1.getText().trim().isEmpty()) {
        	RegisterAlert("회원가입 실패", "전화번호를 입력해주세요.");
        	return;
        } else if (userEmail.getText() == null || userEmail.getText().trim().isEmpty()) {
        	RegisterAlert("회원가입 실패", "이메일을 입력해주세요.");
        	return;
        }
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
		UserJoinDto userJoinDto = new UserJoinDto();
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
		 * 서버에 json 문자열을 보내고 서버에서 json 문자열을 받고, 해당 json 문자열을 객체로 변환 responseData의
		 * messageType의로 회원가입이 정상적으로 이뤄어졌는지 판단.
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
					RegisterAlert("회원가입 실패", "회원가입에 실패했습니다. 입력한 정보를 다시 확인해주세요.");
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	public void handleRegisterCancelBtn() {
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
	}

	private void RegisterAlert(String title, String message) {
		Alert alert = null;
		if (title.contains("실패")) {
			alert = new Alert(AlertType.ERROR);
		} else {
			alert = new Alert(AlertType.INFORMATION);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
