package org.example.server.service.declare;

import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.salary_dto.BonusDto;

import java.sql.SQLException;

public interface SalaryService {

    ResponseData searchSalary(User user) throws SQLException;

    ResponseData addSalary() throws SQLException;

    ResponseData addBonus(BonusDto bonus) throws SQLException;


}
