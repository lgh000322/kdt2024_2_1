package org.example.server.repository;

import org.example.server.domain.leave_log.LeaveLog;
import org.example.server.dto.leave_dto.*;

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
    public Long createLeave(ForRequestLeaveDto forRequestLeaveDto, Long userNum, Connection conn) throws SQLException {
        String sql = "insert into leave_log " +
                "(request_date, start_date, end_date, user_num)" +
                " values (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = null;

        java.sql.Date requestDate = java.sql.Date.valueOf(forRequestLeaveDto.getRequestDate());
        java.sql.Date startDate = java.sql.Date.valueOf(forRequestLeaveDto.getStartDate());
        java.sql.Date endDate = java.sql.Date.valueOf(forRequestLeaveDto.getEndDate());


        try {
            ps.setDate(1, requestDate);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            ps.setLong(4, userNum);

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

    // 휴가 승락시 실행 -> acceptance_status와 check_status를 모두 true로 바꿔줌
    public int acceptLeave(Long leaveNum, Connection conn) throws SQLException {
        PreparedStatement ps = null;

        String sql = "update leave_log set " +
                "acceptance_status = ?, check_status = ? where leave_num = ?";

        int row = 0;

        try{
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setBoolean(2, true);
            ps.setLong(3, leaveNum);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        close(ps, null);

        return row; // 0 일경우 수정실패 , 0이 아니면 수정성공
    }

    //휴가 거절시 실행 -> 체크스테이터스만 true로 바꿔줌.
    public int rejectLeave(Long leaveNum, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        String sql = "update leave_log set check_status = ? where leave_num = ?";

        int row = 0;

        try{
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setLong(2, leaveNum);
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
        String sql = "select l.leave_num, u.name, l.request_date, l.start_date, l.end_date, p.position_name, l.acceptance_status" +
                " from leave_log l inner join user u on l.user_num = u.user_num" +
                "inner join position p on u.position_num = p.position_num";

        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                leaveLogOfAdminDto = new LeaveLogOfAdminDto.Builder()
                        .leaveNum(rs.getLong(1))
                        .userName(rs.getString("name"))
                        .requestDate(rs.getDate("request_date"))
                        .startDate(rs.getDate("start_date"))
                        .endDate(rs.getDate("end_date"))
                        .deptName(rs.getString("dept_name"))
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
    public List<LeaveLogOfAdminDto> getMatchedByUserName(String userName, Connection conn) throws SQLException {

        LeaveLogOfAdminDto leaveLogOfAdminDto = null;
        List<LeaveLogOfAdminDto> leaveLogOfAdminDtos = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        //모든 leave_log를 가져오기위한 쿼리
        String sql = "select l.leave_num, u.name, l.request_date, l.start_date, l.end_date, p.position_name, l.acceptance_status" +
                " from leave_log l inner join user u on l.user_num = u.user_num" +
                "inner join position p on u.position_num = p.position_num";

        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                leaveLogOfAdminDto = new LeaveLogOfAdminDto.Builder()
                        .leaveNum(rs.getLong(1))
                        .userName(rs.getString("name"))
                        .requestDate(rs.getDate("request_date"))
                        .startDate(rs.getDate("start_date"))
                        .endDate(rs.getDate("end_date"))
                        .deptName(rs.getString("dept_name"))
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
