package org.example.server.dto.salary_dto;

import org.example.server.domain.user.User;

public class SalaryAddData {
    //현재 접속한 관리자와 관리자가 추가시킬 직원의 정보를 받아오게 됨.
    User AdminUser;
    User normalUser;

    public SalaryAddData(User admin, User normal){
        this.AdminUser = admin;
        this.normalUser=normal;
    }
    public User getNormalUser() {
        return normalUser;
    }

    public void setNormalUser(User normalUser) {
        this.normalUser = normalUser;
    }

    public User getAdminUser() {
        return AdminUser;
    }

    public void setAndminUser(User adminUser) {
        this.AdminUser = adminUser;
    }
}
