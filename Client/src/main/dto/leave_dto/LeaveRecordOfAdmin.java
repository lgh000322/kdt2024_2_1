package main.dto.leave_dto;

import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaveRecordOfAdmin {

	   	private final SimpleLongProperty no;
	    private final SimpleStringProperty userName;
	    private final SimpleStringProperty leaveRequestDate;
	    private final SimpleStringProperty leaveStartDate;
	    private final SimpleStringProperty leaveEndDate;
	    private final SimpleStringProperty deptName;
	    private final SimpleBooleanProperty leaveAcceptStatus;
	    private final SimpleIntegerProperty remainedLeave;

	    // 생성자
	    public LeaveRecordOfAdmin(Long no, String userName, LocalDate leaveRequestDate, LocalDate leaveStartDate
	    		, LocalDate leaveEndDate, String deptName, Boolean leaveAcceptStatus, Integer remainedLeave) {
	        this.no = new SimpleLongProperty(no);
	        this.userName = new SimpleStringProperty(userName);
	        // LocalDate를 String으로 변환
	        this.leaveRequestDate = new SimpleStringProperty(leaveRequestDate.toString());
	        this.leaveStartDate = new SimpleStringProperty(leaveStartDate.toString());
	        this.leaveEndDate = new SimpleStringProperty(leaveEndDate.toString());
	        this.deptName = new SimpleStringProperty(deptName);
	        this.leaveAcceptStatus = new SimpleBooleanProperty(leaveAcceptStatus);
	        this.remainedLeave = new SimpleIntegerProperty(remainedLeave);
	    }

		public SimpleLongProperty getNo() {
			return no;
		}

		public SimpleStringProperty getUserName() {
			return userName;
		}

		public SimpleStringProperty getLeaveRequestDate() {
			return leaveRequestDate;
		}

		public SimpleStringProperty getLeaveStartDate() {
			return leaveStartDate;
		}

		public SimpleStringProperty getLeaveEndDate() {
			return leaveEndDate;
		}

		public SimpleStringProperty getDeptName() {
			return deptName;
		}

		public SimpleBooleanProperty getLeaveAcceptStatus() {
			return leaveAcceptStatus;
		}

		public SimpleIntegerProperty getRemainedLeave() {
			return remainedLeave;
		}
	   
}
