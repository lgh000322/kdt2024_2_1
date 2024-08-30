package org.example.server.repository;

import org.example.server.domain.salary_log.SalaryLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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

    public Optional<SalaryLog> DBSalarySearch(Connection conn, String userId) throws SQLException {
        String sql = "select * from salary_log where user_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            SalaryLog salary = null;


            return Optional.ofNullable(salary);

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
