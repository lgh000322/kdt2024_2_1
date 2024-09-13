package org.example.server.dto.leave_dto;

import java.sql.Date;
import java.time.LocalDate;

/**
*
*  휴가 정보를 Admin 에게 보내기위한 dto -> 수정 필요.
* */
public class LeaveLogOfAdminDto
{


    private Long leaveNum;
    private String userName;
    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String deptName;
    private Boolean status;
    private Integer remainedLeave;

    public String getUserName() {
        return userName;
    }

    public Long getLeaveNum() {
        return leaveNum;
    }

    public String getDeptName() {
        return deptName;
    }

    public Integer getRemainedLeave() {return remainedLeave;}

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

    public LeaveLogOfAdminDto(LeaveLogOfAdminDto.Builder builder) {
        this.requestDate = builder.requestDate;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
        this.deptName = builder.deptName;
        this.leaveNum = builder.leaveNum;
        this.userName = builder.userName;
        this.remainedLeave = builder.remainedLeave;
    }

    public static class Builder {
        private LocalDate requestDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean status;
        private String deptName;
        private Integer remainedLeave;
        private Long leaveNum;
        private String userName;

        public LeaveLogOfAdminDto.Builder requestDate(LocalDate requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder remainedLeave(Integer remainedLeave) {
            this.remainedLeave = remainedLeave;
            return this;
        }

        public LeaveLogOfAdminDto.Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public LeaveLogOfAdminDto.Builder deptName(String deptName) {
            this.deptName = deptName;
            return this;
        }


        public LeaveLogOfAdminDto.Builder leaveNum(Long leaveNum) {
            this.leaveNum = leaveNum;
            return this;
        }

        public LeaveLogOfAdminDto.Builder userName(String userName) {
            this.userName = userName;
            return this;
        }



        public LeaveLogOfAdminDto build() {
            return new LeaveLogOfAdminDto(this);
        }

    }
}
