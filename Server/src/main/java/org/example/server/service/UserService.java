package org.example.server.service;


import org.example.server.db_utils.DBUtils;
import org.example.server.domain.mail.MailStore;
import org.example.server.domain.mail.MailType;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.*;
import org.example.server.dto.leave_dto.LeaveDay;
import org.example.server.dto.user_dto.UserIdAndRole;
import org.example.server.dto.user_dto.UserInfo;
import org.example.server.dto.user_dto.UserJoinDto;
import org.example.server.dto.user_dto.UserLoginDto;
import org.example.server.repository.DeptRepository;
import org.example.server.repository.MailRepository;
import org.example.server.repository.PositionRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private static UserService userService = null;
    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    private final DeptRepository deptRepository;
    private final PositionRepository positionRepository;
    private final DataSource dataSource;

    private UserService(UserRepository userRepository, MailRepository mailRepository, DeptRepository deptRepository, PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
        this.deptRepository = deptRepository;
        this.userRepository = userRepository;
        this.mailRepository = mailRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }


    public static UserService createOrGetUserService() {
        if (userService == null) {
            userService = new UserService(UserRepository.createOrGetUserRepository(), MailRepository.createOrGetMailRepository(), DeptRepository.createOrGetDeptRepository(), PositionRepository.createOrGetPositionRepository());
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
    public ResponseData join(UserJoinDto user) throws SQLException {
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
    public ResponseData login(UserLoginDto user) throws SQLException {
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
     * @param userIdAndRole
     * @return
     * @throws SQLException
     */
    public ResponseData findByUserId(UserIdAndRole userIdAndRole) throws SQLException {


        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findByUserIdBizLogic(userIdAndRole.getUserId(), con, userIdAndRole.getRole());
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    /**
     *
     * 아이디 중복 검사
     */
    public ResponseData idValidation(String userId) throws SQLException {


        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findByUserIdBizLogic(userId, con, Role.USER);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    /**
     * 모든 회원을 조회하는 메소드
     * @param deptName
     * @return
     * @throws SQLException
     */

    public ResponseData findAll(String deptName) throws SQLException {
        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = userRepository.findAllByDeptName(con,deptName);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    private ResponseData findByUserIdBizLogic(String userId, Connection con, Role role) throws SQLException {

        Optional<UserInfo> findUserInfo = userRepository.findUserInfoByIDAndRole(con, userId, role);

        if (findUserInfo.isPresent()) {
            return new ResponseData("회원 조회 성공", findUserInfo.get());
        }

        return new ResponseData("회원 조회 실패", null);
    }


    private ResponseData loginBizLogicUser(UserLoginDto user, Connection con) throws SQLException {
        Optional<UserInfo> findUser = Optional.empty();

        findUser = userRepository.findUserInfoByIDAndRole(con, user.getUserId(), user.getRole());
        if (findUser.isEmpty()) {
            return new ResponseData("실패(존재하지 않는 회원)", null);
        }

        if (!user.getPassword().equals(user.getPassword())) {
            return new ResponseData("로그인 실패", null);
        }

        //이름, 사용자 번호, 이메일, 부서, 직급,
        UserInfo userInfo = findUser.get();


        return new ResponseData("로그인 성공", userInfo);
    }


    private ResponseData joinBizLogic(UserJoinDto userJoinDto, Connection con) throws SQLException {
        Optional<User> findUser = userRepository.findUserById(con, userJoinDto.getUserId());

        if (findUser.isPresent()) {
            return new ResponseData("실패", null);
        }

        //회원 저장 수행


        //회원의 부서를 임시부서로 설정한다.
        Long deptNum = deptRepository.findDeptNumByDeptName(con, "임시부서");

        //회원의 직책을 사원으로 설정한다.
        LeaveDay leaveDay = positionRepository.findPositionNumByPositionName(con, "사원");

        User user = new User.Builder()
                .userId(userJoinDto.getUserId())
                .password(userJoinDto.getPassword())
                .email(userJoinDto.getEmail())
                .tel(userJoinDto.getTel())
                .name(userJoinDto.getName())
                .role(Role.USER)
                .deptNum(deptNum)
                .positionNum(leaveDay.getPositionNum())
                .remainedLeave(leaveDay.getLeaveDay())
                .build();

        Long saveUserNum = userRepository.save(con, user);

        //각 회원의 메일함을 생성해야 한다.
        MailStore mailStore = new MailStore.Builder()
                .userNum(saveUserNum)
                .mailType(MailType.RECEIVED)
                .build();

        //받은 메일함 저장
        mailRepository.mailStoreSave(con, mailStore);

        //메일함 종류 변경
        mailStore.changeMailType(MailType.SEND);

        //보내는 메일함 저장
        mailRepository.mailStoreSave(con,mailStore);
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
