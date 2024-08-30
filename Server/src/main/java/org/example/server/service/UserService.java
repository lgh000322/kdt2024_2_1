package org.example.server.service;


import org.example.server.db_utils.DBUtils;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private static UserService userService = null;
    private final UserRepository userRepository;
    private final DataSource dataSource;

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }


    public static UserService createOrGetUserService() {
        if (userService == null) {
            userService = new UserService(UserRepository.createOrGetUserRepository());
            System.out.println("싱글톤 memberService 생성됨");
            return userService;
        }

        System.out.println("싱글톤 memberService를 재사용");
        return userService;
    }

    /**
     * 아래부턴 비즈니스 로직 및 memberRepository를 호출해 쿼리문을 실행한다.
     * 예시는 다음과 같다.
     * public void methodEX() {
     * //트랜잭션 처리후 비즈니스 로직을 실행한다.
     * callBizz();
     * memberRepository.methodEX();
     * }
     * <p>
     * public void callBizz(){
     * System.out.println("비즈니스 로직");
     * }
     */


    /**
     * 회원가입
     *
     * @param user: json으로 받은 회원 데이터
     */
    public ResponseData join(User user) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = joinBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    /**
     * 로그인
     *
     * @param user 로그인 정보를 검사할 유저
     * @return
     */
    public ResponseData login(User user) throws SQLException {
        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = loginBizLogicUser(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }


    /**
     * 특정 회원을 찾는 메소드
     *
     * @param user
     * @param threadLocalUser
     * @return
     * @throws SQLException
     */
    public ResponseData findByUserId(User user, ThreadLocal<User> threadLocalUser) throws SQLException {
        //로그인을 한 적이 있으면 데이터베이스에 쿼리를 날리지 않는다.
        if (threadLocalUser != null) {
            return new ResponseData("회원 조회 성공", threadLocalUser.get());
        }

        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findByUserIdBizLogic(user.getUserId(), con, user.getRole());
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    public ResponseData findAll(String deptName) throws SQLException {
        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = userRepository.findAll(con,deptName);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    private ResponseData findByUserIdBizLogic(String userId, Connection con, Role role) throws SQLException {

        Optional<User> findUser = userRepository.findUserByIDAndRole(con, userId, role);

        if (findUser.isPresent()) {
            return new ResponseData("회원 조회 성공", findUser.get());
        }

        return new ResponseData("회원 조회 실패", null);
    }


    private ResponseData loginBizLogicUser(User user, Connection con) throws SQLException {
        Optional<User> findUser = Optional.empty();

        findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());
        if (findUser.isEmpty()) {
            return new ResponseData("실패(존재하지 않는 회원)", null);
        }

        User loginUser = findUser.get();
        if (!loginUser.getPassword().equals(user.getPassword())) {
            return new ResponseData("로그인 실패", null);
        }

        return new ResponseData("로그인 성공", user);
    }


    private ResponseData joinBizLogic(User user, Connection con) throws SQLException {
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());

        if (findUser.isPresent()) {
            return new ResponseData("실패", null);
        }

        userRepository.save(con, user);
        return new ResponseData("성공", null);
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                System.out.println("커넥션 반환중 에러 발생");
            }
        }
    }


}
