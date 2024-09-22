package org.example.server.repository;

import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.salary_dto.SalaryScheduleDto;
import org.example.server.dto.user_dto.UpdateUserDto;
import org.example.server.dto.user_dto.UserInfo;
import org.example.server.dto.user_dto.UserNameAndEmailDto;

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





    /**
     * 휴가일수 수정 메서드 -> leaveService 에서 사용
     * */
    public int updateRemainedLeave(String userId, int remainedLeave, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "update user set remained_leave = ? where user_id = ?";
        int row = 0;

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, remainedLeave);
            pstmt.setString(2, userId);
            row = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }

        return row; // 0 일경우 수정 실패 , 0이 아니면 수정 성공.
    }

    public int updatePosition(String email, Long positionNum, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "update user set position_num = ? where email = ?";
        int row = 0;

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, positionNum);
            pstmt.setString(2, email);
            row = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }

        return row; // 0 일경우 수정 실패 , 0이 아니면 수정 성공.
    }

    public int updatedept(String email, Long deptNum, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "update user set dept_num = ? where email = ?";
        int row = 0;

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, deptNum);
            pstmt.setString(2, email);
            row = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }

        return row; // 0 일경우 수정 실패 , 0이 아니면 수정 성공.
    }


    public int updatePositionAndDept(String email, Long positionNum, Long deptNum, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "update user set position_num = ?, dept_num = ? where email = ?";
        int row = 0;

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, positionNum);
            pstmt.setLong(2, deptNum);
            pstmt.setString(3, email);
            row = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, null);
        }

        return row; // 0 일경우 수정 실패 , 0이 아니면 수정 성공.
    }


    public Optional<User> findUserByIDAndRole(Connection conn, String userId, Role role) throws SQLException {
        String sql = "select * from user" +
                " left join dept on user.dept_num = dept.dept_num" +
                " left join position on user.position_num = position.position_num" +
                " where user_id = ? and role = ?";

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
                        .userNum(rs.getLong("user.user_num"))
                        .userId(rs.getString("user.user_id"))
                        .password(rs.getString("user.password"))
                        .name(rs.getString("user.name"))
                        .tel(rs.getString("user.tel"))
                        .email(rs.getString("user.email"))
                        .role(Role.fromString(rs.getString("user.role")))
                        .remainedLeave(rs.getInt("user.remained_leave"))
                        .positionNum(rs.getLong("user.position_num"))
                        .deptNum(rs.getLong("user.dept_num"))
                        .positionName(rs.getString("position.position_name"))
                        .deptName(rs.getString("dept.dept_name"))
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
        String sql = "select user.user_num, user.name, user.email, user.tel, dept.dept_name, position.position_name" +
                " from user" +
                " left join dept on user.dept_num = dept.dept_num" +
                " left join position on user.position_num = position.position_num" +
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
                userInfo.setTel(rs.getString("user.tel"));
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

    public List<UserInfo> findAllForAdmin(Connection conn) throws SQLException {
        String sql = "select u.user_num, u.name, u.tel, u.email, d.dept_name, p.position_name" +
                " from user u inner join dept d on u.dept_num = d.dept_num " +
                "inner join position p on u.position_num = p.position_num";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UserInfo> list = new ArrayList<>();

        try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNum(rs.getLong("user_num"));
                userInfo.setName(rs.getString("name"));
                userInfo.setTel(rs.getString("tel"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setDeptName(rs.getString("dept_name"));
                userInfo.setPositionName(rs.getString("position_name"));
                list.add(userInfo);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public List<UserInfo> findUsersByName(Connection conn, String name) throws SQLException {
        String sql = "select u.user_num, u.name, u.tel, u.email, d.dept_name, p.position_name" +
                " from user u inner join dept d on u.dept_num = d.dept_num " +
                "inner join position p on u.position_num = p.position_num where u.name like ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UserInfo> list = new ArrayList<>();

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNum(rs.getLong("user_num"));
                userInfo.setName(rs.getString("name"));
                userInfo.setTel(rs.getString("tel"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setDeptName(rs.getString("dept_name"));
                userInfo.setPositionName(rs.getString("position_name"));
                list.add(userInfo);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public List<User> findAll(Connection conn,Role role) throws SQLException {
        String sql = "select * from user where role = ?";
        List<User> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(role));
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
            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public List<SalaryScheduleDto> findAllWithPositionInfo(Connection conn) throws SQLException {
        String sql = "select user.user_num, user.remained_leave, position.basic_salary, position.leave_pay from user" +
                " left join position on user.position_num = position.position_num";

        List<SalaryScheduleDto> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                SalaryScheduleDto salaryScheduleDto = new SalaryScheduleDto();
                salaryScheduleDto.setUserNum(rs.getLong("user.user_num"));
                salaryScheduleDto.setRemainedLeave(rs.getInt("user.remained_leave"));
                salaryScheduleDto.setBasicSalary(rs.getInt("position.basic_salary"));
                salaryScheduleDto.setLeavePay(rs.getInt("position.leave_pay"));

                list.add(salaryScheduleDto);
            }
            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(pstmt, rs);
        }
    }

    public List<UserNameAndEmailDto> findUsernameAndEmailAll(Connection conn) throws SQLException {
        String sql = "select * from user";
        List<UserNameAndEmailDto> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                UserNameAndEmailDto userNameAndEmailDto = new UserNameAndEmailDto();
                userNameAndEmailDto.setUserEmail(rs.getString("email"));
                userNameAndEmailDto.setUsername(rs.getString("name"));

                list.add(userNameAndEmailDto);
            }
            return list;
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
