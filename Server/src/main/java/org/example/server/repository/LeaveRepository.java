package org.example.server.repository;

import org.example.server.domain.leave_log.LeaveLog;
import org.example.server.dto.leave_dto.ForRequestLeaveDto;
import org.example.server.dto.leave_dto.ForUpdateLeaveDto;
import org.example.server.dto.leave_dto.LeaveLogOfAdminDto;
import org.example.server.dto.leave_dto.LeaveLogOfUserDto;

import java.sql.*;
import java.time.LocalDate;
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
    public Long createLeave(ForRequestLeaveDto forRequestLeaveDto, Connection conn) throws SQLException {
        String sql = "insert into leave_log " +
                "(request_date, start_date, end_date, acceptance_status, user_num)" +
                " values (?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = null;

        java.sql.Date requestDate = java.sql.Date.valueOf(forRequestLeaveDto.getRequestDate());
        java.sql.Date startDate = java.sql.Date.valueOf(forRequestLeaveDto.getStartDate());
        java.sql.Date endDate = java.sql.Date.valueOf(forRequestLeaveDto.getEndDate());
        boolean status = forRequestLeaveDto.isAcceptanceStatus();
        Long userNum = forRequestLeaveDto.getUserNum();

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
    *  휴가 상태 업데이트 (관리자가 실행)
    * */
    public int updateLeave(ForUpdateLeaveDto forUpdateLeaveDto, Connection conn) throws SQLException {
        PreparedStatement ps = null;

        String sql = "update leave_log set " +
                "acceptance_status = ? where leave_num = ?";

        int row = 0;

        try{
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, forUpdateLeaveDto.isAcceptanceStatus());
            ps.setLong(2,forUpdateLeaveDto.getLeaveNum());
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        close(ps, null);

        return row; // 0 일경우 수정실패 , 0이 아니면 수정성공
    }


    /**
    * 휴가 조회 -> admin 의 휴가 조회. ( 모든 유저 휴가조회)
    * */
    public List<LeaveLogOfAdminDto> getLeaveLogOfAdmin(Connection conn) throws SQLException {
        LeaveLogOfAdminDto leaveLogOfAdminDto = null;
        List<LeaveLogOfAdminDto> leaveLogOfAdminDtos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        //모든 leave_log를 가져오기위한 쿼리
        String sql = "select request_date, start_date, end_date, acceptance_status" +
                " from leave_log";

        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
               leaveLogOfAdminDto = new LeaveLogOfAdminDto.Builder()
                        .requestDate(rs.getDate("request_date"))
                        .startDate(rs.getDate("start_date"))
                        .endDate(rs.getDate("end_date"))
                        .status(rs.getBoolean("acceptance_status"))
                        .build();

                leaveLogOfAdminDtos.add(leaveLogOfAdminDto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return leaveLogOfAdminDtos;
    }


    /**
    *  휴가 조회 -> admin의 휴가조회 (특정 유저 휴가조회)
    * */


    /**
     * 회원번호와 오늘 날짜로 휴가허가가 난 사람이 있는지
     */

    public LeaveLog findByUserNum(Connection con, Long userNum, LocalDate now) throws SQLException {
        Date date = Date.valueOf(now);
        String sql = "select * from leave_log" +
                " where user_num = ? and" +
                " start_date >= ? and" +
                " end_date <= ? and" +
                " acceptance_status = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1,userNum);
            pstmt.setDate(2,date);
            pstmt.setDate(3,date);
            pstmt.setBoolean(4, true);
            rs = pstmt.executeQuery();


            LeaveLog leaveLog = null;
            if (rs.next()) {
                leaveLog = new LeaveLog.Builder()
                        .leaveNum(rs.getLong("leave_num"))
                        .requestDate(rs.getDate("request_date").toLocalDate())
                        .startDate(rs.getDate("start_date").toLocalDate())
                        .endDate(rs.getDate("end_date").toLocalDate())
                        .acceptanceStatus(rs.getBoolean("acceptance_status"))
                        .userNum(rs.getLong("user_num"))
                        .build();
            }

            return leaveLog;

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    /**
    * 휴가 조회 -> user 의 휴가조회.
    * */
    public List<LeaveLogOfUserDto> getLeaveLogsOfUser(String userId, Connection conn) throws SQLException {

        LeaveLogOfUserDto leaveLogOfUserDto;
        List<LeaveLogOfUserDto> leaveLogOfUserDtos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select request_date, start_date, end_date, acceptance_status " +
                "from leave_log where user_id = ?";

        try{

            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                leaveLogOfUserDto = new LeaveLogOfUserDto.Builder()
                        .requestDate(rs.getDate("request_date"))
                        .startDate(rs.getDate("start_date"))
                        .endDate(rs.getDate("end_date"))
                        .status(rs.getBoolean("acceptance_status"))
                        .build();

                leaveLogOfUserDtos.add(leaveLogOfUserDto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }
        return leaveLogOfUserDtos;
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
