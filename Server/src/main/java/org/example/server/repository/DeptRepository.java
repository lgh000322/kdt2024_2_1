package org.example.server.repository;

import org.example.server.domain.mail.Mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DeptRepository {

    private static DeptRepository deptRepository = null;

    public static DeptRepository createOrGetDeptRepository() {
        if (deptRepository == null) {
            deptRepository = new DeptRepository();
            return deptRepository;
        }

        return deptRepository;
    }

    public Long findDeptNumByDeptName(Connection con, String deptName) throws SQLException {

        String sql = "select * from dept where dept_name = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, deptName);
            rs = pstmt.executeQuery();

            Long result = null;
            if (rs.next()) {
                result = rs.getLong("dept_num");
            }
            return result;
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
