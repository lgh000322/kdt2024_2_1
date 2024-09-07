package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.util.ServerConnectUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("login_ui/LoginUi.fxml"));
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("로그인");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

}