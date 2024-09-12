package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminUserManagementController {

    @FXML
    private ComboBox<String> positionComboBox;

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
    
    @FXML
    public void initialize() {
        // Define a list of dummy values for the positionComboBox
        ObservableList<String> positionValues = FXCollections.observableArrayList(
            "사원", "주임", "대리", "팀장", "과장", "부장"
        );

        // Add these values to the positionComboBox
        positionComboBox.setItems(positionValues);

        // Set an event handler for the cancel button to close the window
        cancelBtn.setOnAction(event -> {
            // Get the current stage and close it
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }
}