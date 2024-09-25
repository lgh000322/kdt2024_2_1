package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.dto.ResponseData;
import main.dto.salary_dto.AdminSalaryRecord;
import main.dto.salary_dto.BonusDto;
import main.dto.user_dto.UpdateUserDto;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserRoleDto;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;

public class AdminSalaryController {

	@FXML
	private ImageView userImage;
	@FXML
	private Label userName;

	@FXML
	private Label userDept;

	@FXML
	private Label userPosition;

	@FXML
	private Label userbasicSalary;

	@FXML
	private Button saveBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField userBonus;

	private Long userNum;

	@FXML
	public void initialize() {
		File file = new File("src/UserImage/userImage.jpg");
		if (file.exists()) {
			Image image = new Image(file.toURI().toString());
			userImage.setImage(image);
		} else {
			System.out.println("이미지 로딩 오류");
		}

		// 저장 버튼을 클릭시 실행.
		saveBtn.setOnAction(event -> {
			System.out.println("사원 성과급 추가 실행.");

			CommunicationUtils communicationUtils = new CommunicationUtils();
			ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

			DataOutputStream dos = serverConnectUtils.getDataOutputStream();
			DataInputStream dis = serverConnectUtils.getDataInputStream();

			int Bonus = Integer.parseInt(userBonus.getText());
			int totalSalary = Bonus + Integer.parseInt(userbasicSalary.getText());

			BonusDto bonusDto = new BonusDto();
			bonusDto.setTotalSalary(totalSalary);
			bonusDto.setUsrRole(UserRoleDto.ADMIN);
			bonusDto.setUserNum(userNum);

			String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SALARY_EDIT, bonusDto);

			try {
				communicationUtils.sendServer(jsonSendStr, dos);
				String jsonReceivedStr = dis.readUTF();

				ResponseData<BonusDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
						BonusDto.class);
				String messageType = responseData.getMessageType();

				if (messageType.contains("성공")) {
					try {
						System.out.println("보너스 추가 성공");
						serverConnectUtils.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Stage stage = (Stage) saveBtn.getScene().getWindow();
					stage.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverConnectUtils.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Stage stage = (Stage) saveBtn.getScene().getWindow();
				stage.close();
			}
		});

		// Set an event handler for the cancel button to close the window
		cancelBtn.setOnAction(event -> {
			// Get the current stage and close it
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		});
	}

	/*
	 * public void setAdminSalaryRecord(AdminSalaryRecord adminSalaryRecord) {
	 * 
	 * this.adminSalaryRecord = new AdminSalaryRecord(null,
	 * adminSalaryRecord.getSalaryNum(), adminSalaryRecord.getSalaryName(),
	 * adminSalaryRecord.getSalaryDept(),
	 * adminSalaryRecord.getSalaryPosition(),adminSalaryRecord.getBasicSalary()); }
	 */

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	// 사용자 부서 직책 월급 연차 수당. 설정
	public void setAdminSalary(AdminSalaryRecord adminSalaryRecord) {
		userName.setText(adminSalaryRecord.getSalaryName());
		userDept.setText(adminSalaryRecord.getSalaryDept());
		userPosition.setText(adminSalaryRecord.getSalaryPosition());
		userbasicSalary.setText(String.valueOf(adminSalaryRecord.getBasicSalary()));
	}

}
