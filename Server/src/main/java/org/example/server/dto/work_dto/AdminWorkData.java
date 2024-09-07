package org.example.server.dto;

public class AdminWorkData {
    private Long userNum; // 근퇴번호 PK
    private String name; // 근퇴기록의 해당 직원이름
    private String tel; // 직원의 전화번호
    private Long deptNum; // 부서번호
    private Long position; // 직급번호
    private String email; // 이메일

    private AdminWorkData(Builder builder) {
        this.userNum = builder.userNum;
        this.name = builder.name;
        this.tel = builder.tel;
        this.deptNum = builder.deptNum;
        this.position = builder.position;
        this.email = builder.email;
    }
    @Override
    public String toString() {
        return "AdminWorkData{" +
                "workNum=" + userNum +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", deptNum=" + deptNum +
                ", position=" + position +
                ", email='" + email + '\'' +
                '}';
    }

    public static class Builder {
        private Long userNum;
        private String name;
        private String tel;
        private Long deptNum;
        private Long position;
        private String email;

        public Builder userNum(Long workNum) {
            this.userNum = workNum;
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

        public Builder deptNum(Long deptNum) {
            this.deptNum = deptNum;
            return this;
        }

        public Builder position(Long position) {
            this.position = position;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public AdminWorkData build() {
            return new AdminWorkData(this);
        }

        public Long getUserNum() {
            return userNum;
        }

        public String getName() {
            return name;
        }

        public String getTel() {
            return tel;
        }

        public Long getDeptNum() {
            return deptNum;
        }

        public Long getPosition() {
            return position;
        }

        public String getEmail() {
            return email;
        }

    }
}