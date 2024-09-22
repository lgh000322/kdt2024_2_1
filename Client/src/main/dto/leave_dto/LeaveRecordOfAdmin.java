package main.dto.leave_dto;

import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

public class LeaveRecordOfAdmin {

    private final SimpleLongProperty no;
    private final SimpleStringProperty userName;
    private final SimpleStringProperty leaveRequestDate;
    private final SimpleStringProperty leaveStartDate;
    private final SimpleStringProperty leaveEndDate;
    private final SimpleStringProperty deptName;
    private final SimpleBooleanProperty leaveAcceptStatus;
    private final SimpleIntegerProperty remainedLeave;
	private final SimpleBooleanProperty checkStatus;
    private final SimpleStringProperty userId;
    // 생성자
    public LeaveRecordOfAdmin(Long no, String userName, LocalDate leaveRequestDate, LocalDate leaveStartDate,
            LocalDate leaveEndDate, String deptName, Boolean leaveAcceptStatus, Integer remainedLeave, Boolean checkStatus, String userId) {
        this.no = new SimpleLongProperty(no);
        this.userName = new SimpleStringProperty(userName);
        this.leaveRequestDate = new SimpleStringProperty(leaveRequestDate.toString());
        this.leaveStartDate = new SimpleStringProperty(leaveStartDate.toString());
        this.leaveEndDate = new SimpleStringProperty(leaveEndDate.toString());
        this.deptName = new SimpleStringProperty(deptName);
        this.leaveAcceptStatus = new SimpleBooleanProperty(leaveAcceptStatus);
        this.remainedLeave = new SimpleIntegerProperty(remainedLeave);
        this.checkStatus = new SimpleBooleanProperty(checkStatus);
        this.userId = new SimpleStringProperty(userId);
    }

    // Property 반환 메서드
    public LongProperty noProperty() {
        return no;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty leaveRequestDateProperty() {
        return leaveRequestDate;
    }

    public StringProperty leaveStartDateProperty() {
        return leaveStartDate;
    }

    public StringProperty leaveEndDateProperty() {
        return leaveEndDate;
    }

    public StringProperty deptNameProperty() {
        return deptName;
    }

    public BooleanProperty leaveAcceptStatusProperty() {
        return leaveAcceptStatus;
    }

    public IntegerProperty remainedLeaveProperty() {
        return remainedLeave;
    }

    // Getter methods
    public long getNo() {
        return no.get();
    }

    public void setNo(long no) {
        this.no.set(no);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getLeaveRequestDate() {
        return leaveRequestDate.get();
    }

    public void setLeaveRequestDate(String leaveRequestDate) {
        this.leaveRequestDate.set(leaveRequestDate);
    }

    public String getLeaveStartDate() {
        return leaveStartDate.get();
    }

    public void setLeaveStartDate(String leaveStartDate) {
        this.leaveStartDate.set(leaveStartDate);
    }

    public String getLeaveEndDate() {
        return leaveEndDate.get();
    }

    public void setLeaveEndDate(String leaveEndDate) {
        this.leaveEndDate.set(leaveEndDate);
    }

    public String getDeptName() {
        return deptName.get();
    }

    public void setDeptName(String deptName) {
        this.deptName.set(deptName);
    }

    public boolean getLeaveAcceptStatus() {
        return leaveAcceptStatus.get();
    }

    public void setLeaveAcceptStatus(boolean leaveAcceptStatus) {
        this.leaveAcceptStatus.set(leaveAcceptStatus);
    }

    public int getRemainedLeave() {
        return remainedLeave.get();
    }

    public void setRemainedLeave(int remainedLeave) {
        this.remainedLeave.set(remainedLeave);
    }
    
    // CheckStatus
    public boolean getCheckStatus() {
        return checkStatus.get();
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus.set(checkStatus);
    }

    // UserId
    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }
}