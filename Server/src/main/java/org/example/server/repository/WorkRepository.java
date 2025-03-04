package org.example.server.repository;

import org.example.server.domain.user.User;
import org.example.server.domain.work_log.Status;
import org.example.server.domain.work_log.WorkLog;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UserWorkData;
import org.example.server.dto.work_dto.AdminWorkData;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkRepository {
    /**
     * 오전 8시 스케줄링에 사용되는 메소드. 회원의 근태로그를 미리 저장한다.
     *
     * @param con
     * @param status
     * @param time
     * @param localDate
     * @param userNum
     * @throws SQLException
     */
    public void save(Connection con, Status status, LocalTime time, LocalDate localDate, Long userNum) throws SQLException {
        String sql = "insert into work_log (start_time, end_time, status, work_date, user_num)" +
                " values (?, ?, ?, ?, ?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setTime(1, Time.valueOf(time));
            pstmt.setTime(2, null);
            pstmt.setString(3, status.name());
            pstmt.setDate(4, Date.valueOf(localDate));
            pstmt.setLong(5, userNum);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, null);
        }
    }

    public List<WorkLog> findWorkLogByDate(Connection conn, LocalDate today) throws SQLException {
        String sql = "select * from work_log where work_date = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(today));
            rs = pstmt.executeQuery();

            List<WorkLog> result = new ArrayList<>();

            while (rs.next()) {
                WorkLog workLog = new WorkLog.Builder()
                        .logNum(rs.getLong("log_num"))
                        .startTime(rs.getTime("start_time").toLocalTime())
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null)
                        .status(Status.valueOf(rs.getString("status").toUpperCase()))  // Enum 처리
                        .workDate(rs.getDate("work_date").toLocalDate())
                        .userNum(rs.getLong("user_num"))
                        .build();

                result.add(workLog);
            }

            return result;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }


    ///유저의 근퇴목록 모두 조회
    public ResponseData UserworkSearchAllonDB(User user, Connection conn) throws SQLException {
        /*
        2024-09-07수정
         */

        String sql = "select * from work_log where user_num = ?";

        List<UserWorkData> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserWorkData workLog = new UserWorkData.Builder()
                        .workNum(rs.getLong("log_num")) // 근퇴 기록 번호
                        .workDate(rs.getDate("work_date").toLocalDate()) // 출근 날짜
                        .status(Status.valueOf(rs.getString("status").toUpperCase())) // status 설정
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null) // 시작 시간, null 체크 후 LocalTime 변환
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null) // 퇴근 시간, null 체크 후 LocalTime 변환
                        .build();

                workLogs.add(workLog); // 리스트에 workLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("근퇴조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("근퇴조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }

    ///유저의 근퇴목록 근태상태로 조회
    public ResponseData findUserWorksByUserNumAndStatus(User user, Connection conn,Status status) throws SQLException {
        /*
        2024-09-07수정
         */

        String sql = "select * from work_log where user_num = ? and status = ?";

        List<UserWorkData> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setString(2,status.name());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserWorkData workLog = new UserWorkData.Builder()
                        .workNum(rs.getLong("log_num")) // 근퇴 기록 번호
                        .workDate(rs.getDate("work_date").toLocalDate()) // 출근 날짜
                        .status(Status.valueOf(rs.getString("status").toUpperCase())) // status 설정
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null) // 시작 시간, null 체크 후 LocalTime 변환
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null) // 퇴근 시간, null 체크 후 LocalTime 변환
                        .build();

                workLogs.add(workLog); // 리스트에 workLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("근퇴조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("근퇴조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }

    ///유저의 근태목록 근태날짜로 조회
    public ResponseData findUserWorksByUserNumAndDate(User user, Connection conn,LocalDate date) throws SQLException {
        /*
        2024-09-07수정
         */

        String sql = "select * from work_log where user_num = ? and work_date = ?";

        List<UserWorkData> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setDate(2,Date.valueOf(date));

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserWorkData workLog = new UserWorkData.Builder()
                        .workNum(rs.getLong("log_num")) // 근퇴 기록 번호
                        .workDate(rs.getDate("work_date").toLocalDate()) // 출근 날짜
                        .status(Status.valueOf(rs.getString("status").toUpperCase())) // status 설정
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null) // 시작 시간, null 체크 후 LocalTime 변환
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null) // 퇴근 시간, null 체크 후 LocalTime 변환
                        .build();

                workLogs.add(workLog); // 리스트에 workLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("근퇴조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("근퇴조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }


    ///유저의 근태목록 근태날짜와 근태상태로 조회
    public ResponseData findUserWorksByUserNumAndDateAndStatus(User user, Connection conn,LocalDate date,Status status) throws SQLException {
        /*
        2024-09-07수정
         */

        String sql = "select * from work_log where user_num = ? and work_date = ? and status = ?";

        List<UserWorkData> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setDate(2,Date.valueOf(date));
            pstmt.setString(3,status.name());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserWorkData workLog = new UserWorkData.Builder()
                        .workNum(rs.getLong("log_num")) // 근퇴 기록 번호
                        .workDate(rs.getDate("work_date").toLocalDate()) // 출근 날짜
                        .status(Status.valueOf(rs.getString("status").toUpperCase())) // status 설정
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null) // 시작 시간, null 체크 후 LocalTime 변환
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null) // 퇴근 시간, null 체크 후 LocalTime 변환
                        .build();

                workLogs.add(workLog); // 리스트에 workLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("근퇴조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("근퇴조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }
    ///관리자의 유저목록 모두 조회
    public ResponseData AdminworkSearchAllonDB(Connection conn) throws SQLException {
        /*
        2024-09-07수정
         */

        // 필요한 컬럼들만 선택하여 조회
        String sql = "SELECT user_num, name, tel, dept_num, position_num, email FROM user";

        List<AdminWorkData> workLogs = new ArrayList<>(); // 여러 개의 WorkLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AdminWorkData workLog = new AdminWorkData.Builder()
                        .userNum(rs.getLong("user_num")) // 사용자 번호
                        .name(rs.getString("name")) // 이름
                        .tel(rs.getString("tel")) // 전화번호
                        .deptNum(rs.getLong("dept_num")) // 부서번호
                        .position(rs.getLong("position_num")) // 직급번호
                        .email(rs.getString("email")) // 이메일
                        .build();

                workLogs.add(workLog); // 리스트에 AdminWorkData 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!workLogs.isEmpty()) {
                return new ResponseData("근퇴조회 성공", workLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("근퇴조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }


    ///////DB 출근 로직
    public ResponseData workStartonDB(User user, Connection conn, LocalTime startTime, Status status) throws SQLException {
        String sql = "INSERT INTO work_log (user_num, start_time, work_date, status) VALUES (?, ?, CURDATE(), ?)"; //업데이트로
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setTime(2, Time.valueOf(startTime));
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


    // 출근 시간 업데이트하는 메서드 (기존에 기록이 있는 경우)
    public ResponseData updateStartWorkLogonDB(WorkLog workLog, Connection con) throws SQLException {
        String updateSQL = "UPDATE work_log SET start_time = ?, status = ? WHERE user_num = ? AND work_date = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setTime(1, workLog.getStartTime() != null ? Time.valueOf(workLog.getStartTime()) : null);
            pstmt.setString(2, workLog.getStatus().name());
            pstmt.setLong(3, workLog.getUserNum());
            pstmt.setDate(4, Date.valueOf(workLog.getWorkDate()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("출근 시간 업데이트 성공", workLog);
            } else {
                return new ResponseData("출근 시간 업데이트 실패", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("출근 시간 업데이트 중 오류 발생", null);
        } finally {
            close(pstmt, null);
        }
    }

    // 퇴근 시간만 업데이트하는 메서드
    public ResponseData updateEndWorkLogonDB(WorkLog workLog, Connection con) throws SQLException {
        String updateSQL = "UPDATE work_log SET end_time = ?, status = ? WHERE user_num = ? AND work_date = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setTime(1, workLog.getEndTime() != null ? Time.valueOf(workLog.getEndTime()) : null);
            pstmt.setString(2,workLog.getStatus().name());
            pstmt.setLong(3, workLog.getUserNum());
            pstmt.setDate(4, Date.valueOf(workLog.getWorkDate()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new ResponseData("퇴근 기록 성공", workLog);
            } else {
                return new ResponseData("퇴근 기록 실패", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("퇴근 기록 중 오류 발생", null);
        } finally {
            close(pstmt, null);
        }
    }

    //근퇴로그 업데이트
    public ResponseData updateWorkLog(WorkLog workLog, Connection con) throws SQLException {
        String updateSQL = "UPDATE work_log SET start_time = ?, end_time = ?, status = ?, leave_num = ? WHERE log_num = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setTime(1, workLog.getStartTime() != null ? Time.valueOf(workLog.getStartTime()) : null);
            pstmt.setTime(2, workLog.getEndTime() != null ? Time.valueOf(workLog.getEndTime()) : null);
            pstmt.setString(3, workLog.getStatus().name());
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
        } finally {
            close(pstmt, null);
        }
    }

    // 스케줄러가 쓰는 업데이트
    public void updateStatus(WorkLog workLog, Connection con) throws SQLException {
        String updateSQL = "UPDATE work_log SET status = ? WHERE user_num = ? AND work_date = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setString(1, Status.ABSENCE.name());
            pstmt.setLong(2, workLog.getUserNum());
            pstmt.setDate(3, Date.valueOf(workLog.getWorkDate()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt, null);
        }
    }

    //특정 유저의 해당 날짜에 근퇴목록이 있는지 조회
    public Optional<WorkLog> findWorkLogByUserAndDate(Long userNum, LocalDate workDate, Connection con) throws SQLException {
        String selectSQL = "SELECT * FROM work_log WHERE user_num = ? AND work_date = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setLong(1, userNum);
            pstmt.setDate(2, Date.valueOf(workDate));

            rs = pstmt.executeQuery();
            if (rs.next()) {
                WorkLog workLog = new WorkLog.Builder()
                        .logNum(rs.getLong("log_num"))
                        .startTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null)
                        .endTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null)
                        .status(Status.valueOf(rs.getString("status").toUpperCase()))
                        .workDate(rs.getDate("work_date").toLocalDate())
                        .userNum(rs.getLong("user_num"))
                        .build();
                return Optional.of(workLog);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            close(pstmt, rs); // 자원 해제
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
