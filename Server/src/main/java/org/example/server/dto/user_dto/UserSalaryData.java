package org.example.server.dto;
import java.time.LocalDate;

public class UserSalaryData {

    private Long salaryNum; // PK 월급번호
    private LocalDate receivedDate; // 지급받은 날
    private int totalSalary; // 받은 총 월급

    private UserSalaryData(Builder builder) {
        this.salaryNum = builder.salaryNum;
        this.receivedDate = builder.receivedDate;
        this.totalSalary = builder.totalSalary;
    }// Getter methods

    public Long getSalaryNum() {
        return salaryNum;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public int getTotalSalary() {
        return totalSalary;
    }// toString method

    @Override
    public String toString() {
        return "UserSalaryData{" +
                "salaryNum=" + salaryNum +
                ", receivedDate=" + receivedDate +
                ", totalSalary=" + totalSalary +
                '}';
    }

    public static class Builder {
        private Long salaryNum;
        private LocalDate receivedDate;
        private int totalSalary;

        public Builder salaryNum(Long salaryNum) {
            this.salaryNum = salaryNum;
            return this;
        }

        public Builder receivedDate(LocalDate receivedDate) {
            this.receivedDate = receivedDate;
            return this;
        }

        public Builder totalSalary(int totalSalary) {
            this.totalSalary = totalSalary;
            return this;
        }

        public UserSalaryData build() {
            return new UserSalaryData(this);
        }
    }
}