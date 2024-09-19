package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.leave_dto.*;
import org.example.server.repository.LeaveRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class LeaveService {

    private static LeaveService leaveService = null;
    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource;

    public LeaveService(LeaveRepository leaveRepository, UserRepository userRepository) {
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static LeaveService createOrGetLeaveService() {
        if(leaveService == null) {
            leaveService = new LeaveService(LeaveRepository.createOrGetLeaveRepository(), UserRepository.createOrGetUserRepository());
            System.out.println("LeaveService 싱글톤 생성");
            return leaveService;
        }

        System.out.println("LeaveService 싱글톤 반환.");
        return leaveService;
    }


    /**
     * 휴가 조회 메서드
     */
    public ResponseData findLeaveLog(ForFindLeaveDto forFindLeaveDto) throws SQLException {

        Connection conn = null;
        ResponseData responseData = null;

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            responseData = findAllLeaveLogBizLogic(forFindLeaveDto, conn);
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            release(conn);
        }

        return responseData;
    }


    /**
     * 휴가 수정 메서드
     */
    public ResponseData updateLeaveLog(ForUpdateLeaveDto forUpdateLeaveDto) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            responseData = updateLeaveBizLogic(forUpdateLeaveDto, conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        }
        return responseData;
    }

    /**
     * 휴가 신청 메서드
     */
    public ResponseData requestLeave(ForRequestLeaveDto forRequestLeaveDto) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            responseData = requestLeaveBizLogic(forRequestLeaveDto, conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        }
        return responseData;
    }


    /**
     * 휴가 수정 비즈니스 로직
     */


    /**
     * 수정 예정 2024 9 15
     * leave num으로 user_num을 찾고 user_num에서 userId찾아서 reamainedLeave를 찾아야한다.
    *  admin에서 넘겨오는값이 이름으로 넘겨옴. (이름 중복되는게 여러개 있을수 있다.)
     *  userId를 leaveNum으로 확인하고  확인된 id의 남은 휴가일수를 확인한다
    *
    * */
    public ResponseData updateLeaveBizLogic(ForUpdateLeaveDto leaveLog, Connection conn) throws SQLException {

        //남은 후가일수
        int remainedLeave = 0;
        Long userNum = null;

        Date startDate = Date.valueOf(leaveLog.getStartDate());
        Date endDate = Date.valueOf(leaveLog.getEndDate());

        Optional<User> userById = userRepository.findUserById(conn, leaveLog.getUserId());
        if(userById.isPresent()) {
            userNum = userById.get().getUserNum();
        }

        /**
        *  휴가  거절 클릭시 실행.
         *
         *  유저아이디로 유저넘을찾는다.  유저넘으로 유저의 휴가들을 찾는다.  유저넘과 startdate가 일치하는 휴가를 찾고 해당 휴가의 확인여부를 true로변경
        * */
        if(leaveLog.getStatus() == LeaveStatus.REJECT) {


            int row = leaveRepository.rejectLeave(userNum, startDate, endDate,conn);

            if (row == 0) {
                return new ResponseData("휴가 거절 실패", null);
            }


            List<LeaveLogOfAdminDto> leaveLogOfAdmin = leaveRepository.getLeaveLogOfAdmin(conn);


            return new ResponseData("휴가 거절 성공", leaveLogOfAdmin);
        }


        /**
        *  아래부터는 휴가 승락시 실행.
         *  유저 데이터에서 유저의 남은 휴가일수를 가져오고
         *  가져온 휴가일수 갱신 (만약 신청 휴가일수가 남은휴가일수 보다 많다면 null 반환)
        * */
        Optional<User> userInfo = userRepository.findUserById(conn, leaveLog.getUserId());

        //남은 휴가일수를 받아온다.
        if(userInfo.isPresent()) {
            remainedLeave = userInfo.get().getRemainedLeave();
        }

        // 날짜 차이 계산
        int daysBetween = calculateDaysBetween(leaveLog.getStartDate(), leaveLog.getEndDate());

        // 남은 휴가일수 갱신.
        remainedLeave = remainedLeave - daysBetween - 1;

        // 남은 휴가일수가 없거나 신청일수가 많다면, null값 반환
        if(remainedLeave <= 0) {
                return new ResponseData("휴가 수락 실패 (신청 일수가 남은 일수를 초과함.)", null);
        }

        int checkUpdate = leaveRepository.acceptLeave(userNum, startDate, endDate,conn);

        if(checkUpdate == 0) {
            return new ResponseData("휴가 수락 실패", null);
        }


        // 휴가 수정하고 유저데이터에서 남은 휴가일수 갱신
        int row = userRepository.updateRemainedLeave(leaveLog.getUserId(), remainedLeave ,conn);


        if(row == 0) {
            return new ResponseData("남은 휴가 일수 수정 실패", null);
        }


        List<LeaveLogOfAdminDto> leaveLogOfAdmin = leaveRepository.getLeaveLogOfAdmin(conn);
        return new ResponseData("휴가 수락 성공", leaveLogOfAdmin);
    }




    /**
     * 휴가 신청 로직
     * */
    public ResponseData requestLeaveBizLogic(ForRequestLeaveDto forRequestLeaveDto, Connection conn) throws SQLException {

        Long userNum = 0L;

        if(forRequestLeaveDto.getRequestDate() == null) {
            return new ResponseData("휴가 신청 실패 (요청 날짜 미입력)", null);
        }

        if(forRequestLeaveDto.getStartDate() == null) {
            return new ResponseData("휴가 신청 실패 (시작 날짜 미입력)", null);
        }

        if(forRequestLeaveDto.getEndDate() == null) {
            return new ResponseData("휴가 신청 실패 (종료 날짜 미입력)", null);
        }

        Optional<User> userInfo = userRepository.findUserById(conn, forRequestLeaveDto.getUserId());

        if(userInfo.isPresent()) {
            userNum = userInfo.get().getUserNum();
        }



        Long createLeaveNum = leaveRepository.createLeave(forRequestLeaveDto, userNum, conn);
        System.out.println(createLeaveNum);


        return new ResponseData("휴가 신청 성공", null);
    }



    /**
     *  유저, 관리자의 휴가로그조회 로직.
     * */
    private ResponseData findAllLeaveLogBizLogic(ForFindLeaveDto forFindLeaveDto, Connection conn) throws SQLException {

        List<LeaveLogOfUserDto> leaveLogOfUserDtos = new ArrayList<>();     //유저의 휴가신청리스트
        List<LeaveLogOfAdminDto> leaveLogOfAdminDtos = new ArrayList<>();   // 관리자의 모든 직원 휴가관리리스트
        List<LeaveLogOfAdminDto> matchedByUserName = new ArrayList<>();

        ResponseData responseData = null;

        //관리자일 경우 관리자가 볼수 있는 모든 직원의 휴가를 보여줌
        if(forFindLeaveDto.getUserRoleDto().getDescription().equals("관리자")) {
            //만약 유저의 이름이 널이 아니면 (유저의 이름이 있으면) -> 사용자가 이름을 같이 넘겨주면 사용자 이름과 일치하는 리스트를 가져옴
            if(forFindLeaveDto.getUserName() != null) {
                matchedByUserName = leaveRepository.getMatchedByUserName(forFindLeaveDto.getUserName(), conn);
                responseData = new ResponseData("유저 이름과 일치하는 휴가로그 조회 성공", matchedByUserName);
                return responseData;
            }

            // 모든 유저의 휴가로그 조회
            leaveLogOfAdminDtos = leaveRepository.getLeaveLogOfAdmin(conn);
            responseData = new ResponseData("모든 유저의 휴가로그 조회 성공", leaveLogOfAdminDtos);
            return responseData;

        } else if (forFindLeaveDto.getUserRoleDto().getDescription().equals("일반 유저")) {
            // 일반 유저일경우 유저 아이디를 통해서 해당 유저의 휴가 로그를 찾음.
            leaveLogOfUserDtos = leaveRepository.getLeaveLogsOfUser(forFindLeaveDto.getUserId(), conn);
            if (leaveLogOfUserDtos.isEmpty()) {
                return new ResponseData("일반 유저 휴가로그 조회 실패(휴가 로그 없음)", null);
            }
            responseData = new ResponseData("일반 유저 휴가로그 조회 성공", leaveLogOfUserDtos);
        }



        return responseData;
    }


    public static int calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        // 날짜 간의 차이를 계산
        return (int) java.time.Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
    }


    /**
     * 커넥션 반환메서드
     * */
    private void release(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
                System.out.println("커넥션 반환중 에러 발생");
            }
        }
    }

}
