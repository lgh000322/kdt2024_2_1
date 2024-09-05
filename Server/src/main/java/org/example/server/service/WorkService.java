package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.user.User;
import org.example.server.domain.work_log.Status;
import org.example.server.domain.work_log.WorkLog;
import org.example.server.dto.ResponseData;
import org.example.server.repository.UserRepository;
import org.example.server.repository.WorkRepository;

import javax.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class WorkService {
    private static WorkService workService = null;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource; //DB연결

    public static WorkService createOrGetWorkService() {
        if (workService == null) {
            workService = new WorkService(WorkRepository.createOrGetWorkRepository(), UserRepository.createOrGetUserRepository());
            System.out.println("WorkService 싱글톤 생성");
            return workService;
        }

        System.out.println("WorkService 싱글톤 반환");
        return workService;
    }
    public WorkService(WorkRepository workService, UserRepository userRepository){
        this.workRepository = workService;
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }
    //////////////////////////////////// 비즈니스 로직 ////////////////////////////////////////
    public ResponseData workSearchBizLogic(User user, Connection conn) throws SQLException {
        //connecter와 유저ID, (관리자or직원) 을 가져와서 해당 유저의 정보를 가져옴
        Optional<User> findUser = userRepository.findUserByIDAndRole(conn, user.getUserId(), user.getRole());
        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            //findUser가 존재한다면 Repository로가서 해당 근퇴을 조회시킨다.
            ResponseData workLogResponse = workRepository.workSearchAllonDB(DBUser, conn);

            return new ResponseData("성공", workLogResponse.getData()); //리스트를 반환함
        } else {
            return new ResponseData("실패", null);
        }
    }

    //출근 비즈니스 로직
    private ResponseData workStartBizLogic(User user, Connection con) throws SQLException {
        try {
            // 현재 접속한 유저의 정보를 가져온다.
            Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());

            // 만약 유저가 있으면
            if (findUser.isPresent()) {
                User DBUser = findUser.get(); // 유저를 user 객체화 시키고
                LocalTime startTime = LocalTime.now(); // 지금 시작시간과
                LocalDate workDate = LocalDate.now(); // 오늘 날짜를 가져온다

                // 오늘 날짜에 해당 유저의 WorkLog가 있는지 검사
                Optional<WorkLog> workLogOpt = workRepository.findWorkLogByUserAndDate(DBUser.getUserNum(), workDate, con);

                if (workLogOpt.isPresent()) { //출근 기록이 있어야 값을 업데이트 시킨다.

                    // WorkLog가 존재하면 퇴근 시간과 상태를 업데이트
                    WorkLog workLog = workLogOpt.get();

                    // 시작 시간이 9시 이후면 지각, 아닐 경우 정상 출근임을 Status 객체로 넣는다.
                    Status status = startTime.isAfter(LocalTime.of(9, 0)) ? Status.TARDINESS : Status.ATTENDANCE;

                    // 새로운 WorkLog를 생성하고 데이터베이스에 업데이트
                    workLog = new WorkLog.Builder()
                            .logNum(workLog.getLogNum())
                            .startTime(startTime)
                            .endTime(workLog.getEndTime())
                            .workDate(workDate)
                            .userNum(DBUser.getUserNum())
                            .status(status)
                            .build();
                    //출근 로그를 생성하여 넣는다.
                    return workRepository.updateStartWorkLogonDB(workLog, con);

                } else {
                    // 이미 오늘 날짜에 출근 기록이 있으면 처리하지 않음
                    return new ResponseData("출근 실패: 출근 기록이 없습니다.", null);
                }
            } else {
                return new ResponseData("출근 실패: 사용자 정보를 찾을 수 없습니다.", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("출근 실패", null);
        }
    }

    //퇴근 비즈니스 로직
    private ResponseData workEndBizLogic(User user, Connection con) throws SQLException {
        try {
            // 현재 접속한 유저의 정보를 가져온다.
            Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());

            // 만약 유저가 있으면
            if (findUser.isPresent()) {
                User DBUser = findUser.get(); // 유저를 user 객체화 시키고
                LocalTime endTime = LocalTime.now(); // 현재 시간을 가져온다
                LocalDate workDate = LocalDate.now(); // 오늘 날짜를 가져온다

                // 오늘 날짜로 해당 유저의 출근 정보가 있는지 검색
                Optional<WorkLog> workLogOpt = workRepository.findWorkLogByUserAndDate(DBUser.getUserNum(), workDate, con);

                if (workLogOpt.isPresent()) {
                    // WorkLog가 존재하면 퇴근 시간과 상태를 업데이트
                    WorkLog workLog = workLogOpt.get();

                    // 퇴근 시간이 18시 이후면 정상 출근(ATTENDANCE), 그 전일 경우 지각(TARDINESS)으로 처리
                    Status status = endTime.isAfter(LocalTime.of(18, 0)) ? Status.ATTENDANCE : Status.TARDINESS;

                    // WorkLog에 퇴근 시간과 상태를 업데이트
                    workLog = new WorkLog.Builder()
                            .logNum(workLog.getLogNum())
                            .startTime(workLog.getStartTime())
                            .endTime(endTime)
                            .workDate(workDate)
                            .userNum(DBUser.getUserNum())
                            .status(status)
                            .build();

                    return workRepository.updateEndWorkLogonDB(workLog, con);
                } else {
                    // WorkLog가 없는 경우 퇴근 기록을 남길 수 없음을 알림
                    return new ResponseData("퇴근 실패: 출근 기록 없음", null);
                }
            } else {
                return new ResponseData("퇴근 실패: 유저가 없음", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseData("퇴근 실패", null);
        }
    }

//////////////////////////////////////// 일반 로직 ////////////////////////////////////////
    public ResponseData SearchWork(User user) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = workSearchBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    public ResponseData StartWork(User user) throws SQLException{
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = workStartBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    public ResponseData EndWork(User user) throws SQLException{
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = workEndBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    public ResponseData EarlyOutWork(User user) throws SQLException{
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = workEndBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
        
    }

    ///////////////////////// release //////////////////
    //얜 그냥 쭉쓰면됨
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
