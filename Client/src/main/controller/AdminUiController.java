package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.consts.MessageTypeConst;
import main.domain.mail.MailType;
import main.domain.user.Role;
import main.domain.user.User;
import main.dto.ResponseData;
import main.dto.board_dto.BoardFindAllDto;
import main.dto.board_dto.QnARecord;
import main.dto.leave_dto.ForFindLeaveDto;
import main.dto.leave_dto.ForUpdateLeaveDto;
import main.dto.leave_dto.LeaveLogOfAdminDto;
import main.dto.leave_dto.LeaveRecordOfAdmin;
import main.dto.leave_dto.LeaveStatus;
import main.dto.mail_dto.MailAllDto;
import main.dto.mail_dto.MailRecord;
import main.dto.mail_dto.MailSearchDto;
import main.dto.mail_dto.UserAndEmailDto;
import main.dto.salary_dto.AdminSalaryData;
import main.dto.salary_dto.AdminSalaryRecord;
import main.dto.salary_dto.SalaryAddData;
import main.dto.user_dto.UpdateUserDto;
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
	private Tab qnaTab;
	
	@FXML
	private TextField qnaTitle;
	
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


	/*
	 * 사원관리 테이블 뷰
	 */
	@FXML
	private TableView<UserRecord> employeeTable;
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

	@FXML
	private TextField userEditSearch;;

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
	@FXML
	private TableColumn<LeaveRecordOfAdmin, Boolean> checkStatus;
	@FXML
	private TableColumn<LeaveRecordOfAdmin, String> userId;
	
	
	@FXML
	private TextField userLeaveSearch;
	
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
	private TableColumn<AdminSalaryRecord, String> usersalaryBasic;
	
	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryDept;

	@FXML
	private TableColumn<AdminSalaryRecord, String> usersalaryPosition;

	@FXML
	private Button salarySend;
	
	/*
	 * Q&A관리 테이블 뷰
	 */
	@FXML
	private TableView<QnARecord> qnaRecordTableView;
	
	@FXML
	private TableColumn<QnARecord, Long> qnaNoColumn;
	
	@FXML
	private TableColumn<QnARecord, String> qnaTitleColumn;
	
	@FXML
	private TableColumn<QnARecord, String> qnaPostUserColumn;
	
	@FXML
	private TableColumn<QnARecord, String> qnaDateColumn;
	
	@FXML
	private TableColumn<QnARecord, Long> boardNo;
	
	/*
	 * 메일관리 테이블 뷰
	 */
	@FXML
	private TableView<MailRecord> mailRecordTableView;
	
	@FXML
	private TableColumn<MailRecord, Long> mailNoColumn;
	
	@FXML
	private TableColumn<MailRecord, String> mailReceivedColumn;
	
	@FXML
	private TableColumn<MailRecord, String> mailTitleColumn;
	
	@FXML
	private TableColumn<MailRecord, String> mailReceivedDateColumn;
	
	@FXML
	private ComboBox<String> mailComboList;
	
	
	@FXML
	private Button logoutBtn;

	@FXML
	private Button searchMailTitleBtn;
	
	@FXML
	private Button sendMailBtn;
	
	@FXML
	private Button deleteMailBtn;
	
	@FXML
	private Button titleSearchQnABtn;
	
	@FXML
	private Button deletePostQnABtn;

	@FXML
	private Button userEditSearchBtn;

	@FXML
	private Button userLeaveSearchBtn;

	@FXML
	private Button userSalarySearchBtn;
	
	@FXML
	private Button rejectBtn;
	
	@FXML
	private Button acceptBtn;
	
	/* 현재시간 표시 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@FXML
	private ObservableList<UserRecord> userRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<LeaveRecordOfAdmin> leaveRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<AdminSalaryRecord> salaryRecordList = FXCollections.observableArrayList();
	
	@FXML
	private ObservableList<MailRecord> mailRecordList = FXCollections.observableArrayList();
	
	@FXML
	private ObservableList<QnARecord> qnaRecordList = FXCollections.observableArrayList();
	
	private LeaveRecordOfAdmin selectedLeaveRecord;

	private LocalDate selectedLeaveEndDate;
	private LocalDate selectedLeaveStartDate;
	private Long selectedLeaveNum;
	private String selectedUserId;
	
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

		qnaTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
		
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalDateTime now = LocalDateTime.now();
			currentTime.setText(now.format(formatter));
			
			updateSalarySendButton(); // 버튼 상태 갱신
		}), new KeyFrame(Duration.seconds(1)));

		clock.setCycleCount(Timeline.INDEFINITE);
		clock.play();

		
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
		
		userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
		checkStatus.setCellValueFactory(new PropertyValueFactory<>("checkStatus"));

		// 컬럼과 급여관리
		usersalaryNum.setCellValueFactory(new PropertyValueFactory<>("salaryNum"));
		usersalaryName.setCellValueFactory(new PropertyValueFactory<>("salaryName"));
		usersalaryDept.setCellValueFactory(new PropertyValueFactory<>("salaryDept"));
		usersalaryPosition.setCellValueFactory(new PropertyValueFactory<>("salaryPosition"));
		usersalaryBasic.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
	
	

		try {
			employeeTabClickedMethod();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//휴가테이블 클릭시 update를 위한 정보 가져옴.
		leaveTable.setOnMouseClicked((MouseEvent e) -> {
			LocalDate startDate = null;
			LocalDate endDate = null;
			if(e.getClickCount() == 2 || e.getClickCount() == 1) {  // 한번 클릭하거나 더블 클릭하면. 해당정보들을 가져
				selectedLeaveRecord = leaveTable.getSelectionModel().getSelectedItem();
				
				if (selectedLeaveRecord != null) {
					
					  // 날짜 문자열을 LocalDate로 변환하기 위한 포맷터 정의
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
			        try {
			            // 문자열을 LocalDate로 변환
			           startDate = LocalDate.parse(selectedLeaveRecord.getLeaveStartDate(), formatter);
			           endDate = LocalDate.parse(selectedLeaveRecord.getLeaveEndDate(), formatter);
			            
			        } catch (DateTimeParseException er) {
			            // 문자열이 올바른 날짜 형식이 아닌 경우 예외 처리
			            System.err.println("Invalid date format: " + er.getMessage());
			        }
			        
	
			        
			        selectedLeaveEndDate = endDate;
			        selectedLeaveStartDate = startDate;
			    	selectedLeaveNum = selectedLeaveRecord.getNo();
			    	selectedUserId = selectedLeaveRecord.getUserId();
			        
		        }
			}
		});
		
		
		// 사원관리 컬럼 클릭 이벤트
		employeeTable.setOnMouseClicked((MouseEvent event) -> {
			 if (event.getClickCount() == 2) { // 더블 클릭 확인
				 UserRecord selectedRecord = employeeTable.getSelectionModel().getSelectedItem();
				 
				 if (selectedRecord != null) { // 데이터가 있는 행이 선택되었는지 확인
				
					 Platform.runLater(() -> {
							try {
								 // FXML 파일 로드
				                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Admin_Ui/AdminUserManagement.fxml"));
				                Parent root = loader.load();
				                
				                AdminUserManagementController adminUserManagementController = loader.getController();
				                
				                UserInfo userInfo = new UserInfo();
				                userInfo.setName(selectedRecord.getName());
	                            userInfo.setTel(selectedRecord.getTel());
	                            userInfo.setEmail(selectedRecord.getEmail());
	                            userInfo.setPositionName(selectedRecord.getPosition());
	                            userInfo.setDeptName(selectedRecord.getDept());
				                
				                adminUserManagementController.setUpdateUserInfo(userInfo);
				                
				                
				                // 새로운 스테이지(창) 생성 및 장면 설정
				                Stage stage = new Stage();
				                stage.setTitle("사원 관리");
				                stage.setScene(new Scene(root));
				                stage.show();

							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					 
				 }
				 				 
			 }
		});
		
		// 급여관리 컬럼 클릭 이벤트
		salaryTable.setOnMouseClicked((MouseEvent event) -> {
		    if (event.getClickCount() == 2) { // 더블 클릭 확인
		    	AdminSalaryRecord selectedRecord = salaryTable.getSelectionModel().getSelectedItem();
		        if (selectedRecord != null) { // 데이터가 있는 행이 선택되었는지 확인
		        	
		            try {
		                // FXML 파일 로드
		                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Admin_Ui/AdminSalary.fxml"));
		                Parent root = loader.load();

		                AdminSalaryController adminSalaryController = loader.getController();
		                adminSalaryController.setAdminSalary(selectedRecord);
		                adminSalaryController.setUserNum(selectedRecord.getKeyUserNum());
//		                adminSalaryController.setAdminSalaryRecord(selectedRecord);
		                
		                // 새로운 스테이지(창) 생성 및 장면 설정
		                Stage stage = new Stage();
		                stage.setTitle("급여 관리");
		                stage.setScene(new Scene(root));
		                stage.show();

		            } catch (IOException e) {
		                e.printStackTrace(); // 예외 처리
		            }
		        }
		    }
		});
		
	}
	
	/* 좌측 사용자 정보 표시 */
	public void setUserData(UserInfo userInfo) {
		userName.setText(userInfo.getName());
		userTel1.setText(userInfo.getTel());
		userEmail.setText(userInfo.getEmail());
		userDept.setText(userInfo.getDeptName());
		userPosition.setText(userInfo.getPositionName());
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
		forFindLeaveDto.setUserName(null);
		forFindLeaveDto.setUserId(null);
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
					
					LeaveLogOfAdminDto leaveLogOfAdminDto = list.get(i);
					LeaveRecordOfAdmin leaveRecordOfAdmin = new LeaveRecordOfAdmin(leaveLogOfAdminDto.getLeaveNum(), leaveLogOfAdminDto.getUserName(),
							leaveLogOfAdminDto.getRequestDate(), leaveLogOfAdminDto.getStartDate(),
							leaveLogOfAdminDto.getEndDate(), leaveLogOfAdminDto.getDeptName(),
							leaveLogOfAdminDto.getStatus(), leaveLogOfAdminDto.getRemainedLeave(), leaveLogOfAdminDto.getCheckStatus(), leaveLogOfAdminDto.getUserId());

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
		salaryRecordList.clear();
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
				long no = 1L;
				for (int i = 0; i < list.size(); i++) {
					// 이름이 관리자일경우 스킵
					if(list.get(i).getName().equals("admin")) {
						continue;
					}
					
					
					
					
					AdminSalaryRecord adminSalaryRecord = new AdminSalaryRecord(no++, list.get(i).getSalaryNum(), list.get(i).getName(), list.get(i).getDeptName(),
							list.get(i).getPositionName(), list.get(i).getBasicSalary());
					
					
					/*
					 * AdminSalaryRecord adminSalaryRecord = new AdminSalaryRecord(no,
					 * adminSalaryData.getSalaryNum(), adminSalaryData.getName(),
					 * adminSalaryData.getTel(), adminSalaryData.getDeptName(),
					 * adminSalaryData.getPositionName(), adminSalaryData.getTotalSalary(),
					 * adminSalaryData.getReceivedDate(), adminSalaryData.getLeavePay(),
					 * adminSalaryData.getBasicSalary() ,adminSalaryData.getRemainedLeave(),
					 * adminSalaryData.getBonus());
					 */
					
					
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
	
	// 메일탭이 선택되었을때
	public void mailTabClickedMethod() throws IOException {
		System.out.println("메일탭 클릭 이벤트 발생");
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
		MailSearchDto mailSearchDto = new MailSearchDto();
		mailSearchDto.setEmail(userEmail.getText());
		mailSearchDto.setMailType(MailType.RECEIVED);

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_STORE_SEARCH, mailSearchDto);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<MailAllDto>>() {
			}.getType();
			ResponseData<MailAllDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<MailAllDto> list = (List<MailAllDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					System.out.println("휴가로그 출력 실행");
					MailAllDto mailAllDto = list.get(i);
					Long no = (long) (i + 1);
					MailRecord mailRecord = new MailRecord(no, mailAllDto.getUserEmail(), mailAllDto.getTitle(),
							mailAllDto.getCreatedDate().toString());

					mailRecordList.add(mailRecord);
				}

				Platform.runLater(() -> {
					mailRecordTableView.setItems(mailRecordList);
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

	
	
	
	/* 메일쓰기 버튼 클릭 시, 메일작성 창 띄우기 */
	public void handlesendMailBtn() throws IOException {
		ObservableList<String> emailList = FXCollections.observableArrayList();
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

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SEARCH_ALL_USERNAME_AND_EMAIL,
				null);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<UserAndEmailDto>>() {
			}.getType();
			ResponseData<UserAndEmailDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<UserAndEmailDto> list = (List<UserAndEmailDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					UserAndEmailDto userAndEmailDto = list.get(i);
					String str = userAndEmailDto.getUserEmail();
					emailList.add(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/mail_ui/SendMail.fxml"));
			Parent sendMailRoot = fxmlLoader.load();
			MailController mailController = fxmlLoader.getController();
			mailController.setReceiveUserEmailData(emailList);

			Stage sendMailStage = new Stage();
			sendMailStage.setTitle("메일작성");
			sendMailStage.setScene(new Scene(sendMailRoot));

			sendMailStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 메일삭제 버튼 클릭 시, 메일삭제 처리 로직 */
	public void handledeleteMailBtn() {
	}
	
	/* Q&A 탭에서 검색 버튼 버튼 클릭 시, 입력한 제목명으로 검색 처리 로칙 */
	public void handletitleSearchBtn() throws IOException {
		qnaRecordList.clear();
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
		String title = qnaTitle.getText();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_BOARD_LIST_SEARCH, title);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<BoardFindAllDto>>() {
			}.getType();
			ResponseData<BoardFindAllDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<BoardFindAllDto> list = (List<BoardFindAllDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					System.out.println("QnA게시판 로그 출력 실행");
					BoardFindAllDto boardFindAllDto = list.get(i);
					Long no = Long.valueOf(i + 1);

					QnARecord qnaRecord = new QnARecord(boardFindAllDto.getBoardNum(), no, boardFindAllDto.getTitle(),
					        boardFindAllDto.getUserId(), boardFindAllDto.getCreatedDate());


					qnaRecordList.add(qnaRecord);
				}

				Platform.runLater(() -> {
					qnaRecordTableView.setItems(qnaRecordList);
				});

			}
		} catch (

		IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}
	/* Q&A 글 삭제 버튼 클릭 시, 글 삭제 처리 로칙 */
	public void handledeletePostQnABtn() {
	}
	
	/* 로그아웃 버튼 처리 로직 */
	public void handleLogoutBtn() {
		UserInfoSavedUtil.logout();

		Platform.runLater(() -> {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/login_ui/LoginUi.fxml"));
				Parent loginRoot = fxmlLoader.load();

				// 새 Stage를 생성하고 Scene을 설정합니다.
				Stage loginStage = new Stage();
				loginStage.setTitle("로그인");
				loginStage.setScene(new Scene(loginRoot));

				// 현재 Stage를 가져와서 숨깁니다.
				Stage currentStage = (Stage) userEditSearchBtn.getScene().getWindow();
				currentStage.hide();

				// 새 Stage를 표시합니다.
				loginStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public void hadleLeaveSearchBtn() throws IOException {
		
		leaveRecordList.clear();
		
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
		forFindLeaveDto.setUserName(userLeaveSearch.getText());
		forFindLeaveDto.setUserId(null);
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
					
					LeaveLogOfAdminDto leaveLogOfAdminDto = list.get(i);
					LeaveRecordOfAdmin leaveRecordOfAdmin = new LeaveRecordOfAdmin(leaveLogOfAdminDto.getLeaveNum(), leaveLogOfAdminDto.getUserName(),
							leaveLogOfAdminDto.getRequestDate(), leaveLogOfAdminDto.getStartDate(),
							leaveLogOfAdminDto.getEndDate(), leaveLogOfAdminDto.getDeptName(),
							leaveLogOfAdminDto.getStatus(), leaveLogOfAdminDto.getRemainedLeave(), leaveLogOfAdminDto.getCheckStatus(), leaveLogOfAdminDto.getUserId());

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
	
	public void handleRejectBtn() throws IOException {
		
		leaveRecordList.clear();
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
		ForUpdateLeaveDto forUpdateLeaveDto = new ForUpdateLeaveDto.Builder()
				.userId(selectedUserId)
				.leaveNum(selectedLeaveNum)
				.startDate(selectedLeaveStartDate)
				.endDate(selectedLeaveEndDate)
				.status(LeaveStatus.REJECT)
				.build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LEAVE_EDIT, forUpdateLeaveDto);
		
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
					
					LeaveLogOfAdminDto leaveLogOfAdminDto = list.get(i);
					LeaveRecordOfAdmin leaveRecordOfAdmin = new LeaveRecordOfAdmin(leaveLogOfAdminDto.getLeaveNum(), leaveLogOfAdminDto.getUserName(),
							leaveLogOfAdminDto.getRequestDate(), leaveLogOfAdminDto.getStartDate(),
							leaveLogOfAdminDto.getEndDate(), leaveLogOfAdminDto.getDeptName(),
							leaveLogOfAdminDto.getStatus(), leaveLogOfAdminDto.getRemainedLeave(), leaveLogOfAdminDto.getCheckStatus(), leaveLogOfAdminDto.getUserId());

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
		
	
	
	public void handleAcceptBtn() throws IOException {
		
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
		ForUpdateLeaveDto forUpdateLeaveDto = new ForUpdateLeaveDto.Builder()
				.userId(selectedUserId)
				.leaveNum(selectedLeaveNum)
				.startDate(selectedLeaveStartDate)
				.endDate(selectedLeaveEndDate)
				.status(LeaveStatus.ACCEPT)
				.build();
		
		

		selectedUserId = null;
		selectedLeaveNum = null;
		selectedLeaveStartDate = null;
		selectedLeaveEndDate = null;
		
		
		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LEAVE_EDIT, forUpdateLeaveDto);
		
		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<LeaveLogOfAdminDto>>() {
			}.getType();
			ResponseData<LeaveLogOfAdminDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();
				
			if (messageType.contains("휴가 수락 실패 (신청 일수가 남은 일수를 초과함.)")) {
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("휴가 수락 실패");
		        alert.setHeaderText("휴가 수락 실패");
		        alert.setContentText("휴가 일수를 초과하여 신청하였습니다.");

		        // Alert 창을 띄우고, 사용자가 닫을 때까지 대기
		        alert.showAndWait();
		        
		        // Alert 창이 닫힌 후 실행할 코드
		        System.out.println("Alert 창이 닫혔습니다.");
		        
		        return;
		        
			}
			
			leaveRecordList.clear();
			
			
			if (messageType.contains("성공")) {
				List<LeaveLogOfAdminDto> list = (List<LeaveLogOfAdminDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					
					LeaveLogOfAdminDto leaveLogOfAdminDto = list.get(i);
					LeaveRecordOfAdmin leaveRecordOfAdmin = new LeaveRecordOfAdmin(leaveLogOfAdminDto.getLeaveNum(), leaveLogOfAdminDto.getUserName(),
							leaveLogOfAdminDto.getRequestDate(), leaveLogOfAdminDto.getStartDate(),
							leaveLogOfAdminDto.getEndDate(), leaveLogOfAdminDto.getDeptName(),
							leaveLogOfAdminDto.getStatus(), leaveLogOfAdminDto.getRemainedLeave(), leaveLogOfAdminDto.getCheckStatus(), leaveLogOfAdminDto.getUserId());

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
	
	
	public void handleSalarySend() throws IOException {
	
		CommunicationUtils communicationUtils = new CommunicationUtils();

		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		/**
		 * 데이터를 주고받기 위해 stream을 받아옴
		 */
		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SALARY_PLUS, null);
		

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

	
			ResponseData<SalaryAddData> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,SalaryAddData.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {

				System.out.println("월급 지급 성공");
			
			}
			
		} catch (

		IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}
	
	
	
	public void handleUserEditSearchBtn() throws IOException {
		userRecordList.clear();
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
		String userName = userEditSearch.getText();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SEARCH_ADMIN, userName);
		
		

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<UserInfo>>() {
			}.getType();
			ResponseData<UserInfo> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<UserInfo> list = (List<UserInfo>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					System.out.println("이름과 일치하는 회원 정보 출력.");
					UserInfo userInfo = list.get(i);
			
					UserRecord userRecord = new UserRecord(userInfo.getUserNum(), userInfo.getName(), userInfo.getTel(),
							userInfo.getDeptName(), userInfo.getPositionName(), userInfo.getEmail());
					userRecordList.add(userRecord);
				}

				Platform.runLater(() -> {
					employeeTable.setItems(userRecordList);
				});

			}
		} catch (

		IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}
	
	
	
	private void updateSalarySendButton() {
	    salarySend.setDisable(!isActivationDate());
	}
	
	
	private boolean isActivationDate() {
	    LocalDate today = LocalDate.now();
	    return today.getDayOfMonth() == 10; // 매달 1일에 활성화
	}
}