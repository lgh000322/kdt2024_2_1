package org.example.server.dto.leave_dto;

import java.time.LocalDate;

public class ForRequestLeaveDto {

    private Long leaveNum;

    private LocalDate requestDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean acceptanceStatus;

    private Long userNum;

    private ForUpdateLeaveDto(ForUpdateLeaveDto.Builder builder) {
        this.leaveNum = builder.leaveNum;
        this.requestDate=builder.requestDate;
        this.startDate=builder.startDate;
        this.endDate=builder.endDate;
        this.acceptanceStatus=builder.acceptanceStatus;
        this.userNum = builder.userNum;
    }

    public static class Builder{
        private Long leaveNum;

        private LocalDate requestDate;

        private LocalDate startDate;

        private LocalDate endDate;

        private boolean acceptanceStatus;

        private Long userNum;

        public ForUpdateLeaveDto.Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public ForUpdateLeaveDto.Builder requestDate(LocalDate requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public ForUpdateLeaveDto.Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public ForUpdateLeaveDto.Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public ForUpdateLeaveDto.Builder acceptanceStatus(boolean acceptanceStatus) {
            this.acceptanceStatus = acceptanceStatus;
            return this;
        }

        public ForUpdateLeaveDto.Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public ForUpdateLeaveDto build() {
            return new ForUpdateLeaveDto(this);
        }

    }
    public Long getLeaveNum() {
        return leaveNum;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isAcceptanceStatus() {
        return acceptanceStatus;
    }

    public Long getUserNum() {
        return userNum;
    }
}
