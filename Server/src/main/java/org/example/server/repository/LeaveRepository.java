package org.example.server.repository;

import org.example.server.domain.leave_log.LeaveLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveRepository {
    private static LeaveRepository leaveRepository = null;

    //싱글톤으로 휴가 레포 생성 -> 기존 레포가 있을경우 기존 레포 사용
    public static LeaveRepository createOrGetLeaveRepository() {

        if (leaveRepository == null) {
            leaveRepository = new LeaveRepository();
            System.out.println("Leave Repository 싱글톤 생성");
            return leaveRepository;
        }

        System.out.println("LeaveRepository 싱글톤 반환");
        return leaveRepository;
    }

    /**
    *  휴가신청
    * */
    public Long createLeave(LeaveLog leaveLog, Connection conn) throws SQLException {
        String sql = "insert into leave_log " +
                "(request_date, start_date, end_date, acceptance_status, user_num)" +
                " values (?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = null;

        java.sql.Date requestDate = java.sql.Date.valueOf(leaveLog.getRequestDate());
        java.sql.Date startDate = java.sql.Date.valueOf(leaveLog.getStartDate());
        java.sql.Date endDate = java.sql.Date.valueOf(leaveLog.getEndDate());
        boolean status = leaveLog.isAcceptanceStatus();
        Long userNum = leaveLog.getUserNum();

        try {
            ps.setDate(1, requestDate);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            ps.setBoolean(4, status);
            ps.setLong(5, userNum);

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("휴가 신청 실패.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }

    }


    /**
    *  휴가 상태 업데이트 ( 관리자가 실행)
    * */
    public int updateLeave(LeaveLog leaveLog, Connection conn) throws SQLException {
        PreparedStatement ps = null;

        String sql = "update leave_log set " +
                "acceptance_status = ? where leave_num = ?";

        int row = 0;

        try{
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, leaveLog.isAcceptanceStatus());
            ps.setLong(2,leaveLog.getLeaveNum());
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        close(ps, null);

        return row; // 0 일경우 수정실패 , 0이 아니면 수정성공
    }



    /**
    * 휴가 조회
    * */
    public List<LeaveLog> getLeaveLogs(Connection conn) throws SQLException {

        LeaveLog leaveLog;
        List<LeaveLog> leaveLogs = new ArrayList<LeaveLog>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select * from leave_log";

        try{

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                leaveLog = new LeaveLog.Builder()
                        .requestDate(rs.getDate("request_date").toLocalDate())
                        .startDate(rs.getDate("start_date").toLocalDate())
                        .endDate(rs.getDate("end_date").toLocalDate())
                        .acceptanceStatus(rs.getBoolean("acceptance_status"))
                        .userNum(rs.getLong("user_num"))
                        .build();

                leaveLogs.add(leaveLog);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }
        return leaveLogs;
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
