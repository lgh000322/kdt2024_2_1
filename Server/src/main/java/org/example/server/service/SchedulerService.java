package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.leave_log.LeaveLog;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.domain.work_log.Status;
import org.example.server.domain.work_log.WorkLog;
import org.example.server.dto.salary_dto.SalaryScheduleDto;
import org.example.server.dto.salary_dto.UserSalaryDto;
import org.example.server.repository.LeaveRepository;
import org.example.server.repository.SalaryRepository;
import org.example.server.repository.UserRepository;
import org.example.server.repository.WorkRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SchedulerService {
    private static SchedulerService schedulerService = null;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final LeaveRepository leaveRepository;
    private final SalaryRepository salaryRepository;
    private final DataSource dataSource;

    public SchedulerService(WorkRepository workRepository, UserRepository userRepository, LeaveRepository leaveRepository, SalaryRepository salaryRepository) {
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.leaveRepository = leaveRepository;
        this.salaryRepository = salaryRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static SchedulerService createOrGetSchedulerService() {
        if (schedulerService == null) {
            schedulerService = new SchedulerService(WorkRepository.createOrGetWorkRepository(), UserRepository.createOrGetUserRepository(), LeaveRepository.createOrGetLeaveRepository(), SalaryRepository.createOrGetSalaryRepository());
            System.out.println("싱글톤 스케줄서비스 생성됨");
            return schedulerService;
        }

        System.out.println("싱글톤 스케줄서비스 반환됨");
        return schedulerService;
    }

    public void morningAct() throws SQLException {
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
    }

    public void eveningAct() throws SQLException {
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
    }

    public void everyMonthAct() throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            everyTenDaysBizLogic(con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
    }

    private void everyTenDaysBizLogic(Connection con) throws SQLException {
        //모든 회원의 기본급을 가져온다.
        List<SalaryScheduleDto> findSalaryScheduleDtos = userRepository.findAllWithPositionInfo(con);


        //기본급만큼 salary_log에 튜플을 추가한다.
        for (SalaryScheduleDto salaryScheduleDto : findSalaryScheduleDtos) {
            salarySaveBizLogic(con,salaryScheduleDto);
        }
    }

    private void salarySaveBizLogic(Connection con,SalaryScheduleDto salaryScheduleDto) throws SQLException {
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() == 1) {
            int totalLeavePay = salaryScheduleDto.getRemainedLeave() * salaryScheduleDto.getLeavePay();
            salaryScheduleDto.setBasicSalary(totalLeavePay);
        }

        UserSalaryDto userSalaryDto=new UserSalaryDto();
        userSalaryDto.setUserNum(salaryScheduleDto.getUserNum());
        userSalaryDto.setUserSalary(salaryScheduleDto.getBasicSalary());

        salaryRepository.addSalary(con, userSalaryDto);
    }

    private void eveningActBizLogic(Connection con) throws SQLException {
        // 오늘 날짜의 모든 근태로그를 가져온다.
        LocalDate today = LocalDate.now();
        List<WorkLog> findWorkLogs = workRepository.findWorkLogByDate(con, today);

        // 해당 근태로그의 상태가 출근 또는 지각이고 퇴근시간이 null이면 근태로그를 결근으로 변경한다.
        for (WorkLog findWorkLog : findWorkLogs) {
            if ((findWorkLog.getStatus() == Status.ATTENDANCE || findWorkLog.getStatus() == Status.TARDINESS) && findWorkLog.getEndTime() == null) {
                workRepository.updateStatus(findWorkLog, con);
            }
        }
    }

    private void morningActBizLogic(Connection con) throws SQLException {
        LocalTime localTime = LocalTime.of(8, 0);
        //모든 회원을 찾는다.
        List<User> findUsers = userRepository.findAll(con, Role.USER);
        for (User findUser : findUsers) {
            Long userNum = findUser.getUserNum();

            //오늘 날짜와 회원의 회원번호로 휴가한 적이 있는지 확인한다. 오늘 날짜가 휴가 시작일과 휴가 종료일 사이에 있어야하며, 휴가허가상태가 true여야 한다.
            LeaveLog leaveLog =leaveRepository.findByUserNum(con, userNum, LocalDate.now());

            //휴가 일정이 있으면 휴가 상태로 데이터베이스에 저장한다.
            if (leaveLog != null) {
                workRepository.save(con, Status.LEAVE, localTime, LocalDate.now(),userNum);
                continue;
            }

            //휴가 일정이 없으면 근태상태를 결근으로 저장한다.
            workRepository.save(con, Status.ABSENCE, localTime, LocalDate.now(),userNum);
        }
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
