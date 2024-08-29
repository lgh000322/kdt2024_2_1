package project2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private StackPane centerStackPane;

    @FXML
    private void handleMenuAction(MenuItem menuItem) {
        String menuText = menuItem.getText();
        Node content;

        switch (menuText) {
            case "결재문서":
                content = new Label("결재문서 내용");
                break;
            case "프로젝트 관리":
                content = new VBox(new Label("프로젝트 관리 내용"), new Button("작업 시작"));
                break;
            case "메일 작성":
                content = new Label("메일 작성 화면");
                break;
            default:
                content = new Label("알 수 없는 메뉴 선택");
        }

        // StackPane의 자식 요소를 모두 제거하고 새로운 콘텐츠 추가
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(content);
    }
}
