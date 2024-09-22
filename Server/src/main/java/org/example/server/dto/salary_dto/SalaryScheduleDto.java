package org.example.server.dto.salary_dto;

public class SalaryScheduleDto {
    private Long userNum;

    private int remainedLeave;

    private int basicSalary;

    private int leavePay;

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public int getRemainedLeave() {
        return remainedLeave;
    }

    public void setRemainedLeave(int remainedLeave) {
        this.remainedLeave = remainedLeave;
    }

    public int getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(int basicSalary) {
        this.basicSalary = basicSalary;
    }

    public int getLeavePay() {
        return leavePay;
    }

    public void setLeavePay(int leavePay) {
        this.leavePay = leavePay;
    }
}
