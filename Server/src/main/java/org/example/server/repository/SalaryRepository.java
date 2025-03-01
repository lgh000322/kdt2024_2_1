package org.example.server.repository;

import org.example.server.domain.salary_log.SalaryLog;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.salary_dto.AdminSalaryData;
import org.example.server.dto.ResponseData;
import org.example.server.dto.salary_dto.BonusDto;
import org.example.server.dto.salary_dto.UserSalaryDto;
import org.example.server.dto.user_dto.UserSalaryData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalaryRepository {

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
                return new ResponseData("일반직원 월급조회 실패", null);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw e; // 예외를 다시 던짐
        } finally {
            close(pstmt, rs); // 자원 해제
        }
    }

    //일반 유저의 월급을 모두 조회시키는 메소드
    public ResponseData searchSalaryUserOnDBByDate(Connection conn, User user, LocalDate moneySearchDate) throws SQLException {
        /*
        2024-09-07수정
         */
        String sql = "select * from salary_log where user_num = ? and received_date = ?";

        List<UserSalaryData> salaryLogs = new ArrayList<>(); // 여러 개의 SalaryLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserNum());
            pstmt.setDate(2, Date.valueOf(moneySearchDate));

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
                return new ResponseData("일반직원 월급조회 실패", null);
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

        // 샐러리 로그가 생성되지 않았을 경우 실행할 로직
        String sql = "select u.user_num, u.name, d.dept_name, p.position_name, p.basic_salary from" +
                " user u inner join dept d on u.dept_num = d.dept_num inner join position p on p.position_num = u.position_num";


        List<AdminSalaryData> salaryLogs = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // 샐러리 로그가 생성되었을 경우 해당 로직 실행.

            while (rs.next()) {

                // AdminSalaryData 객체를 생성하여 리스트에 추가
                AdminSalaryData adminSalary = new AdminSalaryData.Builder()
                        .salaryNum(rs.getLong("user_num"))
                        .name(rs.getString("name"))
                        .deptName(rs.getString("d.dept_name"))
                        .positionName(rs.getString("p.position_name"))
                        .basicSalary(rs.getInt("basic_salary"))
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

    public ResponseData searchSalaryAdminOnDBBySearchUserName(Connection conn,String searchUserName) throws SQLException {
        /*
        2024-09-07수정
         */
        // salary_log와 user 테이블을 JOIN해서 필요한 데이터를 가져옵니다.

        // 샐러리 로그가 생성되지 않았을 경우 실행할 로직
        String sql = "select u.user_num, u.name, d.dept_name, p.position_name, p.basic_salary from" +
                " user u inner join dept d on u.dept_num = d.dept_num inner join position p on p.position_num = u.position_num" +
                " where u.name like ?";


        List<AdminSalaryData> salaryLogs = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchUserName + "%");
            rs = pstmt.executeQuery();

            // 샐러리 로그가 생성되었을 경우 해당 로직 실행.

            while (rs.next()) {

                // AdminSalaryData 객체를 생성하여 리스트에 추가
                AdminSalaryData adminSalary = new AdminSalaryData.Builder()
                        .salaryNum(rs.getLong("user_num"))
                        .name(rs.getString("name"))
                        .deptName(rs.getString("d.dept_name"))
                        .positionName(rs.getString("p.position_name"))
                        .basicSalary(rs.getInt("basic_salary"))
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


    /**
     * 모든 유저 num 가져오는 함수 월급 지급 에서 사용
     */
    public List<UserSalaryDto> findUserNumAndSalary(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserSalaryDto> result = new ArrayList<>();
        String sql = "SELECT u.user_num, p.basic_salary " +
                "FROM user u inner join position p WHERE " +
                "user_Id != 'admin' and u.position_num = p.position_num";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                UserSalaryDto userSalaryDto = new UserSalaryDto();
                userSalaryDto.setUserNum(rs.getLong("user_num"));
                userSalaryDto.setUserSalary(rs.getInt("basic_salary"));
                result.add(userSalaryDto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }

        return result;
    }


    public int addSalary(Connection conn, UserSalaryDto userSalaryDto) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        int row = 0;
        String sql = "insert into salary_log (received_date, total_salary, user_num) VALUES (?, ?, ?)";

        // 현재 날짜를 가져옴
        LocalDate localDate = LocalDate.now();
        Date nowDate = Date.valueOf(localDate);

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, nowDate);
            ps.setInt(2, userSalaryDto.getUserSalary());
            ps.setLong(3, userSalaryDto.getUserNum());
            row = ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return row;
    }

    public int addBonus(BonusDto bonus, Connection con) throws SQLException {

        int row = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String checkSql = "SELECT COUNT(*) FROM salary_log WHERE user_num = ?";

        String sql = "UPDATE salary_log " +
                "SET total_salary = ? " +
                "WHERE user_num = ? " +
                "AND received_date = (SELECT MAX(received_date) FROM (SELECT received_date FROM salary_log WHERE user_num = ?) AS temp)";
        try {
            pstmt = con.prepareStatement(checkSql);
            pstmt.setLong(1, bonus.getUserNum());
            rs = pstmt.executeQuery();


            // 샐러리 로그가 존재하면 실행.
            if (rs.next() && rs.getInt(1) > 0) {

                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, bonus.getTotalSalary());
                pstmt.setLong(2, bonus.getUserNum());
                pstmt.setLong(3, bonus.getUserNum());
                row = pstmt.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return row;
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
