package org.example.server.dto.salary_dto;

public class UserSalaryDto {

    Long userNum;
    int userSalary;

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public int getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(int userSalary) {
        this.userSalary = userSalary;
    }
}
