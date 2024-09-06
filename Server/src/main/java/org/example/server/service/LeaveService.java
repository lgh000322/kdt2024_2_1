package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.leave_log.LeaveLog;
import org.example.server.dto.ResponseData;
import org.example.server.repository.LeaveRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
        if(leaveService == null) {
            leaveService = new LeaveService(LeaveRepository.createOrGetLeaveRepository());
            System.out.println("LeaveService 싱글톤 생성");
            return leaveService;
        }

        System.out.println("LeaveService 싱글톤 반환.");
        return leaveService;
    }


    private ResponseData findAllLeaveLog() throws SQLException {

        Connection conn = null;
        ResponseData responseData = null;

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            responseData = findAllLeaveLogBizLogic(conn);
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

    private ResponseData findAllLeaveLogBizLogic(Connection conn) throws SQLException {

        List<LeaveLog> leaveLogs = leaveRepository.getLeaveLogs(conn);

        return new ResponseData("휴가 조회 성공", leaveLogs);
    }


    private ResponseData updateLeaveBizLogic(LeaveLog leaveLog, Connection conn) throws SQLException {

        int checkUpdate = leaveRepository.updateLeave(leaveLog, conn);

        if(checkUpdate == 0) {
            return new ResponseData("휴가 수락 실패", null);
        }

        return new ResponseData("휴가 수락 성공", null);
    }




    /**
     * 휴가 신청 로직
     * */
    private ResponseData requestLeaveBizLogic(LeaveLog leaveLog, Connection conn) throws SQLException {



        if(leaveLog.getRequestDate() == null) {
            return new ResponseData("휴가 신청 실패 (요청 날짜 미입력)", null);
        }

        if(leaveLog.getStartDate() == null) {
            return new ResponseData("휴가 신청 실패 (시작 날짜 미입력)", null);
        }

        if(leaveLog.getEndDate() == null) {
            return new ResponseData("휴가 신청 실패 (종료 날짜 미입력)", null);
        }

        Long createLeaveNum = leaveRepository.createLeave(leaveLog, conn);
        System.out.println(createLeaveNum);


        return new ResponseData("휴가 신청 성공", null);
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
