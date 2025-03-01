package org.example.server.service.declare;

import java.sql.SQLException;

public interface SchedulerService {
    void morningAct() throws SQLException;

    void eveningAct() throws SQLException;

    void everyMonthAct() throws SQLException;
}
