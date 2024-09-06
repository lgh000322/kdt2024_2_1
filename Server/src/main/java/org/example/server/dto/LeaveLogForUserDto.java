package org.example.server.dto;

import java.time.LocalDate;

public class LeaveLogForUserDto {

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

    public LeaveLogForUserDto(Builder builder) {
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

        public LeaveLogForUserDto build() {return new LeaveLogForUserDto(this);}

    }
}
