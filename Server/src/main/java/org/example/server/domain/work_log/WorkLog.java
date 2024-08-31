package org.example.server.domain.work_log;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkLog {
    private Long logNum;

    private LocalTime startTime;

    private LocalTime endTime;

    private Status status;

    private LocalDate workDate;

    private Long userNum;

    private Long leaveNum;

    private WorkLog(Builder builder) {
        this.logNum = builder.logNum;
        this.startTime=builder.startTime;
        this.endTime = builder.endTime;
        this.status = builder.status;
        this.workDate=builder.workDate;
        this.userNum=builder.userNum;
        this.leaveNum = builder.leaveNum;
    }

    public static class Builder{
        private Long logNum;

        private LocalTime startTime;

        private LocalTime endTime;

        private Status status;

        private LocalDate workDate;

        private Long userNum;

        private Long leaveNum;

        public Builder logNum(Long logNum) {
            this.logNum = logNum;
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

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder workDate(LocalDate workDate) {
            this.workDate = workDate;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public WorkLog build(){
            return new WorkLog(this);
        }
    }
    public Long getLogNum() {
        return logNum;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public Long getUserNum() {
        return userNum;
    }

    public Long getLeaveNum() {
        return leaveNum;
    }
}