package org.example.server.dto;

//db 날짜와 호환되는 date 라이브러리 임포트.
import java.sql.Date;


/**
 *
 *  휴가 정보를 User 에게 보내기위한 dto
 * */
public class LeaveLogOfUserDto {

    private Date requestDate;
    private Date startDate;
    private Date endDate;
    private Boolean status;


    public Date getRequestDate() {
        return requestDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public LeaveLogOfUserDto(Builder builder) {
        this.requestDate = builder.requestDate;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
    }

    public static class Builder {
        private Date requestDate;
        private Date startDate;
        private Date endDate;
        private Boolean status;

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

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public LeaveLogOfUserDto build() {return new LeaveLogOfUserDto(this);}

    }
}
