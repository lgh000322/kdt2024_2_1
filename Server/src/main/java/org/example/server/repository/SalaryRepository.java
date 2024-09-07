package org.example.server.repository;

import org.example.server.domain.salary_log.SalaryLog;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.salary_dto.AdminSalaryData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.user_dto.UserSalaryData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryRepository {
    private static SalaryRepository salaryRepository = null;

    public static SalaryRepository createOrGetSalaryRepository() {
        if (salaryRepository == null) {
            salaryRepository = new SalaryRepository();
            System.out.println("SalaryRepository 싱글톤 생성");
            return salaryRepository;
        }

        System.out.println("SalaryRepository 싱글톤 반환");
        return salaryRepository;
    }

    //DB에 월급내역을 등록하는 메소드
    public ResponseData insertSalaryonDB(Connection conn, User user, SalaryLog salaryLog) throws SQLException {
        String sql = "INSERT INTO salary_log (received_date, total_salary, user_num) VALUES (?, ?, ?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            // PreparedStatement에 값 설정
            pstmt.setDate(1, Date.valueOf(salaryLog.getReceivedData())); // LocalDate -> java.sql.Date 변환
            pstmt.setInt(2, salaryLog.getTotalSalary());
            pstmt.setLong(3, user.getUserNum());

            // SQL 쿼리 실행
            int rowsAffected = pstmt.executeUpdate();

            // 삽입 성공 여부에 따라 적절한 메시지 반환
            if (rowsAffected > 0) {
                return new ResponseData("월급 내역 등록 성공", salaryLog);
            } else {
                return new ResponseData("월급 내역 등록 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, null); // PreparedStatement 자원 해제, ResultSet은 사용되지 않으므로 null 전달
        }
    }


    //일반 유저의 월급을 모두 조회시키는 메소드
    public ResponseData searchSalaryUserOnDB(Connection conn, User user) throws SQLException {
        /*
        2024-09-07수정
         */
        String sql = "select * from salary_log where user_num = ?";

        List<UserSalaryData> salaryLogs = new ArrayList<>(); // 여러 개의 SalaryLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // SalaryLog 객체를 생성하여 리스트에 추가
                UserSalaryData userSalary = new UserSalaryData.Builder()
                        .salaryNum(rs.getLong("salary_num"))
                        .receivedDate(rs.getDate("received_date").toLocalDate()) // LocalDate 변환
                        .totalSalary(rs.getInt("total_salary"))
                        .build();

                salaryLogs.add(userSalary); // 리스트에 SalaryLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!salaryLogs.isEmpty()) {
                return new ResponseData("일반직원 월급조회 성공", salaryLogs);
            } else {
                // 조회된 결과가 없을 때 적절한 메시지 반환
                return new ResponseData("일바직원 월급조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }

    public ResponseData searchSalaryAdminOnDB(Connection conn) throws SQLException {
        /*
        2024-09-07수정
         */
        // salary_log와 user 테이블을 JOIN해서 필요한 데이터를 가져옵니다.
        String sql = "SELECT s.salary_num, s.total_salary, s.received_date, u.name, u.tel, u.position_num, u.role " +
                "FROM salary_log s " +
                "JOIN user u ON s.user_num = u.user_num";  // user 테이블과 salary_log를 user_num을 기준으로 조인

        List<AdminSalaryData> salaryLogs = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Role 필드를 String으로 읽어와서 Role Enum으로 변환
                String roleString = rs.getString("role");
                Role role = Role.fromString(roleString);

                // AdminSalaryData 객체를 생성하여 리스트에 추가
                AdminSalaryData adminSalary = new AdminSalaryData.Builder()
                        .salaryNum(rs.getLong("salary_num"))
                        .name(rs.getString("name"))
                        .tel(rs.getString("tel"))
                        .positionNum(rs.getLong("position_num"))  // position_num 확인
                        .role(role)
                        .totalSalary(rs.getInt("total_salary"))
                        .build();

                salaryLogs.add(adminSalary);
            }

            if (!salaryLogs.isEmpty()) {
                return new ResponseData("관리자 월급조회 성공", salaryLogs);
            } else {
                return new ResponseData("관리자 월급조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
