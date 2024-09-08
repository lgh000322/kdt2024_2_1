package org.example.server.dto.leave_dto;

public class LeaveDay {
    private Long positionNum;

    private int leaveDay;

    public Long getPositionNum() {
        return positionNum;
    }

    public void setPositionNum(Long positionNum) {
        this.positionNum = positionNum;
    }

    public int getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(int leaveDay) {
        this.leaveDay = leaveDay;
    }
}
