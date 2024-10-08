package main.domain.leave_log;


import java.time.LocalDate;

public class LeaveLog {
    private Long leaveNum;

    private LocalDate requestDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean acceptanceStatus;

    private Long userNum;

    private LeaveLog(Builder builder) {
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

        public Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

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

        public Builder acceptanceStatus(boolean acceptanceStatus) {
            this.acceptanceStatus = acceptanceStatus;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public LeaveLog build() {
            return new LeaveLog(this);
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
