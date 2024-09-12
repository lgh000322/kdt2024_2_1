package main.dto.user_dto;

import java.time.LocalDate;
import java.time.LocalTime;

import main.domain.work_log.Status;

public class UserWorkData {

	
	private Long workNum; // 근퇴번호 PK
    private LocalDate workDate; // 근무날짜
    private Status status; // 근무 상태
    private LocalTime startTime; // 출근시간
    private LocalTime endTime; // 퇴근시간

    
    public Long getWorkNum() {
		return workNum;
	}

	

	public LocalDate getWorkDate() {
		return workDate;
	}



	public Status getStatus() {
		return status;
	}



	public LocalTime getStartTime() {
		return startTime;
	}


	public LocalTime getEndTime() {
		return endTime;
	}



	
    private UserWorkData(Builder builder) {
        this.workNum = builder.workNum;
        this.workDate = builder.workDate;
        this.status = builder.status;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    // toString 메서드 추가
    @Override
    public String toString() {
        return "UserWorkData{" +
                "workNum=" + workNum +
                ", workDate=" + workDate +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static class Builder {
        private Long workNum;
        private LocalDate workDate;
        private Status status;
        private LocalTime startTime;
        private LocalTime endTime;

        public Builder workNum(Long workNum) {
            this.workNum = workNum;
            return this;
        }

        public Builder workDate(LocalDate workDate) {
            this.workDate = workDate;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public UserWorkData build() {
            return new UserWorkData(this);
        }

    }
}
