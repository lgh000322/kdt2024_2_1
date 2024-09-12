package org.example.server.dto.leave_dto;

//db 날짜와 호환되는 date 라이브러리 임포트.


import java.time.LocalDate;
import java.util.Date;

/**
 *
 *  휴가 정보를 User 에게 보내기위한 dto
 * */
public class LeaveLogOfUserDto {

    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean status;


    public LocalDate getRequestDate() {
        return requestDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
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
        private LocalDate requestDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean status;

        public Builder requestDate(LocalDate requestDate) {
            this.requestDate = requestDate;
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

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public LeaveLogOfUserDto build() {return new LeaveLogOfUserDto(this);}

    }
}
