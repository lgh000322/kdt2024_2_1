package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.domain.mail.Mail;
import main.dto.ResponseData;
import main.dto.mail_dto.MailReceivedData;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;

import org.jsoup.Jsoup;

public class MailController {

	@FXML
	private ComboBox<String> receiveUserEmail;

	@FXML
	private TextField emailTitle;

	@FXML
	private HTMLEditor emailContent;

	public void setReceiveUserEmailData(ObservableList<String> emailList) {
		receiveUserEmail.setItems(emailList);
	}

	// 메일 전송버튼을 눌렀을 때
	public void handleEmailSubmit() throws IOException {
	    // 메일 전송 로직 구현
	    String selectedEmail = receiveUserEmail.getValue(); // 선택된 이메일 주소
	    String title = emailTitle.getText(); // 이메일 제목
	    String htmlContent = emailContent.getHtmlText(); // 이메일 내용 (HTML 포함)
	    String content = Jsoup.parse(htmlContent).text(); // HTML 태그 제거하고 텍스트만 추출

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
	    MailReceivedData mailReceivedData = new MailReceivedData();
	    mailReceivedData.setSendUserEmail(UserInfoSavedUtil.getUserInfo().getEmail());
	    List<String> receivedEmails = new ArrayList<>();
	    receivedEmails.add(selectedEmail);
	    mailReceivedData.setReceivedUserEmails(receivedEmails);
	    Mail mail = new Mail.Builder().title(title).contents(content).createdDate(LocalDate.now()).build();
	    mailReceivedData.setMail(mail);

	    /**
	     * requestData 생성
	     */
	    String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_MAIL_ADD, mailReceivedData);

	    try {
	        communicationUtils.sendServer(jsonSendStr, dos);
	        String jsonReceivedStr = dis.readUTF();

	        ResponseData<Object> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, Object.class);
	        String messageType = responseData.getMessageType();
	        if (messageType.contains("성공")) {
	            Stage stage = (Stage) emailTitle.getScene().getWindow();
	            stage.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        serverConnectUtils.close();
	    }
	}

}
