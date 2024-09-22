package main.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import main.consts.MessageTypeConst;
import main.dto.ResponseData;
import main.dto.board_dto.BoardSaveDto;
import main.util.CommunicationUtils;
import main.util.ServerConnectUtils;
import main.util.UserInfoSavedUtil;
import org.jsoup.Jsoup;

public class QnAPostController {

    @FXML
    private HTMLEditor InputContents;

    @FXML
    private TextField InputTitle;

    @FXML
    private Button writePostBtn;

    public void handlewritePostBtn() throws IOException {
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
         * requestData의 data에 넣어줄 객체를 생성
         */
        BoardSaveDto boardSaveDto = new BoardSaveDto();
        boardSaveDto.setTitle(InputTitle.getText());

        // HTML에서 순수 텍스트만 추출하여 저장
        String htmlContent = InputContents.getHtmlText();
        String plainText = Jsoup.parse(htmlContent).text(); // HTML 태그 제거하고 순수 텍스트만 추출
        boardSaveDto.setContents(plainText);  // 텍스트만 서버에 전송

        boardSaveDto.setUserNum(UserInfoSavedUtil.getUserInfo().getUserNum());
        boardSaveDto.setCreatedDate(LocalDate.now());

        /**
         * requestData 생성
         */
        String jsonSendStr = communicationUtils.objectToJson(MessageTypeConst.MESSAGE_BOARD_ADD, boardSaveDto);

        /**
         * 서버에 json 문자열을 보내고 서버에서 json 문자열을 받고, 해당 json 문자열을 객체로 변환 responseData의
         * messageType의로 회원가입이 정상적으로 이뤄어졌는지 판단.
         */
        try {
            communicationUtils.sendServer(jsonSendStr, dos);
            String jsonReceivedStr = dis.readUTF();

            ResponseData<BoardSaveDto> responseData = communicationUtils.jsonToResponseData(jsonReceivedStr,
                    BoardSaveDto.class);
            String messageType = responseData.getMessageType();

            Platform.runLater(() -> {
                Stage stage = (Stage) writePostBtn.getScene().getWindow();
                stage.close();
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverConnectUtils.close();
        }
    }
}