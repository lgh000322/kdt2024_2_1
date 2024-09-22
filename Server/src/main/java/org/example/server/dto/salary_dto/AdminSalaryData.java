package org.example.server.dto.salary_dto;
import org.example.server.domain.user.Role;

import java.time.LocalDate;


public class AdminSalaryData {
    private Long salaryNum; //월급번호 PK
    private String name; //월급을 지급받은 직원의이름
    private Long userNum;
    private String deptName;
    private String positionName;
    private int basicSalary; // 기본급




    private AdminSalaryData(Builder builder) {
        this.salaryNum = builder.salaryNum;
        this.name = builder.name;
        this.userNum = builder.userNum;
        this.deptName=builder.deptName;
        this.positionName=builder.positionName;
        this.basicSalary = builder.basicSalary;

    }// Getter methods


    public String getDeptName() {
        return deptName;
    }
    public Long getUserNum() {
        return userNum;
    }

    public String getPositionName() {
        return positionName;
    }



    public int getBasicSalary() {
        return basicSalary;
    }
    public Long getSalaryNum() {
        return salaryNum;
    }

    public String getName() {
        return name;
    }



    public static class Builder {
        private Long salaryNum;
        private String name;
        private Long userNum;
        private String deptName;
        private String positionName;
        private int basicSalary;




        public Builder UserNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public Builder basicSalary(int basicSalary) {
            this.basicSalary = basicSalary;
            return this;
        }



        public Builder deptName(String deptName) {
            this.deptName=deptName;
            return this;
        }

        public Builder positionName(String positionName) {
            this.positionName = positionName;
            return this;
        }

        public Builder salaryNum(Long salaryNum) {
            this.salaryNum = salaryNum;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }



        public AdminSalaryData build() {
            return new AdminSalaryData(this);
        }
    }
}