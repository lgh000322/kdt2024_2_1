package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.reflect.TypeToken;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.consts.MessageTypeConst;
import main.domain.board.Board;
import main.domain.mail.Mail;
import main.domain.mail.MailType;
import main.domain.user.Role;
import main.domain.user.User;
import main.domain.work_log.Status;
import main.domain.work_log.WorkLog;
import main.dto.ResponseData;
import main.dto.board_dto.BoardAndAnswer;
import main.dto.board_dto.BoardDelDto;
import main.dto.board_dto.BoardFindAllDto;
import main.dto.board_dto.QnARecord;
import main.dto.leave_dto.ForFindLeaveDto;
import main.dto.leave_dto.LeaveLogOfUserDto;
import main.dto.leave_dto.LeaveRecord;
import main.dto.mail_dto.MailAllDto;
import main.dto.mail_dto.MailDeleteDto;
import main.dto.mail_dto.MailRecord;
import main.dto.mail_dto.MailSearchDto;
import main.dto.mail_dto.UserAndEmailDto;
import main.dto.salary_dto.SalaryRecord;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserRoleDto;
import main.dto.user_dto.UserSalaryData;
import main.dto.user_dto.UserSearchData;
import main.dto.user_dto.UserSearchDto;
import main.dto.user_dto.UserWorkData;
import main.dto.work_dto.WorkRecord;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

public class UserUiController implements Initializable {

	@FXML
	private Tab workTab;

	@FXML
	private Tab leaveTab;

	@FXML
	private Tab moneyTab;

	@FXML
	private Tab mailTab;

	@FXML
	private Tab qnaTab;

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

	/* 로그아웃 버튼 */
	@FXML
	private Button logoutBtn;

	/* 근태기록 탭 */
	@FXML
	private DatePicker selectWorkDate;

	@FXML
	private Button searchWorkDateBtn;

	@FXML
	private ComboBox<String> workComboList;

	@FXML
	private Button startWorkBtn;

	@FXML
	private Button endWorkBtn;

	@FXML
	private Button leaveEarlyBtn;

	/* 휴가신청 탭 */
	@FXML
	private Button leaveRequestBtn;

	/* 급여내역 탭 */
	@FXML
	private DatePicker selectMoneyDate;

	@FXML
	private Button searchMoneyDateBtn;

	/* 메일함 탭 */
	@FXML
	private ComboBox<String> mailComboList;

	@FXML
	private TextField mailTitle;

	@FXML
	private Button searchMailTitleBtn;

	@FXML
	private Button sendMailBtn;

	@FXML
	private Button deleteMailBtn;

	/* Q&A 탭 */
	@FXML
	private TextField qnaTitle;

	@FXML
	private Button titleSearchQnABtn;

	@FXML
	private Button writePostQnABtn;

	@FXML
	private Button updatePostQnABtn;

	@FXML
	private Button deletePostQnABtn;

	/* 근태기록탭 테이블뷰 컬럼 */
	@FXML
	private TableView<WorkRecord> workRecordTableView;
	@FXML
	private TableColumn<WorkRecord, Long> noColumn;
	@FXML
	private TableColumn<WorkRecord, String> dateColumn;
	@FXML
	private TableColumn<WorkRecord, String> workStatusColumn;
	@FXML
	private TableColumn<WorkRecord, String> startTimeColumn;
	@FXML
	private TableColumn<WorkRecord, String> endTimeColumn;
	@FXML
	private TableColumn<WorkRecord, String> noteColumn;

	/* 휴가신청탭 테이블뷰 컬럼 */
	@FXML
	private TableView<LeaveRecord> leaveRecordTableView;
	@FXML
	private TableColumn<LeaveRecord, Long> leaveNoColumn;
	@FXML
	private TableColumn<LeaveRecord, String> leaveRequestColumn;
	@FXML
	private TableColumn<LeaveRecord, String> leaveStartColumn;
	@FXML
	private TableColumn<LeaveRecord, String> leaveEndColumn;
	@FXML
	private TableColumn<LeaveRecord, Boolean> leaveAcceptColumn;

	/* 급여내역탭 테이블뷰 컬럼 */
	@FXML
	private TableView<SalaryRecord> salaryRecordTableView;

	@FXML
	private TableColumn<SalaryRecord, Long> salaryNoColumn;
	@FXML
	private TableColumn<SalaryRecord, String> salaryReceivedColumn;
	@FXML
	private TableColumn<SalaryRecord, Integer> salaryTotalColumn;

	/* Q&A탭 테이블뷰 컬럼 */
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
	private ObservableList<WorkRecord> workRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<LeaveRecord> leaveRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<QnARecord> qnaRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<SalaryRecord> salaryRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<MailRecord> mailRecordList = FXCollections.observableArrayList();

	@FXML
	private ObservableList<String> worklist = FXCollections.observableArrayList("출근", "결근", "조퇴","지각","휴가");

	@FXML
	private ObservableList<String> maillist = FXCollections.observableArrayList("받은메일함", "보낸메일함");

	/* 현재시간 표시 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Long mailNum;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		File file = new File("src/UserImage/userImage.jpg");
		if (file.exists()) {
			Image image = new Image(file.toURI().toString());
			userImage.setImage(image);
		} else {
			System.out.println("이미지 로딩 오류");
		}
		// 근태기록 탭이 선택되었을 때 이벤트 추가
		workTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("근태기록 탭이 선택됨");
				try {
					workTabClickedMethod();
				} catch (IOException e) {
					e.printStackTrace();
				}
				leaveRecordList.clear();
				qnaRecordList.clear();
				salaryRecordList.clear();
				mailRecordList.clear();

				selectWorkDate.setValue(null);
				selectMoneyDate.setValue(null);
				mailTitle.clear();
				qnaTitle.clear();
			}
		});

		// 휴가신청 탭이 선택되었을 때 이벤트 추가
		leaveTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("휴가신청 탭이 선택됨");
				try {
					leaveTablClickedMethod();
				} catch (IOException e) {
					e.printStackTrace();
				}
				workRecordList.clear();
				qnaRecordList.clear();
				salaryRecordList.clear();
				mailRecordList.clear();
				selectWorkDate.setValue(null);
				selectMoneyDate.setValue(null);
				mailTitle.clear();
				qnaTitle.clear();

			}
		});

		// 급여내역 탭이 선택되었을 때 이벤트 추가
		moneyTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("급여내역 탭이 선택됨");
				try {
					moneyTabClickedMethod();
				} catch (IOException e) {
					e.printStackTrace();
				}
				workRecordList.clear();
				leaveRecordList.clear();
				qnaRecordList.clear();
				mailRecordList.clear();

				selectWorkDate.setValue(null);
				selectMoneyDate.setValue(null);
				mailTitle.clear();
				qnaTitle.clear();
			}
		});

		// 메일함 탭이 선택되었을 때 이벤트 추가
		mailTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("메일함 탭이 선택됨");
				try {
					mailTabClickedMethod();
				} catch (IOException e) {
					e.printStackTrace();
				}
				workRecordList.clear();
				leaveRecordList.clear();
				qnaRecordList.clear();
				salaryRecordList.clear();

				selectWorkDate.setValue(null);
				selectMoneyDate.setValue(null);
				mailTitle.clear();
				qnaTitle.clear();
			}
		});

		// Q&A 탭이 선택되었을 때 이벤트 추가
		qnaTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("Q&A 탭이 선택됨");
				try {
					qnaTabClickedMethod();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			leaveRecordList.clear();
			workRecordList.clear();
			salaryRecordList.clear();
			mailRecordList.clear();

			selectWorkDate.setValue(null);
			selectMoneyDate.setValue(null);
			mailTitle.clear();
			qnaTitle.clear();
		});

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalDateTime now = LocalDateTime.now();
			currentTime.setText(now.format(formatter));
		}), new KeyFrame(Duration.seconds(1)));

		clock.setCycleCount(Timeline.INDEFINITE);
		clock.play();

		handleworkComboList();
		handlemailComboList();

		noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		workStatusColumn.setCellValueFactory(new PropertyValueFactory<>("workStatus"));
		startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
		endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
		noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

		leaveNoColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
		leaveRequestColumn.setCellValueFactory(new PropertyValueFactory<>("leaveRequestDate"));
		leaveStartColumn.setCellValueFactory(new PropertyValueFactory<>("leaveStartDate"));
		leaveEndColumn.setCellValueFactory(new PropertyValueFactory<>("leaveEndDate"));
		leaveAcceptColumn.setCellValueFactory(new PropertyValueFactory<>("leaveAcceptStatus"));

		qnaNoColumn.setCellValueFactory(new PropertyValueFactory<>("qnaNo"));
		qnaTitleColumn.setCellValueFactory(new PropertyValueFactory<>("qnaTitle"));
		qnaPostUserColumn.setCellValueFactory(new PropertyValueFactory<>("qnaPostUser"));
		qnaDateColumn.setCellValueFactory(new PropertyValueFactory<>("qnaDate"));

		salaryNoColumn.setCellValueFactory(new PropertyValueFactory<>("salaryNo"));
		salaryReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("salaryReceived"));
		salaryTotalColumn.setCellValueFactory(new PropertyValueFactory<>("salaryTotal"));

		mailNoColumn.setCellValueFactory(new PropertyValueFactory<>("mailNo"));
		mailReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("mailReceived"));
		mailTitleColumn.setCellValueFactory(new PropertyValueFactory<>("mailTitle"));
		mailReceivedDateColumn.setCellValueFactory(new PropertyValueFactory<>("mailReceivedDate"));

		// 버튼의 초기 상태 설정
		checkAndSetButtonState();

		// Timeline을 사용해 1분마다 버튼 상태를 확인하고 업데이트
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), event -> checkAndSetButtonState()));
		timeline.setCycleCount(Timeline.INDEFINITE); // 무한 반복
		timeline.play(); // 타임라인 시작

		try {
			workTabClickedMethod();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		qnaRecordTableView.setOnMouseClicked(event -> {

			if (event.getClickCount() == 2) { // 2번 클릭을 감지
				QnARecord selectedQnAItem = qnaRecordTableView.getSelectionModel().getSelectedItem();

				if (selectedQnAItem != null) {
					System.out.println("Selected QnARecord: " + selectedQnAItem);

					// 선택된 QnARecord의 keyNo 가져오기
					Long selectedKeyNo = selectedQnAItem.getKeyNo();

					try {
						qnaItemClickMethod(selectedKeyNo);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		});

		mailRecordTableView.setOnMouseClicked(event -> {
			MailRecord mailRecord = mailRecordTableView.getSelectionModel().getSelectedItem();
			String receivedUserEmail = mailRecord.getMailReceived();
			mailNum = mailRecord.getMailKeyNo();

			if (event.getClickCount() == 2) {
				try {
					mailItemClickedMethod(mailNum, receivedUserEmail);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("특정 메일 선택 메소드 실행");
			}
		});
	}

	// 버튼 활성화/비활성화 상태를 결정하는 메서드
	private void checkAndSetButtonState() {
		LocalTime currentTime = LocalTime.now();
		LocalTime fiveFiftyPM = LocalTime.of(17, 50);
		LocalTime sixPM = LocalTime.of(18, 10);

		// 17:50:00에서 18:10:00 사이이거나 같은 경우 버튼을 활성화
		if ((currentTime.isAfter(fiveFiftyPM) || currentTime.equals(fiveFiftyPM))
				&& (currentTime.isBefore(sixPM) || currentTime.equals(sixPM))) {
			endWorkBtn.setDisable(false); // 버튼 활성화
		} else {
			endWorkBtn.setDisable(true); // 버튼 비활성화
		}
	}

	/* 좌측 사용자 정보 표시 */
	public void setUserData(UserInfo userInfo) {
		userName.setText(userInfo.getName());
		userTel1.setText(userInfo.getTel());
		userEmail.setText(userInfo.getEmail());
		userDept.setText(userInfo.getDeptName());
		userPosition.setText(userInfo.getPositionName());
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
				Stage currentStage = (Stage) startWorkBtn.getScene().getWindow();
				currentStage.hide();

				// 새 Stage를 표시합니다.
				loginStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	/*
	 * 근태기록 탭
	 */
	/* 날짜 검색 버튼 클릭 시, 선택한 날짜에 대한 검색 로직 */
	public void handlesearchWorkDateBtn() {
		System.out.println("근태탭에서 검색 수행");
		workRecordList.clear();
		try {
			workTabClickedMethod();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void handleworkComboList() {
		workComboList.setItems(worklist);
	}

	/*
	 * 휴가신청 탭
	 */
	/* 휴가신청 버튼 선택 시, 휴가신청 창 띄우기 */
	public void handleleaveRequestBtn() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/leave_ui/leaveRequest.fxml"));
			Parent leaveRequestRoot = fxmlLoader.load();

			Stage leaveRequestStage = new Stage();
			leaveRequestStage.setTitle("휴가신청");
			leaveRequestStage.setScene(new Scene(leaveRequestRoot));

			leaveRequestStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 메일함 탭
	 */
	/* 메일 콤보 리스트 */
	public void handlemailComboList() {
		mailComboList.setItems(maillist);
	}

	/* 메일검색 버튼 로직 */
	public void handlesearchMailTitleBtn() {
		mailRecordList.clear();
		String selectedMailBox = mailComboList.getValue(); // ComboBox에서 선택한 값 가져오기

		if ("받은메일함".equals(selectedMailBox)) {
			// 받은메일함을 선택한 경우
			mailReceivedColumn.setText("보낸 사람");
		} else if ("보낸메일함".equals(selectedMailBox)) {
			// 보낸메일함을 선택한 경우
			mailReceivedColumn.setText("받은 사람");
		}
		try {
			mailTabClickedMethod();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void handledeleteMailBtn() throws IOException {
		System.out.println("메일 삭제 로직 실행");

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
		MailDeleteDto mailDeleteDto = new MailDeleteDto();
		mailDeleteDto.setMailType(MailType.RECEIVED);
		mailDeleteDto.setMailNum(mailNum);

		if (mailComboList.getValue() != null) {
			if (mailComboList.getValue().equals("받은메일함")) {
				mailDeleteDto.setMailType(MailType.RECEIVED);
			} else {
				mailDeleteDto.setMailType(MailType.SEND);
			}
		}

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_MAIL_ONE_DELETE, mailDeleteDto);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			ResponseData<MailDeleteDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					MailDeleteDto.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				mailRecordList.clear();
				mailTabClickedMethod();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	/*
	 * Q&A 탭
	 */
	/* Q&A 글 작성 버튼 클릭 시, 글 작성 창 띄우기 */
	public void handlewritePostQnABtn() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/qna_ui/PostQnA.fxml"));
			Parent postQnARoot = fxmlLoader.load();

			Stage postQnAStage = new Stage();
			postQnAStage.setTitle("글 작성");
			postQnAStage.setScene(new Scene(postQnARoot));

			postQnAStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Q&A 글 삭제 버튼 클릭 시, 글 삭제 처리 로직 */
	public void handledeletePostQnABtn() {

		QnARecord selectedQnAItem = qnaRecordTableView.getSelectionModel().getSelectedItem();

		if (selectedQnAItem != null) {
			Long selectedBoardNum = selectedQnAItem.getKeyNo(); // 선택된 QnARecord의 getKey 가져오기
			Long selectedUserNum = selectedQnAItem.getUserNum();

			// 현재 접속 중인 사용자 정보 가져오기
			User nowUser = new User.Builder()
					.userId(UserInfoSavedUtil.getUserId())	
					.userNum(UserInfoSavedUtil.getUserInfo().getUserNum())
					.role(Role.USER)
					.build();

			// 선택된 게시물 정보와 현재 사용자 정보를 사용하여 BoardDelDto 생성
			Board selectedBoard = new Board.Builder()
					.boardNum(selectedBoardNum)
					.title("")
					.userNum(selectedUserNum)
					.build();

			BoardDelDto boardDelDto = new BoardDelDto.Builder().board(selectedBoard).user(nowUser).build();

			try {
				// 서버에 삭제 요청 (BoardDelDto 객체를 전송)
				boolean deleteSuccess = deleteQnAPostFromServer(boardDelDto);

				if (deleteSuccess) {
					// Q&A 리스트에서 해당 boardNum와 일치하는 항목 삭제
					qnaRecordList.removeIf(qna -> Long.valueOf(qna.getKeyNo()).equals(selectedBoardNum));

					// UI 업데이트
					qnaRecordTableView.refresh();

					// 삭제 성공 메시지 출력
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("성공");
					alert.setHeaderText(null);
					alert.setContentText("해당 Q&A 게시물이 성공적으로 삭제되었습니다.");
					alert.showAndWait();
				} else {
					// 삭제 실패 메시지 출력
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("오류");
					alert.setHeaderText(null);
					alert.setContentText("Q&A 게시물 삭제에 실패했습니다.");
					alert.showAndWait();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			// 글이 선택되지 않은 경우 경고 메시지를 표시
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("경고");
			alert.setHeaderText(null);
			alert.setContentText("삭제할 글을 선택해주세요.");
			alert.showAndWait();
		}

	}

	private boolean deleteQnAPostFromServer(BoardDelDto boardDelDto) throws IOException {
		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		try {
			// BoardDelDto를 JSON으로 직렬화하여 서버로 전송
			String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_BOARD_DELETE, boardDelDto);

			// 서버로 삭제 요청 전송
			communicationUtils.sendServer(jsonSendStr, dos);

			// 서버 응답 수신
			String jsonReceivedStr = dis.readUTF();

			// 서버 응답을 처리하여 성공 여부 판단
			ResponseData<String> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, String.class);
			String messageType = responseData.getMessageType();

			return messageType.contains("성공"); // 성공 여부 반환

		} catch (IOException e) {
			e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		} finally {
			// 서버 연결 종료
			serverConnectUtils.close();
		}

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

					QnARecord qnaRecord = new QnARecord(boardFindAllDto.getUserNum(), boardFindAllDto.getBoardNum(), no,
							boardFindAllDto.getTitle(), boardFindAllDto.getUserId(), boardFindAllDto.getCreatedDate());

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

	/* Q&A 리스트 아이템 선택 시, 선택된 아이템 창 연결 */
	public void qnaItemClickMethod(Long keyNo) throws IOException {
		CommunicationUtils communicationUtils = new CommunicationUtils();

		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		/**
		 * 데이터를 주고받기 위해 stream을 받아옴
		 */
		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		/**
		 * requestData 생성 //
		 */
//		RequestData requestData = new RequestData();
//		requestData.setData(userLoginDto);
//		requestData.setMessageType(MessageTypeConst.MESSAGE_LOGIN);

		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_BOARD_ONE_SEARCH, keyNo);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			ResponseData<BoardAndAnswer> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					BoardAndAnswer.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				BoardAndAnswer boardAndAnswer = responseData.getData();
				// 데이터가 null인지 확인
				if (boardAndAnswer != null) {
					// 성공적으로 데이터를 받았는지 확인
					if (responseData.getMessageType().contains("성공")) {

						Platform.runLater(() -> {
							try {
								// QnA 상세 화면 로드
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/main/qna_ui/ShowQnA.fxml"));
								Parent qnaRoot = fxmlLoader.load();

								// QnAShowController를 가져와서 데이터를 설정
								QnAShowController qnaShowController = fxmlLoader.getController();
								qnaShowController.setBoardAndAnswerData(boardAndAnswer, keyNo,
										boardAndAnswer.getBoardInfoDto().getUserNum());

								// 새 창을 띄우고 현재 창 숨기기
								Stage qnaStage = new Stage();
								qnaStage.setTitle("Q&A 상세보기");
								qnaStage.setScene(new Scene(qnaRoot));

								qnaStage.show();

							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					} else {
						System.out.println("게시글 정보를 가져오는 데 실패했습니다.");
					}
				} else {
					System.out.println("BoardAndAnswer 객체가 null입니다.");
				}
			} else {
				System.out.println("서버 응답이 null입니다.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 연결 종료
			serverConnectUtils.close();
		}
	}

	/* 메일함 리스트 아이템 선택 시, 선택된 아이템 창 연결 */
	public void mailItemClickedMethod(Long mailNum, String receivedUserEmail) throws IOException {
		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		/**
		 * 데이터를 주고받기 위해 stream을 받아옴
		 */
		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		/**
		 * requestData 생성 //
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_MAIL_ONE_SEARCH, mailNum);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			ResponseData<Mail> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, Mail.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				Mail mail = responseData.getData();

				// 데이터가 null인지 확인
				if (mail != null) {
					// 성공적으로 데이터를 받았는지 확인
					if (responseData.getMessageType().contains("성공")) {
						Platform.runLater(() -> {
							try {
								// QnA 상세 화면 로드
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/main/mail_ui/ShowMail.fxml"));
								Parent mailRoot = fxmlLoader.load();

								MailShowController mailShowController = fxmlLoader.getController();
								mailShowController.setMailWindow(mail, receivedUserEmail);

								Stage mailStage = new Stage();
								mailStage.setTitle("메일 상세보기");
								mailStage.setScene(new Scene(mailRoot));

								mailStage.show();

							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					} else {
						System.out.println("메일 정보를 가져오는 데 실패했습니다.");
					}
				} else {
					System.out.println("Mail 객체가 null입니다.");
				}
			} else {
				System.out.println("서버 응답이 null입니다.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 연결 종료
			serverConnectUtils.close();
		}
	}

	// 근태기록 선택되었을 때
	public void workTabClickedMethod() throws IOException {
		System.out.println("근태기록 클릭이벤트 발생");
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
		Status status = null;

		// 결근이 선택됨
		if (workComboList.getValue() != null && workComboList.getValue().equals(Status.ABSENCE.getDescription())) {
			status = Status.ABSENCE;
		}

		// 출근이 선택됨
		if (workComboList.getValue() != null && workComboList.getValue().equals(Status.ATTENDANCE.getDescription())) {
			status = Status.ATTENDANCE;
		}

		// 조퇴가 선택됨
		if (workComboList.getValue() != null && workComboList.getValue().equals(Status.LEAVEPREV.getDescription())) {
			status = Status.LEAVEPREV;
		}
		
		// 지각이 선택됨
		if (workComboList.getValue() != null && workComboList.getValue().equals(Status.TARDINESS.getDescription())) {
			status = Status.TARDINESS;
		}
		
		// 휴가가 선택됨
		if (workComboList.getValue() != null && workComboList.getValue().equals(Status.LEAVE.getDescription())) {
			status = Status.LEAVE;
		}
		

		UserSearchData userSearchData = new UserSearchData.Builder().date(selectWorkDate.getValue()).status(status)
				.build();

		UserSearchDto userSearchDto = new UserSearchDto.Builder().userId(UserInfoSavedUtil.getUserId())
				.role(UserInfoSavedUtil.getRole()).userSearchData(userSearchData).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_WORK_SEARCH, userSearchDto);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<UserWorkData>>() {
			}.getType();
			ResponseData<UserWorkData> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, listType);
			String messageType = responseData.getMessageType();
			if (messageType.contains("성공")) {
				List<UserWorkData> list = (List<UserWorkData>) responseData.getData();
				Long no = 1L;
				for (int i = list.size() - 1; i >= 0; i--) {
					UserWorkData userWorkData = list.get(i);
					WorkRecord workRecord = new WorkRecord(no, userWorkData.getWorkDate(), userWorkData.getStatus(),
							userWorkData.getStartTime(), userWorkData.getEndTime(), "");
					no++;
					workRecordList.add(workRecord);
				}

				Platform.runLater(() -> {
					workRecordTableView.setItems(workRecordList);
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

	// 휴가탭이 선택되었을 때
	public void leaveTablClickedMethod() throws IOException {
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
		forFindLeaveDto.setUserRoleDto(UserRoleDto.USER);

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_LEAVE_SEARCH, forFindLeaveDto);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<LeaveLogOfUserDto>>() {
			}.getType();
			ResponseData<LeaveLogOfUserDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<LeaveLogOfUserDto> list = (List<LeaveLogOfUserDto>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					Long no = Long.valueOf(i + 1);
					LeaveLogOfUserDto leaveLogOfUserDto = list.get(i);
					LeaveRecord leaveRecord = new LeaveRecord(no, leaveLogOfUserDto.getRequestDate(),
							leaveLogOfUserDto.getStartDate(), leaveLogOfUserDto.getEndDate(),
							leaveLogOfUserDto.getAcceptanceStatus());

					leaveRecordList.add(leaveRecord);

				}

				Platform.runLater(() -> {
					leaveRecordTableView.setItems(leaveRecordList);

					leaveAcceptColumn.setCellFactory(column -> new TableCell<LeaveRecord, Boolean>() {
						@Override
						protected void updateItem(Boolean item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setText(null);
							} else {
								setText(item ? "승인 됨" : "승인 안됨");
							}
						}
					});
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

	// 급여내역이 선택되었을때
	public void moneyTabClickedMethod() throws IOException {
		System.out.println("급여내역 클릭 이벤트 발생");
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
		User user = new User.Builder().userId(UserInfoSavedUtil.getUserId()).role(Role.USER)
				.moneySearchDate(selectMoneyDate.getValue()).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_SALARY_SEARCH, user);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();

			Type listType = new TypeToken<List<UserSalaryData>>() {
			}.getType();
			ResponseData<UserSalaryData> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
					listType);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				List<UserSalaryData> list = (List<UserSalaryData>) responseData.getData();
				for (int i = 0; i < list.size(); i++) {
					System.out.println("휴가로그 출력 실행");
					UserSalaryData userSalaryData = list.get(i);
					Long no = (long) (i + 1);

					SalaryRecord salaryRecord = new SalaryRecord(no, userSalaryData.getReceivedDate().toString(),
							userSalaryData.getTotalSalary());

					salaryRecordList.add(salaryRecord);
				}

				Platform.runLater(() -> {
					salaryRecordTableView.setItems(salaryRecordList);
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

		if (mailComboList.getValue() != null) {
			if (mailComboList.getValue().equals("받은메일함")) {
				mailSearchDto.setMailType(MailType.RECEIVED);
			} else {
				mailSearchDto.setMailType(MailType.SEND);
			}
		}

		mailSearchDto.setMailTitle(mailTitle.getText());

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
					MailAllDto mailAllDto = list.get(i);
					Long no = (long) (i + 1);
					MailRecord mailRecord = new MailRecord(mailAllDto.getMailNum(), no, mailAllDto.getUserEmail(),
							mailAllDto.getTitle(), mailAllDto.getCreatedDate().toString());

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

	public void qnaTabClickedMethod() throws IOException {
		System.out.println("Q&A탭 클릭 이벤트 발생");
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
					QnARecord qnaRecord = new QnARecord(boardFindAllDto.getUserNum(), boardFindAllDto.getBoardNum(), no,
							boardFindAllDto.getTitle(), boardFindAllDto.getUserId(), boardFindAllDto.getCreatedDate());
					qnaRecordList.add(qnaRecord);
				}

				Platform.runLater(() -> {
					qnaRecordTableView.setItems(qnaRecordList);
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

	public void handleStartWork() throws IOException {
		System.out.println("출근 로직 실행");
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
		User user = new User.Builder().userId(UserInfoSavedUtil.getUserId()).role(Role.USER).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_WORK_START, user);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();
			ResponseData<WorkLog> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, WorkLog.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				WorkLog workLog = responseData.getData();
				System.out.println("근태 상태 변경 성공");
				UiAlert("상태변경", "출근 처리되었습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	public void handleEndWork() throws IOException {
		System.out.println("퇴근 로직 실행");
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
		User user = new User.Builder().userId(UserInfoSavedUtil.getUserId()).role(Role.USER).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_WORK_FINISH, user);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();
			ResponseData<WorkLog> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, WorkLog.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				WorkLog workLog = responseData.getData();
				System.out.println("근태 상태 변경 성공");
				UiAlert("상태변경", "퇴근 처리되었습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	public void handleEndWorkEarly() throws IOException {
		System.out.println("조퇴 로직 실행");
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
		User user = new User.Builder().userId(UserInfoSavedUtil.getUserId()).role(Role.USER).build();

		/**
		 * requestData 생성
		 */
		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_WORK_OUT_EARLY, user);

		try {
			communicationUtils.sendServer(jsonSendStr, dos);
			String jsonReceivedStr = dis.readUTF();
			ResponseData<WorkLog> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, WorkLog.class);
			String messageType = responseData.getMessageType();

			if (messageType.contains("성공")) {
				WorkLog workLog = responseData.getData();
				System.out.println("근태 상태 변경 성공");
				UiAlert("상태변경", "조퇴 처리되었습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverConnectUtils.close();
		}
	}

	public void handleMoneySearch() {
		salaryRecordList.clear();
		try {
			moneyTabClickedMethod();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void UiAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private boolean deleteQnAPostFromServer(Long boardNum, Long writerUserNum) throws IOException {
		CommunicationUtils communicationUtils = new CommunicationUtils();
		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
		DataInputStream dis = serverConnectUtils.getDataInputStream();

		User user = new User.Builder().userNum(UserInfoSavedUtil.getUserInfo().getUserNum()).role(Role.USER).build();

		Board board = new Board.Builder().userNum(writerUserNum).boardNum(boardNum).build();

		BoardDelDto boardDelDto = new BoardDelDto.Builder().user(user).board(board).build();

		try {
			// boardNum만 JSON으로 직렬화
			String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_BOARD_DELETE, boardDelDto);

			// 서버로 삭제 요청 전송
			communicationUtils.sendServer(jsonSendStr, dos);

			// 서버 응답 수신
			String jsonReceivedStr = dis.readUTF();

			// 서버 응답을 처리하여 성공 여부 판단
			ResponseData<String> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, String.class);
			String messageType = responseData.getMessageType();

			return messageType.contains("성공") ? true : false; // 성공 여부 반환

		} catch (IOException e) {
			e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		} finally {
			// 서버 연결 종료
			serverConnectUtils.close();
		}
	}

}
