package main.domain.user;

import java.time.LocalDate;

public class User {
    private Long userNum;

    private String userId;

    private String password;

    private String name;

    private String tel;

    private String email;

    private Role role;

    private int remainedLeave;

    private Long positionNum;

    private Long deptNum;
    
    private LocalDate moneySearchDate;
    
    private String searchUserName;
    
    public String getSearchUserName() {
    	return searchUserName;
    }
    
    public LocalDate getMoneySearchDate() {
    	return moneySearchDate;
    }

    public Long getUserNum() {
        return userNum;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public int getRemainedLeave() {
        return remainedLeave;
    }

    public Long getPositionNum() {
        return positionNum;
    }

    public Long getDeptNum() {
        return deptNum;
    }

    private User(Builder builder) {
        this.userNum=builder.userNum;
        this.userId=builder.userId;
        this.password=builder.password;
        this.name=builder.name;
        this.tel=builder.tel;
        this.email=builder.email;
        this.role=builder.role;
        this.remainedLeave=builder.remainedLeave;
        this.positionNum=builder.positionNum;
        this.deptNum = builder.deptNum;
        this.moneySearchDate=builder.moneySearchDate;
        this.searchUserName=builder.searchUserName;
    }

    public static class Builder{
        private Long userNum;

        private String userId;

        private String password;

        private String name;

        private String tel;

        private String email;

        private Role role;

        private int remainedLeave;

        private Long positionNum;

        private Long deptNum;
        
        private LocalDate moneySearchDate;
        
        private String searchUserName;
        
        public Builder searchUserName(String searchUserName) {
        	this.searchUserName=searchUserName;
        	return this;
        }
        
        public Builder moneySearchDate(LocalDate moneySearchDate) {
        	this.moneySearchDate=moneySearchDate;
        	return this;
        }

        public Builder userNum(Long userNum) {
            this.userNum = userNum;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
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

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder remainedLeave(int remainedLeave) {
            this.remainedLeave = remainedLeave;
            return this;
        }

        public Builder positionNum(Long positionNum) {
            this.positionNum = positionNum;
            return this;
        }

        public Builder deptNum(Long deptNum) {
            this.deptNum = deptNum;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
