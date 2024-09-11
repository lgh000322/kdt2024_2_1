package org.example.server.dto.leave_dto;


import java.util.Date;

public class ForUpdateLeaveDto {

    private Long leaveNum;

    private Date startDate;

    private Date endDate;

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

        private Date startDate;

        private Date endDate;

        private String userId;

        public Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(Date endDate) {
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


    public String getUserId() {
        return userId;
    }
}
