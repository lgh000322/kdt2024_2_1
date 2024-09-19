package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.domain.user.Role;
import main.domain.user.User;
import main.dto.ResponseData;
import main.dto.leave_dto.ForFindLeaveDto;
import main.dto.leave_dto.LeaveLogOfAdminDto;
import main.dto.leave_dto.LeaveRecordOfAdmin;
import main.dto.salary_dto.AdminSalaryData;
import main.dto.salary_dto.AdminSalaryRecord;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserRecord;
import main.dto.user_dto.UserRoleDto;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

public class AdminUiController {

	@FXML
	private Tab employeeTab;

	@FXML
	private Tab leaveTab;

	@FXML
	private Tab salaryTab;

	@FXML
	private Tab mailTab;

	@FXML
	private Tab userboardTab;

	// 사원관리 테이블뷰
	@FXML
	private TableView<UserRecord> employeeTable;

	/* 사원관리 테이블뷰 컬럼 */
	@FXML
	private TableColumn<UserRecord, Long> usereditNum;
	@FXML
	private TableColumn<UserRecord, String> usereditName;
	@FXML
	private TableColumn<UserRecord, String> usereditPhone;
	@FXML
	private TableColumn<UserRecord, String> usereditDept;
	@FXML
	private TableColumn<UserRecord, String> usereditPosition;
	@FXML
	private TableColumn<UserRecord, String> usereditEmail;

	/*
	 * 휴가관리 테이블 뷰
	 */
	@FXML
	private TableView<LeaveRecordOfAdmin> leaveTable;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, Long> leaveNumColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> userNameColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> requestDateColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> startDateColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> endDateColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> deptNameColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, Boolean> statusColumn;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, Integer> userleaveCount;

	/*
	 * 급여관리 테이블 뷰
	 */
	@FXML
	private TableView<AdminSalaryRecord> salaryTable;

	@FXML
	private TableColumn<AdminSalaryRecord, Long> usersalaryNum;

	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryName;

	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryPhone;

	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryDept;

	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryPosition;

	@FXML
	private TableColumn<AdminSalaryRecord, Integer> usersalaryPayment;

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
	private ObservableList<UserRecord> userRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<LeaveRecordOfAdmin> leaveRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<AdminSalaryRecord> salaryRecordList = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		/*
		 * 
		 * 
		 * employeeTable.setOnMouseClicked(event -> { if (event.getClickCount() == 2) {
		 * // 더블 클릭 감지 openAdminUserManagement(); } });
		 */

		// 사원관리 탭이 선택되었을 때 이벤트 추가
		employeeTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("사원관리 탭이 선택됨");
				try {
					employeeTabClickedMethod();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				leaveRecordList.clear();

			}
		});

		// 휴가신청 탭이 선택되었을 때 이벤트 추가
		leaveTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) { // Tab 2가 선택되었을 때
				System.out.println("휴가신청 탭이 선택됨");
				try {
					leaveTabClickedMethod();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				userRecordList.clear();

			}
		});

		salaryTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) { // Tab 2가 선택되었을 때
				System.out.println("급여관리 탭이 선택됨");
				try {
					salaryTabClickedMethod();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				userRecordList.clear();
				leaveRecordList.clear();

			}
		});

		mailTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) { // Tab 2가 선택되었을 때
				System.out.println("메일 탭이 선택됨");
				try {
					leaveTabClickedMethod();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				leaveRecordList.clear();
				userRecordList.clear();

			}
		});

		userboardTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) { // Tab 2가 선택되었을 때
				System.out.println("q&a 탭이 선택됨");
				try {
					leaveTabClickedMethod();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				leaveRecordList.clear();
				userRecordList.clear();

			}
		});

		// 컬럼과 Employee 속성 바인딩
		usereditNum.setCellValueFactory(new PropertyValueFactory<>("num"));
		usereditName.setCellValueFactory(new PropertyValueFactory<>("name"));
		usereditPhone.setCellValueFactory(new PropertyValueFactory<>("tel"));
		usereditDept.setCellValueFactory(new PropertyValueFactory<>("dept"));
		usereditPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		usereditEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		// 컬럼과 휴가관리
		leaveNumColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
		userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("leaveRequestDate"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<>("leaveStartDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<>("leaveEndDate"));
		deptNameColumn.setCellValueFactory(new PropertyValueFactory<>("deptName"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("leaveAcceptStatus"));
		userleaveCount.setCellValueFactory(new PropertyValueFactory<>("remainedLeave"));

		// 컬럼과 급여관리
		usersalaryNum.setCellValueFactory(new PropertyValueFactory<>("salaryNum"));
		usersalaryName.setCellValueFactory(new PropertyValueFactory<>("salaryName"));
		usersalaryPhone.setCellValueFactory(new PropertyValueFactory<>("salaryPhone"));
		usersalaryDept.setCellValueFactory(new PropertyValueFactory<>("salaryDept"));
		usersalaryPosition.setCellValueFactory(new PropertyValueFactory<>("salaryPosition"));
		usersalaryPayment.setCellValueFactory(new PropertyValueFactory<>("salaryPayment"));

		try {
			employeeTabClickedMethod();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 모든 사원 조회(관리자)
	private void employeeTabClickedMethod() throws IOException {
		System.out.println("사원 관리 메소드 실행");

		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SEARCH_ALL_BYADMIN, null);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<UserInfo>>() {
			}.getType();
			ResponseData<UserInfo> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<UserInfo> list = (List<UserInfo>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					UserInfo userInfo = list.get(i);
					Long no = Long.valueOf(i + 1);
					UserRecord userRecord = new UserRecord(no, userInfo.getName(), userInfo.getTel(),
							userInfo.getDeptName(), userInfo.getPositionName(), userInfo.getEmail());
					userRecordList.add(userRecord);
				}

				Platform.runLater(() -> {
					try {
						employeeTable.setItems(userRecordList);
					} catch (Exception e) {
						e.printStackTrace(); // UI 업데이트 중 예외 발생 시 처리
					}
				});

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}

	}

	private void leaveTabClickedMethod() throws IOException {
		System.out.println("휴가탭 클릭 이벤트 발생");
		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		/**
		 * 데이터를 주고받기 위해 stream을 받아옴
		 */
		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		/**
		 * requestData의 data에 넣어줄 객체를 생성
		 */
		ForFindLeaveDto forFindLeaveDto = new ForFindLeaveDto();
		forFindLeaveDto.setUserName(UserInfoSavedUtil.getUserInfo().getName());
		forFindLeaveDto.setUserId(UserInfoSavedUtil.getUserId());
		forFindLeaveDto.setUserRoleDto(UserRoleDto.ADMIN);

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LEAVE_SEARCH, forFindLeaveDto);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<LeaveLogOfAdminDto>>() {
			}.getType();
			ResponseData<LeaveLogOfAdminDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<LeaveLogOfAdminDto> list = (List<LeaveLogOfAdminDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					Long no = Long.valueOf(i + 1);
					LeaveLogOfAdminDto leaveLogOfAdminDto = list.get(i);
					LeaveRecordOfAdmin leaveRecordOfAdmin = new LeaveRecordOfAdmin(no, leaveLogOfAdminDto.getUserName(),
							leaveLogOfAdminDto.getRequestDate(), leaveLogOfAdminDto.getStartDate(),
							leaveLogOfAdminDto.getEndDate(), leaveLogOfAdminDto.getDeptName(),
							leaveLogOfAdminDto.getStatus(), leaveLogOfAdminDto.getRemainedLeave());

					leaveRecordList.add(leaveRecordOfAdmin);

				}

				Platform.runLater(() -> {
					leaveTable.setItems(leaveRecordList);
				});

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}

	}

	private void salaryTabClickedMethod() throws IOException {
		System.out.println("급여탭 클릭 이벤트 발생");
		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		/**
		 * 데이터를 주고받기 위해 stream을 받아옴
		 */
		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		/**
		 * requestData의 data에 넣어줄 객체를 생성
		 */
		User user = new User.Builder().userId(UserInfoSavedUtil.getUserId()).role(Role.ADMIN).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SALARY_SEARCH, user);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<AdminSalaryData>>() {
			}.getType();
			ResponseData<AdminSalaryData> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<AdminSalaryData> list = (List<AdminSalaryData>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					Long no = Long.valueOf(i + 1);
					AdminSalaryData adminSalaryData = list.get(i);
					AdminSalaryRecord adminSalaryRecord = new AdminSalaryRecord(adminSalaryData.getSalaryNum(), no,
							adminSalaryData.getName(), adminSalaryData.getTel(), adminSalaryData.getDeptName(),
							adminSalaryData.getPositionName(), adminSalaryData.getTotalSalary());

					salaryRecordList.add(adminSalaryRecord);
				}

				Platform.runLater(() -> {
					salaryTable.setItems(salaryRecordList);
				});

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}

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