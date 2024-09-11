package org.example.server.dto.leave_dto;

import java.time.LocalDate;
import java.util.Date;

public class ForRequestLeaveDto {



    private Date requestDate;

    private Date startDate;

    private Date endDate;

    private String userId;

    private ForRequestLeaveDto(Builder builder) {

        this.requestDate=builder.requestDate;
        this.startDate=builder.startDate;
        this.endDate=builder.endDate;

        this.userId = builder.userId;
    }

    public static class Builder{


        private Date requestDate;

        private Date startDate;

        private Date endDate;



        private String userId;


        public Builder requestDate(Date requestDate) {
            this.requestDate = requestDate;
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

        public ForRequestLeaveDto build() {
            return new ForRequestLeaveDto(this);
        }

    }


    public Date getRequestDate() {
        return requestDate;
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
