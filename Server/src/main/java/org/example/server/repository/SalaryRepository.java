package org.example.server.repository;

import org.example.server.domain.salary_log.SalaryLog;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;

import java.sql.*;
import java.util.*;

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
    
    
    //특정 유저의 월급을 모두 조회시키는 메소드
    public ResponseData DBSalarySearchAll(Connection conn, User user) throws SQLException {
        String sql = "select * from salary_log where user_num = ?";

        List<SalaryLog> salaryLogs = new ArrayList<>(); // 여러 개의 SalaryLog를 저장할 리스트
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());

            // SQL 쿼리 실행
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // SalaryLog 객체를 생성하여 리스트에 추가
                SalaryLog salary = new SalaryLog.Builder()
                        .salaryNum(rs.getLong("salary_num"))
                        .receivedData(rs.getDate("received_data").toLocalDate()) // LocalDate 변환
                        .totalSalary(rs.getInt("total_salary"))
                        .userNum(rs.getLong("user_num"))
                        .build();

                salaryLogs.add(salary); // 리스트에 SalaryLog 추가
            }

            // 리스트가 비어 있지 않다면 조회 성공 메시지와 함께 리스트 반환
            if (!salaryLogs.isEmpty()) {
                return new ResponseData("월급조회 성공", salaryLogs);
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
