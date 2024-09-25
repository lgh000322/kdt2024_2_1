package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.dto.ResponseData;
import main.dto.user_dto.UpdateUserDto;
import main.dto.user_dto.UserInfo;
import main.dto.user_dto.UserRecord;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;

public class AdminUserManagementController {
	
	@FXML
	private ImageView userImage;

    @FXML
    private ComboBox<String> positionComboBox;
    
    @FXML
    private ComboBox<String> deptComboBox;

    @FXML
    private Label userEmail;

    @FXML
    private Label userName;

    @FXML
    private Label userTel;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private Button cancelBtn;
    
    
    // 바뀐정보를 확인하기 위한 변수 선언.
	private String deptName; 
	private String positionName;
    
    @FXML
    public void initialize() {
    	File file = new File("src/UserImage/userImage.jpg");
		if (file.exists()) {
			Image image = new Image(file.toURI().toString());
			userImage.setImage(image);
		} else {
			System.out.println("이미지 로딩 오류");
		}
		
        // Define a list of dummy values for the positionComboBox
        ObservableList<String> positionValues = FXCollections.observableArrayList(
            "사원", "주임", "대리", "팀장", "과장", "부장", "부사장", "사장"
        );

        // Add these values to the positionComboBox
        positionComboBox.setItems(positionValues);
        
        // Define a list of dummy values for the positionComboBox
        ObservableList<String> deptValues = FXCollections.observableArrayList(
            "임시부서", "인사부", "재무부", "영업부", "IT", "법무부"
        );

        deptComboBox.setItems(deptValues);
        
        //저장 버튼을 클릭시 실행.
        saveBtn.setOnAction(event -> {
        	System.out.println("사원 정보 수정 실행.");

    		CommunicationUtils communicationUtils = new CommunicationUtils();
    		ServerConnectUtils serverConnectUtils = communicationUtils.getConnection();

    		DataOutputStream dos = serverConnectUtils.getDataOutputStream();
    		DataInputStream dis = serverConnectUtils.getDataInputStream();
    		UpdateUserDto updateUserDto = new UpdateUserDto();
 
    		//만약 부서가 변하지 않았다면 부서와 직위가 변한게 없다면 서버에 보내지 않고 종료
    		if(deptName.equals(deptComboBox.getValue()) && positionName.equals(positionComboBox.getValue())) {
    			try {
					serverConnectUtils.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			Stage stage = (Stage) saveBtn.getScene().getWindow();
    	        stage.close();
    	        return;
    		}
    		
    		//만약 부서 이름이 변하고 , 직위 이름이 변경되지 않았다면 updateUserDto에는 부서와 email만 값을 넣어서 보내줌.
    		else if(!deptName.equals(deptComboBox.getValue()) && positionName.equals(positionComboBox.getValue())) {
    			
        		updateUserDto.setDeptName(deptComboBox.getValue());
        		updateUserDto.setPositionName(null);
        		updateUserDto.setEmail(userEmail.getText());
    		}
    		
    		// 만약 부서이름이 같고 직위만 달라졌다면 updateUserDto에는 직위와 email값만 추가하여 서버로 보냄.
    		else if(deptName.equals(deptComboBox.getValue()) && !positionName.equals(positionComboBox.getValue())) {
    			
        		updateUserDto.setDeptName(null);
        		updateUserDto.setPositionName(positionComboBox.getValue());
        		updateUserDto.setEmail(userEmail.getText());
    		}
    		
    		// 만약 둘다 달라졌다면 부서 직위 email 값을 모두 보내줌
    		else if(!deptName.equals(deptComboBox.getValue()) && !positionName.equals(positionComboBox.getValue())) {
	    
	    		updateUserDto.setDeptName(deptComboBox.getValue());
	    		updateUserDto.setPositionName(positionComboBox.getValue());
	    		updateUserDto.setEmail(userEmail.getText());
    		}
    		
    		String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_UPDATE, updateUserDto);
    		
    		try {
    			communicationUtils.sendServer(jsonSendStr, dos);
    			String jsonReceivedStr = dis.readUTF();

    			ResponseData<UpdateUserDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr, UpdateUserDto.class);
    			String messageType = responseData.getMessageType();

    			if (messageType.contains("성공")) {
    				try {
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
    
    
    
   
    
    // 유저 직위, 부서 수정페이지 정버 설정
    public void setUpdateUserInfo(UserInfo userInfo) {
    	userName.setText(userInfo.getName());
		userTel.setText(userInfo.getTel());
		userEmail.setText(userInfo.getEmail());
		deptComboBox.setValue(userInfo.getDeptName());
		positionComboBox.setValue(userInfo.getPositionName());
		
		//초기 deptName과 positionName을 확인하기위해 해당값들을 저장.
		deptName = deptComboBox.getValue();
		positionName = positionComboBox.getValue();
    	
    }
}