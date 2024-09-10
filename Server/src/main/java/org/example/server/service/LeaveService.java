package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.dto.ResponseData;
import org.example.server.dto.leave_dto.ForFindLeaveDto;
import org.example.server.dto.leave_dto.ForRequestLeaveDto;
import org.example.server.dto.leave_dto.ForUpdateLeaveDto;
import org.example.server.dto.leave_dto.LeaveLogOfUserDto;
import org.example.server.dto.user_dto.UserRoleDto;
import org.example.server.repository.LeaveRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveService {

    private static LeaveService leaveService = null;
    private final LeaveRepository leaveRepository;
    private final DataSource dataSource;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static LeaveService createOrGetLeaveService() {
        if (leaveService == null) {
            leaveService = new LeaveService(LeaveRepository.createOrGetLeaveRepository());
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

        try {
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
    public ResponseData updateLeaveBizLogic(ForUpdateLeaveDto leaveLog, Connection conn) throws SQLException {

        int checkUpdate = leaveRepository.updateLeave(leaveLog, conn);

        if (checkUpdate == 0) {
            return new ResponseData("휴가 수락 실패", null);
        }

        return new ResponseData("휴가 수락 성공", null);
    }


    /**
     * 휴가 신청 로직
     */
    public ResponseData requestLeaveBizLogic(ForRequestLeaveDto forRequestLeaveDto, Connection conn) throws SQLException {


        if (forRequestLeaveDto.getRequestDate() == null) {
            return new ResponseData("휴가 신청 실패 (요청 날짜 미입력)", null);
        }

        if (forRequestLeaveDto.getStartDate() == null) {
            return new ResponseData("휴가 신청 실패 (시작 날짜 미입력)", null);
        }

        if (forRequestLeaveDto.getEndDate() == null) {
            return new ResponseData("휴가 신청 실패 (종료 날짜 미입력)", null);
        }

        Long createLeaveNum = leaveRepository.createLeave(forRequestLeaveDto, conn);
        System.out.println(createLeaveNum);


        return new ResponseData("휴가 신청 성공", null);
    }


    /**
     * 유저, 관리자의 휴가로그조회 로직.
     */
    private ResponseData findAllLeaveLogBizLogic(ForFindLeaveDto forFindLeaveDto, Connection conn) throws SQLException {

        List<LeaveLogOfUserDto> leaveLogOfUserDtos = new ArrayList<>();
        ResponseData responseData = null;

        //관리자일 경우 관리자가 볼수 있는 모든 직원의 휴가를 보여줌
        if (forFindLeaveDto.getUserRoleDto() == UserRoleDto.ADMIN) {
            // 관리자 휴가로그dto만들어서 리스폰스데이터에 넣어줄 예정.

        } else if (forFindLeaveDto.getUserRoleDto() == UserRoleDto.USER) {
            // 일반 유저일경우 유저 아이디를 통해서 해당 유저의 휴가 로그를 찾음.
            leaveLogOfUserDtos = leaveRepository.getLeaveLogsOfUser(forFindLeaveDto.getUserId(), conn);
            responseData = new ResponseData("일반 유저 휴가로그 조회 성공", leaveLogOfUserDtos);
        }


        return responseData;
    }


    /**
     * 커넥션 반환메서드
     */
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
