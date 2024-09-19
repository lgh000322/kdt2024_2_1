module Client {
	requires javafx.controls; // JavaFX 관련 모듈
	requires javafx.fxml; // FXML 모듈
	requires com.google.gson; // Gson 라이브러리
	requires javafx.graphics; // JavaFX 그래픽 관련 모듈
	requires java.sql; // SQL 관련 모듈
	requires javafx.base;
	requires javafx.web;

	opens main.controller to javafx.fxml; // FXML에서 사용할 컨트롤러 모듈 열기
	opens main.dto to com.google.gson, javafx.base; // Gson에서 사용할 DTO 모듈 열기
	opens main.dto.user_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.answer_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.board_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.leave_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.mail_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.salary_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.dto.work_dto to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.user to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.board to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.dept to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.leave_log to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.mail to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.position to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.salary_log to com.google.gson, javafx.base; // 필요한 경우
	opens main.domain.work_log to com.google.gson, javafx.base; // 필요한 경우
	
	exports main.dto.work_dto;
	exports main to javafx.graphics, javafx.controls, javafx.fxml; // JavaFX에 내보내기
	exports main.controller; // 컨트롤러를 JavaFX에 내보내기
	exports main.dto.user_dto; // DTO를 JavaFX에 내보내기
	exports main.dto; // DTO를 JavaFX에 내보내기
	exports main.domain.board;
}