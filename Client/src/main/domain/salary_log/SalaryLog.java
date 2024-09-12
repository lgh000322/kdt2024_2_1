package main.domain.salary_log;

import java.time.LocalDate;

public class SalaryLog {
    private Long salaryNum;

    private LocalDate receivedData;

    private int totalSalary;

    private Long userNum;

    private SalaryLog(Builder builder) {
        this.salaryNum = builder.salaryNum;
        this.receivedData = builder.receivedData;
        this.totalSalary = builder.totalSalary;
        this.userNum = builder.userNum;
    }

    public static class Builder{
        private Long salaryNum;

        private LocalDate receivedData;

        private int totalSalary;

        private Long userNum;

        public Builder salaryNum(Long salaryNum) {
            this.salaryNum = salaryNum;
            return this;
        }

        public Builder receivedData(LocalDate receivedData) {
            this.receivedData = receivedData;
            return this;
        }

        public Builder totalSalary(int totalSalary) {
            this.totalSalary = totalSalary;
            return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public SalaryLog build(){
            return new SalaryLog(this);
        }

    }
    public Long getSalaryNum() {
        return salaryNum;
    }

    public LocalDate getReceivedData() {
        return receivedData;
    }

    public int getTotalSalary() {
        return totalSalary;
    }

    public Long getUserNum() {
        return userNum;
    }
}
