package org.example.server.repository;

import org.example.server.domain.user.User;
import org.example.server.domain.work_log.Status;
import org.example.server.domain.work_log.WorkLog;
import org.example.server.dto.ResponseData;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkRepository {
    private static WorkRepository workRepository = null;

    public static WorkRepository createOrGetWorkRepository() {
        if (workRepository == null) {
            workRepository = new WorkRepository();
            System.out.println("WorkRepository 싱글톤 생성");
            return workRepository;
        }

        System.out.println("WorkRepository 싱글톤 반환");
        return workRepository;
    }

    ///유저의 근퇴목록 모두 조회
    public ResponseData DBWorkSearchAll(User user, Connection conn) throws SQLException {
        String sql = "select * from work_log where user_num = ?";

        List<WorkLog> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                WorkLog workLog = new WorkLog.Builder()
                        .logNum(rs.getLong("log_num")) // 근퇴 기록 번호
                        .startTime(rs.getTime("start_time").toLocalTime()) // 시작 시간, LocalTime 변환
                        .endTime(rs.getTime("end_time").toLocalTime()) // 퇴근 시간, LocalTime 변환
                        .status(Status.valueOf(rs.getString("status").toUpperCase())) // status 설정
                        .workDate(rs.getDate("work_date").toLocalDate()) // 출근 날짜
                        .userNum(rs.getLong("user_num")) // 유저번호
                        .leaveNum(rs.getLong("leave_num")) // 휴가 번호
                        .build();

                workLogs.add(workLog); // 리스트에 SalaryLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("월급조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("월급조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }


    ///////DB 출근 로직
    public ResponseData DBWorkStart(User user, Connection conn, LocalTime startTime, Status status) throws SQLException {
        String sql = "INSERT INTO work_log (user_num, start_time, work_date, status) VALUES (?, ?, CURDATE(), ?)";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setTime(2, java.sql.Time.valueOf(startTime));
            pstmt.setString(3, status.name());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("출근 업데이트 성공", null);
            } else {
                return new ResponseData("출근 업데이트 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }
    }


    //////////// DB 퇴근 로직
    public ResponseData DBWorkEnd(User user, Connection conn, LocalTime endTime, Status status) throws SQLException {
        String sql = "UPDATE work_log SET end_time = ?, status = ? WHERE user_num = ? AND work_date = CURDATE()";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setTime(1, java.sql.Time.valueOf(endTime));
            pstmt.setString(2, status.name());
            pstmt.setLong(3, user.getUserNum());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("퇴근 업데이트 성공", null);
            } else {
                return new ResponseData("퇴근 업데이트 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }
    }

    ///////////////// DB 조퇴 로직
    public ResponseData DBWorkEndEarly(User user, Connection conn, LocalTime endTime, Status status) throws SQLException {
        String sql = "UPDATE work_log SET end_time = ?, status = ? WHERE user_num = ? AND work_date = CURDATE()";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setTime(1, java.sql.Time.valueOf(endTime));
            pstmt.setString(2, status.name());
            pstmt.setLong(3, user.getUserNum());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("조퇴 업데이트 성공", null);
            } else {
                return new ResponseData("조퇴 업데이트 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }
    }

    //근퇴로그 생성
    public ResponseData DBCreateWorkLog(WorkLog workLog, Connection con) throws SQLException {
        String insertSQL = "INSERT INTO work_log (start_time, end_time, status, work_date, user_num, leave_num) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
            pstmt.setTime(1, workLog.getStartTime() != null ? Time.valueOf(workLog.getStartTime()) : null);
            pstmt.setTime(2, workLog.getEndTime() != null ? Time.valueOf(workLog.getEndTime()) : null);
            pstmt.setString(3, workLog.getStatus().name());
            pstmt.setDate(4, Date.valueOf(workLog.getWorkDate()));
            pstmt.setLong(5, workLog.getUserNum());
            pstmt.setLong(6, workLog.getLeaveNum() != null ? workLog.getLeaveNum() : null);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("WorkLog 생성 성공", workLog);
            } else {
                return new ResponseData("WorkLog 생성 실패", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("WorkLog 생성 중 오류 발생", null);
        }
    }
    
    //근퇴로그 업데이트
    public ResponseData updateWorkLog(WorkLog workLog, Connection con) throws SQLException {
        String updateSQL = "UPDATE work_log SET start_time = ?, end_time = ?, status = ?, leave_num = ? WHERE log_num = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateSQL)) {
            pstmt.setTime(1, workLog.getStartTime() != null ? Time.valueOf(workLog.getStartTime()) : null);
            pstmt.setTime(2, workLog.getEndTime() != null ? Time.valueOf(workLog.getEndTime()) : null);
            pstmt.setString(3, workLog.getStatus().name());
            pstmt.setLong(4, workLog.getLeaveNum() != null ? workLog.getLeaveNum() : null);
            pstmt.setLong(5, workLog.getLogNum());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("WorkLog 업데이트 성공", workLog);
            } else {
                return new ResponseData("WorkLog 업데이트 실패", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("WorkLog 업데이트 중 오류 발생", null);
        }
    }


    //특ㅈ겅 유저의 해당 날짜에 근퇴목록이 있는지 조회
    public Optional<WorkLog> findWorkLogByUserAndDate(Long userNum, LocalDate workDate, Connection con) throws SQLException {
        String selectSQL = "SELECT * FROM work_log WHERE user_num = ? AND work_date = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSQL)) {
            pstmt.setLong(1, userNum);
            pstmt.setDate(2, Date.valueOf(workDate));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                WorkLog workLog = new WorkLog.Builder()
                        .logNum(rs.getLong("log_num"))
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null)
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null)
                        .status(Status.valueOf(rs.getString("status")))
                        .workDate(rs.getDate("work_date").toLocalDate())
                        .userNum(rs.getLong("user_num"))
                        .leaveNum(rs.getLong("leave_num"))
                        .build();
                return Optional.of(workLog);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
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
