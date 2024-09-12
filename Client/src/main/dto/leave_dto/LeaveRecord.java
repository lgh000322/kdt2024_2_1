package main.dto.leave_dto;

import java.time.LocalDate;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import main.domain.work_log.Status;

public class LeaveRecord {
    private final SimpleLongProperty no;
    private final SimpleStringProperty leaveRequestDate;
    private final SimpleStringProperty leaveStartDate;
    private final SimpleStringProperty leaveEndDate;
    private final SimpleStringProperty leaveAcceptStatus;

    // 생성자
    public LeaveRecord(Long no, LocalDate leaveRequestDate, LocalDate leaveStartDate, LocalDate leaveEndDate, Status leaveAcceptStatus) {
        this.no = new SimpleLongProperty(no);
        
        // LocalDate를 String으로 변환
        this.leaveRequestDate = new SimpleStringProperty(leaveRequestDate != null ? leaveRequestDate.toString() : "");
        this.leaveStartDate = new SimpleStringProperty(leaveStartDate != null ? leaveStartDate.toString() : "");
        this.leaveEndDate = new SimpleStringProperty(leaveEndDate != null ? leaveEndDate.toString() : "");
        
        // Status를 String으로 변환
        this.leaveAcceptStatus = new SimpleStringProperty(leaveAcceptStatus != null ? leaveAcceptStatus.toString() : "");
    }

    public long getNo() {
        return no.get();
    }

    public String getLeaveRequestDate() {
        return leaveRequestDate.get();
    }

    public String getLeaveStartDate() {
        return leaveStartDate.get();
    }

    public String getLeaveEndDate() {
        return leaveEndDate.get();
    }

    public String getLeaveAcceptStatus() {
        return leaveAcceptStatus.get();
    }
}

