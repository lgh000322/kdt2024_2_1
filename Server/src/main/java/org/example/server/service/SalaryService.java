package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.salary_log.SalaryLog;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.repository.SalaryRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class SalaryService {
    private static SalaryService salaryService = null;
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource; //DB연결

    public static SalaryService createOrGetSalaryService() {
        if (salaryService == null) {
            salaryService = new SalaryService(SalaryRepository.createOrGetSalaryRepository(), UserRepository.createOrGetUserRepository());
            System.out.println("AnswerService 싱글톤 생성");
            return salaryService;
        }

        System.out.println("AnswerService 싱글톤 반환");
        return salaryService;
    }

    public SalaryService(SalaryRepository salaryRepository, UserRepository userRepository) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    //월급내역 조회
    public ResponseData searchSalary(User user) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = searchSalaryBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }
    
    //월급내역을 추가
    public ResponseData addSalary(User user) throws SQLException{
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = addSalaryBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    //월급내역을 추가하는 비즈니스로직 메소드
    private ResponseData addSalaryBizLogic(User user,Connection con) throws SQLException{
        // 사용자의 직급(Role)에 따른 기본급 설정 및 성과급 추가
        int baseSalary=1000000;
        //여기서 기본급값을 설정해줘야할거같음.

        //여기서 성과급을 추가해도됨.
        int bonusSalary = 0;

        // 총 월급 계산
        int totalSalary = baseSalary+bonusSalary;

        // SalaryLog 객체 생성
        SalaryLog salaryLog = new SalaryLog.Builder()
                .receivedData(LocalDate.now()) // 현재 날짜를 급여 지급일로 설정
                .totalSalary(totalSalary) // 총 월급 설정
                .userNum(user.getUserNum()) // 유저 번호 설정
                .build();

        // 월급 정보를 데이터베이스에 삽입
        return salaryRepository.insertSalaryonDB(con, user, salaryLog);
    }

    //월급조회 비즈니스로직
    private ResponseData searchSalaryBizLogic(User user, Connection con) throws SQLException {
        //connecter와 유저ID, (관리자or직원) 을 가져와서 해당 유저의 정보를 가져옴
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());
        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            //findUser가 존재한다면 Repository로가서 해당 월급을 조회시킨다.
            salaryRepository.searchSalaryAllOnDB(con, DBUser);

        } else {
            return new ResponseData("실패", null);
        }
        //userRepository.save(con, user);
        return new ResponseData("성공", findUser); //리스트를 반환함
    }

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
