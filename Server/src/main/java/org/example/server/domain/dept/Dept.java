package org.example.server.domain.dept;

public class Dept {
    private Long deptNum;

    private String deptName;

    private Dept(Builder builder) {
        this.deptNum = builder.deptNum;
        this.deptName = builder.deptName;
    }

    public static class Builder{
        private Long deptNum;

        private String deptName;

        public Builder deptNum(Long deptNum) {
            this.deptNum = deptNum;
            return this;
        }

        public Builder deptName(String deptName) {
            this.deptName = deptName;
            return this;
        }

        public Dept build() {
            return new Dept(this);
        }
    }

    public Long getDeptNum() {
        return deptNum;
    }

    public String getDeptName() {
        return deptName;
    }
}
