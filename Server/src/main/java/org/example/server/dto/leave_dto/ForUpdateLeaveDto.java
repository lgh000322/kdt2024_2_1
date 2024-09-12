package org.example.server.dto.leave_dto;


import java.time.LocalDate;
import java.util.Date;

public class ForUpdateLeaveDto {

    private Long leaveNum;

    private LocalDate startDate;

    private LocalDate endDate;

    private String userId;

    private Status status;

    private ForUpdateLeaveDto(Builder builder) {
        this.leaveNum = builder.leaveNum;
        this.status = builder.status;
        this.startDate=builder.startDate;
        this.endDate=builder.endDate;

        this.userId = builder.userId;

    }

    public static class Builder{
        private Long leaveNum;

        private Status status;

        private LocalDate startDate;

        private LocalDate endDate;

        private String userId;

        public Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
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

    public Status getStatus() {
        return status;
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
