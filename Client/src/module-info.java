module Client {
    requires javafx.controls; // JavaFX 관련 모듈
    requires javafx.fxml;     // 만약 FXML을 사용하고 있다면
    requires com.google.gson;
	requires javafx.graphics; // Gson 라이브러리
    
	opens main.controller to javafx.fxml;
	opens main.dto to com.google.gson;
    opens main.domain.user to com.google.gson;
    opens main.domain.board to com.google.gson;
    opens main.domain.dept to com.google.gson;
    opens main.domain.leave_log to com.google.gson;
    opens main.domain.mail to com.google.gson;
    opens main.domain.position to com.google.gson;
    opens main.domain.salary_log to com.google.gson;
    opens main.domain.work_log to com.google.gson;


    
    exports main to javafx.graphics, javafx.controls, javafx.fxml;
    exports main.controller;
    exports main.dto;
}