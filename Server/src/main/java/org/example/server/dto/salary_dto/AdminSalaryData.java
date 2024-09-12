package org.example.server.dto.salary_dto;
import org.example.server.domain.user.Role;


public class AdminSalaryData {
    private Long salaryNum; //월급번호 PK
    private String name; //월급을 지급받은 직원의이름
    private String tel; //해당 직원의 핸드폰번호
    private Long positionNum;
    private Role role; //월급을 지급받은 직원의 직책
    private int totalSalary; //지급한 총 월급액

    private AdminSalaryData(Builder builder) {
        this.salaryNum = builder.salaryNum;
        this.name = builder.name;
        this.tel = builder.tel;
        this.positionNum = builder.positionNum;
        this.role = builder.role;
        this.totalSalary = builder.totalSalary;
    }// Getter methods

    public Long getSalaryNum() {
        return salaryNum;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public Long getPositionNum() {
        return positionNum;
    }

    public Role getRole() {
        return role;
    }

    public int getTotalSalary() {
        return totalSalary;
    }// toString method

    @Override
    public String toString() {
        return "AdminSalaryData{" +
                "salaryNum=" + salaryNum +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", positionNum=" + positionNum +
                ", role=" + role +
                ", totalSalary=" + totalSalary +
                '}';
    }// Builder class

    public static class Builder {
        private Long salaryNum;
        private String name;
        private String tel;
        private Long positionNum;
        private Role role;
        private int totalSalary;

        public Builder salaryNum(Long salaryNum) {
            this.salaryNum = salaryNum;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder tel(String tel) {
            this.tel = tel;
            return this;
        }

        public Builder positionNum(Long positionNum) {
            this.positionNum = positionNum;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder totalSalary(int totalSalary) {
            this.totalSalary = totalSalary;
            return this;
        }

        public AdminSalaryData build() {
            return new AdminSalaryData(this);
        }
    }
}