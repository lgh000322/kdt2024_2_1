package org.example.server.service;


import org.example.server.db_utils.DBUtils;
import org.example.server.domain.mail.MailStore;
import org.example.server.domain.mail.MailType;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.*;
import org.example.server.dto.leave_dto.LeaveDay;
import org.example.server.dto.user_dto.*;
import org.example.server.repository.DeptRepository;
import org.example.server.repository.MailRepository;
import org.example.server.repository.PositionRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public ResponseData findByUserName(String userName) throws SQLException {
        ResponseData responseData = null;
        Connection con = null;

        try{
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findByUserNameBizLogic(userName, con);
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
     * @return
     * @throws SQLException
     */

    public ResponseData findAll() throws SQLException {
        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findAllBizLogic(con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }



    public ResponseData findAllByAdmin() throws SQLException {
       ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findAllByAdminBizLogic(con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;     
    }
  
    public ResponseData findUsernameAndEmailAll() throws SQLException {

        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = findUsernameAndEmailAllBizLogic(con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }



    /**
    *  회원정보 수정 ( 관리자 페이지)
    * */
    public ResponseData updateUser(UpdateUserDto updateUserDto) throws SQLException {

        ResponseData responseData = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = updateUserBizLogic(updateUserDto, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }


    private ResponseData findByUserNameBizLogic(String userName, Connection con) throws SQLException {
        List<UserInfo> userInfos = new ArrayList<>();

        userInfos = userRepository.findUsersByName(con, userName);

        return new ResponseData("회원 이름으로 매칭된 정보 검색 성공", userInfos);
    }

    private ResponseData updateUserBizLogic(UpdateUserDto updateUserDto, Connection con) throws SQLException {

        int row = 0;

        // deptname이 null이고  positioname이 null이 아닐경우 position만 바꿔줌.
        if(updateUserDto.getDeptName() == null && updateUserDto.getPositionName() != null) {
            LeaveDay positionNumByPositionName = positionRepository.findPositionNumByPositionName(con, updateUserDto.getPositionName());
            Long positionNum = positionNumByPositionName.getPositionNum();

            row = userRepository.updatePosition(updateUserDto.getEmail(), positionNum, con);
        }
        // deptName만 null이 아니라면  deptName만 수정한다.
        else if(updateUserDto.getPositionName() == null && updateUserDto.getDeptName() != null) {
            Long deptNumByDeptName = deptRepository.findDeptNumByDeptName(con, updateUserDto.getDeptName());

            row = userRepository.updatedept(updateUserDto.getEmail(), deptNumByDeptName, con);
        }
        // 만약 둘다 다른 값을 가진다면 직위, 부서 모두 변경
        else if(updateUserDto.getPositionName() != null && updateUserDto.getDeptName() != null) {
            LeaveDay positionNumByPositionName = positionRepository.findPositionNumByPositionName(con, updateUserDto.getPositionName());
            Long deptNumByDeptName = deptRepository.findDeptNumByDeptName(con, updateUserDto.getDeptName());
            row = userRepository.updatePositionAndDept(updateUserDto.getEmail(), positionNumByPositionName.getPositionNum(), deptNumByDeptName, con);
        }

        if(row == 0){
            return new ResponseData("관리자 회원 정보 수정 실패", null);
        }

        return new ResponseData("관리자 회원 정보 수정 성공", null);
    }


    private ResponseData findAllByAdminBizLogic(Connection con) throws SQLException {
        List<UserInfo> userInfos = userRepository.findAllForAdmin(con);
        if (userInfos.isEmpty()) {
            return new ResponseData("관리자 회원 조회 실패(회원 없음)", null);
        }

        return new ResponseData("회원 전체 조회 성공(관리자 페이지)", userInfos);
    }



    private ResponseData findUsernameAndEmailAllBizLogic(Connection con) throws SQLException {
        List<UserNameAndEmailDto> usernameAndEmailAll = userRepository.findUsernameAndEmailAll(con);

        if (usernameAndEmailAll.isEmpty()) {
            return new ResponseData("회원이 존재하지 않음(실패)", null);
        }

        return new ResponseData("회원 조회 성공", usernameAndEmailAll);

    }

    private ResponseData findAllBizLogic(Connection con) throws SQLException {
        List<User> users = userRepository.findAll(con,Role.USER);
        if (users.isEmpty()) {
            return new ResponseData("회원 조회 실패(회원 없음)", null);
        }

        return new ResponseData("회원 전체 조회 성공", users);
    }

    private ResponseData findByUserIdBizLogic(String userId, Connection con, Role role) throws SQLException {

        Optional<UserInfo> findUserInfo = userRepository.findUserInfoByIDAndRole(con, userId, role);

        if (findUserInfo.isPresent()) {
            return new ResponseData("회원 조회 성공", findUserInfo.get());
        }

        return new ResponseData("회원 조회 실패", null);
    }


    private ResponseData loginBizLogicUser(UserLoginDto userLoginDto, Connection con) throws SQLException {
        Optional<User> findUserOptional = Optional.empty();

        findUserOptional = userRepository.findUserByIDAndRole(con, userLoginDto.getUserId(), userLoginDto.getRole());
        if (findUserOptional.isEmpty()) {
            return new ResponseData("실패(존재하지 않는 회원)", null);
        }

        User findUser = findUserOptional.get();

        if (!findUser.getPassword().equals(userLoginDto.getPassword())) {
            return new ResponseData("로그인 실패", null);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserNum(findUser.getUserNum());
        userInfo.setPositionName(findUser.getPositionName());
        userInfo.setDeptName(findUser.getDeptName());
        userInfo.setEmail(findUser.getEmail());
        userInfo.setTel(findUser.getTel());
        userInfo.setName(findUser.getName());

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
