package org.example.server.dto.leave_dto;


import java.time.LocalDate;

public class ForUpdateLeaveDto {

    private Long leaveNum;

    private LocalDate startDate;

    private LocalDate endDate;

    private String userId;

    private LeaveStatus leaveStatus;

    private ForUpdateLeaveDto(Builder builder) {
        this.leaveNum = builder.leaveNum;
        this.leaveStatus = builder.leaveStatus;
        this.startDate=builder.startDate;
        this.endDate=builder.endDate;

        this.userId = builder.userId;

    }

    public static class Builder{
        private Long leaveNum;

        private LeaveStatus leaveStatus;

        private LocalDate startDate;

        private LocalDate endDate;

        private String userId;

        public Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public Builder status(LeaveStatus leaveStatus) {
            this.leaveStatus = leaveStatus;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }



        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }



        public ForUpdateLeaveDto build() {
            return new ForUpdateLeaveDto(this);
        }

    }
    public Long getLeaveNum() {
        return leaveNum;
    }

    public LeaveStatus getStatus() {
        return leaveStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public String getUserId() {
        return userId;
    }
}
