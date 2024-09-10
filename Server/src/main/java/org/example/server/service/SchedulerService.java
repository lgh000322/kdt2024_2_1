package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.repository.LeaveRepository;
import org.example.server.repository.UserRepository;
import org.example.server.repository.WorkRepository;

import javax.sql.DataSource;
import java.sql.Connection;

public class SchedulerService {
    private static SchedulerService schedulerService = null;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final LeaveRepository leaveRepository;
    private final DataSource dataSource;

    public SchedulerService(WorkRepository workRepository, UserRepository userRepository, LeaveRepository leaveRepository) {
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.leaveRepository = leaveRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static SchedulerService createOrGetSchedulerService() {
        if (schedulerService == null) {
            schedulerService = new SchedulerService(WorkRepository.createOrGetWorkRepository(), UserRepository.createOrGetUserRepository(), LeaveRepository.createOrGetLeaveRepository());
            System.out.println("싱글톤 스케줄서비스 생성됨");
            return schedulerService;
        }

        System.out.println("싱글톤 스케줄서비스 반환됨");
        return schedulerService;
    }

    /*public void morningAct() throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            morningActBizLogic(con);
            con.commit();

        } catch (Exception e) {
            con.rollback();

        } finally {
            release(con);
        }
    }*/

   /* public void eveningAct() throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            eveningActBizLogic(con);
            con.commit();

        } catch (Exception e) {
            con.rollback();

        } finally {
            release(con);
        }
    }*/

  /*  private void eveningActBizLogic(Connection con) {
        // 오늘 날짜의 모든 근태로그를 가져온다.
        var l1 = workRepository.findAll(LocalDate.now());

        // 해당 근태로그의 상태가 출근 또는 지각일 경우 근태로그를 결근으로 변경한다.
        for (Object o : l1) {

        }

    }*/

 /*   private void morningActBizLogic(Connection con) throws SQLException {

        //모든 회원을 찾는다.
        List<User> findUsers = userRepository.findAll(con);
        for (User findUser : findUsers) {
            Long userNum = findUser.getUserNum();

            //오늘 날짜와 회원의 회원번호로 휴가한 적이 있는지 확인한다. 오늘 날짜가 휴가 시작일과 휴가 종료일 사이에 있어야하며, 휴가허가상태가 true여야 한다.
            var l1=leaveRepository.findByUserNum(con, userNum, LocalDate.now());

            //휴가 일정이 있으면 휴가 상태로 데이터베이스에 저장한다.
            if (l1 != null) {
                workRepository.save(con,);
                continue;
            }

            //휴가 일정이 없으면 근태상태를 결근으로 저장한다.
            workRepository.save(con,);
        }
    }*/

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
