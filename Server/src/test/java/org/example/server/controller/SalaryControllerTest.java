/*
package org.example.server.controller;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.service.SalaryService;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class SalaryControllerTest {
    SalaryService service = SalaryService.createOrGetSalaryService();

    @Test
    void 조회 () throws SQLException {
        User user = new User.Builder()
                .userId("dlrudgns")
                .password("dlrudgns")
                .name("이경훈")
                .tel("010-1111-1111")
                .email("aaa@aaa.com")
                .role(Role.USER)
                .remainedLeave(15)
                .positionNum(1L)
                .deptNum(1L)
                .build();

        ResponseData responseData = service.searchSalary(user);
        System.out.println(responseData.getMessageType() + "obj:" + responseData.getData());
    }
}*/
