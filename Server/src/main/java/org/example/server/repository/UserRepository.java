package org.example.server.repository;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static UserRepository userRepository = null;

    public static UserRepository createOrGetUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository();
            System.out.println("싱글톤 memberRepository 생성됨");
            return userRepository;
        }

        System.out.println("싱글톤 memberRepository를 재사용");
        return userRepository;
    }

    /**
     * 아래부턴 쿼리문을 작성한다.
     * 예시는 다음과 같다.
     * public void methodEX(){
     * System.out.println("memberRepository 실행");
     * }
     */

    public Optional<User> findUserByIDAndRole(Connection conn, String userId, Role role) throws SQLException {
        String sql = "select * from user where user_id = ? and role = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, role.name());
            rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                user = new User.Builder()
                        .userNum(rs.getLong("user_num"))
                        .userId(rs.getString("user_id"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .tel(rs.getString("tel"))
                        .email(rs.getString("email"))
                        .role(Role.fromString(rs.getString("role")))
                        .remainedLeave(rs.getInt("remained_leave"))
                        .positionNum(rs.getLong("position_num"))
                        .deptNum(rs.getLong("dept_num"))
                        .build();
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<UserInfo> findUserInfoByIDAndRole(Connection conn, String userId, Role role) throws SQLException {
        String sql = "select user.user_num, user.name, user.email, dept.dept_name, position.position_name" +
                " from user" +
                " left join dept on user.dept_num = dept.dept_num" +
                " left join position on user.position_num = dept.position_num" +
                " where user_id = ? and role = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, role.name());
            rs = pstmt.executeQuery();

            UserInfo userInfo = null;

            if (rs.next()) {
                userInfo = new UserInfo();
                userInfo.setUserNum(rs.getLong("user.user_num"));
                userInfo.setName(rs.getString("user.name"));
                userInfo.setEmail(rs.getString("user.email"));
                userInfo.setDeptName(rs.getString("dept.dept_name"));
                userInfo.setPositionName(rs.getString("position.position_name"));
            }

            return Optional.ofNullable(userInfo);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<User> findUserById(Connection conn, String userId) throws SQLException {
        String sql = "select * from user where user_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                user = new User.Builder()
                        .userNum(rs.getLong("user_num"))
                        .userId(rs.getString("user_id"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .tel(rs.getString("tel"))
                        .email(rs.getString("email"))
                        .role(Role.fromString(rs.getString("role")))
                        .remainedLeave(rs.getInt("remained_leave"))
                        .positionNum(rs.getLong("position_num"))
                        .deptNum(rs.getLong("dept_num"))
                        .build();
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Optional<User> findUserByEmail(Connection conn, String email) throws SQLException {
        String sql = "select * from user where email = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                user = new User.Builder()
                        .userNum(rs.getLong("user_num"))
                        .userId(rs.getString("user_id"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .tel(rs.getString("tel"))
                        .email(rs.getString("email"))
                        .role(Role.fromString(rs.getString("role")))
                        .remainedLeave(rs.getInt("remained_leave"))
                        .positionNum(rs.getLong("position_num"))
                        .deptNum(rs.getLong("dept_num"))
                        .build();
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public Long save(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO user (password, name, tel, email, role, remained_leave, position_num, dept_num, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // 각 파라미터를 set
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getTel());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole().toString());
            pstmt.setInt(6, user.getRemainedLeave());
            pstmt.setLong(7, user.getPositionNum());
            pstmt.setLong(8, user.getDeptNum());
            pstmt.setString(9, user.getUserId());

            pstmt.executeUpdate();  // INSERT 실행
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public ResponseData findAll(Connection conn, String deptName) throws SQLException {
        String sql = "select * from user inner join dept on user.dept_num = dept.dept_num where user.dept_name = ?";
        List<User> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deptName);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                User user = new User.Builder()
                        .userNum(rs.getLong("user_num"))
                        .userId(rs.getString("user_id"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .tel(rs.getString("tel"))
                        .email(rs.getString("email"))
                        .role(Role.fromString(rs.getString("role")))
                        .remainedLeave(rs.getInt("remained_leave"))
                        .positionNum(rs.getLong("position_num"))
                        .deptNum(rs.getLong("dept_num"))
                        .build();

                list.add(user);
            }

            return new ResponseData("조회 성공", list);

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
