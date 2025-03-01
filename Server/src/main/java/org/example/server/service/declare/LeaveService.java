package org.example.server.service.declare;

import org.example.server.dto.ResponseData;
import org.example.server.dto.leave_dto.ForFindLeaveDto;
import org.example.server.dto.leave_dto.ForRequestLeaveDto;
import org.example.server.dto.leave_dto.ForUpdateLeaveDto;

import java.sql.Connection;
import java.sql.SQLException;

public interface LeaveService {

    ResponseData findLeaveLog(ForFindLeaveDto forFindLeaveDto) throws SQLException;

    ResponseData updateLeaveLog(ForUpdateLeaveDto forUpdateLeaveDto) throws SQLException;

    ResponseData requestLeave(ForRequestLeaveDto forRequestLeaveDto) throws SQLException;

    ResponseData updateLeaveBizLogic(ForUpdateLeaveDto leaveLog, Connection conn) throws SQLException;

    ResponseData requestLeaveBizLogic(ForRequestLeaveDto forRequestLeaveDto, Connection conn) throws SQLException;

}
