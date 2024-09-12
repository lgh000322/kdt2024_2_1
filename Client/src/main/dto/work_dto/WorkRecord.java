package main.dto.work_dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import main.domain.work_log.Status;

public class WorkRecord {
    private final SimpleLongProperty no;
    private final SimpleStringProperty date;
    private final SimpleStringProperty workStatus;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty note;

    // 생성자
    public WorkRecord(Long no, LocalDate localDate, Status status, LocalTime startTime, LocalTime endTime, String note) {
    	 this.no = new SimpleLongProperty(no);
    	    this.date = new SimpleStringProperty(localDate.toString()); // LocalDate를 String으로 변환
    	    this.workStatus = new SimpleStringProperty(status.toString()); // Status를 String으로 변환
    	    this.startTime = new SimpleStringProperty(startTime != null ? startTime.toString() : "값 없음"); // null 체크
    	    this.endTime = new SimpleStringProperty(endTime != null ? endTime.toString() : "값 없음"); // null 체크
    	    this.note = new SimpleStringProperty(note);
    }

    public long getNo() {
        return no.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getWorkStatus() {
        return workStatus.get();
    }

    public String getStartTime() {
        return startTime.get();
    }

    public String getEndTime() {
        return endTime.get();
    }

    public String getNote() {
        return note.get();
    }
}
