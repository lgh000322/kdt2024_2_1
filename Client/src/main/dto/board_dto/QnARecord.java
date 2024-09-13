package main.dto.board_dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import main.domain.work_log.Status;

public class QnARecord {
	private final SimpleLongProperty keyNo;
    private final SimpleLongProperty qnaNo;
    private final SimpleStringProperty qnaTitle;
    private final SimpleStringProperty qnaPostUser;
    private final SimpleStringProperty qnaDate;

    // 생성자
    public QnARecord(Long keyNo, Long qnaNo, String qnaTitle, String qnaPostUser, LocalDate qnaDate) {
    	this.keyNo = new SimpleLongProperty(keyNo);
        this.qnaNo = new SimpleLongProperty(qnaNo);
        this.qnaTitle = new SimpleStringProperty(qnaTitle);
        this.qnaPostUser = new SimpleStringProperty(qnaPostUser);
        this.qnaDate = new SimpleStringProperty(qnaDate.toString());
    }

    // Getter for the properties
    public SimpleLongProperty keyNoProperty() {
    	return keyNo;
    }
    
    public SimpleLongProperty qnaNoProperty() {
        return qnaNo;
    }

    public SimpleStringProperty qnaTitleProperty() {
        return qnaTitle;
    }

    public SimpleStringProperty qnaPostUserProperty() {
        return qnaPostUser;
    }

    public SimpleStringProperty qnaDateProperty() {
        return qnaDate;
    }

    // 기존 Getter 메서드 (필요시 유지)
    public long getKeyNo() {
    	return keyNo.get();
    }
    
    public long getQnANo() {
        return qnaNo.get();
    }

    public String getQnATitle() {
        return qnaTitle.get();
    }

    public String getQnAPostUser() {
        return qnaPostUser.get();
    }

    public String getQnADate() {
        return qnaDate.get();
    }
}
