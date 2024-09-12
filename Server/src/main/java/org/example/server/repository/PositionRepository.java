package org.example.server.repository;

import org.example.server.dto.leave_dto.LeaveDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionRepository {
    private static PositionRepository positionRepository = null;

    public static PositionRepository createOrGetPositionRepository() {
        if (positionRepository == null) {
            positionRepository = new PositionRepository();
            return positionRepository;
        }

        return positionRepository;
    }

    public LeaveDay findPositionNumByPositionName(Connection con, String positionName) throws SQLException {

        String sql = "select * from position where position_name = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, positionName);
            rs = pstmt.executeQuery();

            LeaveDay leaveDay = new LeaveDay();
            if (rs.next()) {
                leaveDay.setPositionNum(rs.getLong("position_num"));
                leaveDay.setLeaveDay(rs.getInt("leave_day"));

            }
            return leaveDay;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    private static void close(PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
