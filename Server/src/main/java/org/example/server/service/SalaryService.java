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
    public ResponseData SearchSalary(User user) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = SalarySearchBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }
    
    //월급내역을 추가
    public ResponseData AddSalary(User user) throws SQLException{
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = SalaryAddBizLogic(user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }

        return responseData;
    }

    //월급내역을 추가하는 비즈니스로직 메소드
    private ResponseData SalaryAddBizLogic(User user,Connection con) throws SQLException{
        //connecter와 유저ID, (관리자or직원) 을 가져와서 해당 유저의 정보를 가져옴
        Scanner scanner = new Scanner(System.in);
        String UserID = scanner.nextLine(); //임시코드 유저아이디를 입력받음

        Role userRole = Role.USER;
        Role userRoleFromString = Role.fromString("USER"); //직원으로 역할설정
        
        //관리자가 선택한 유저의 정보를 받아옴
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, UserID, userRoleFromString); //해당 유저 객체를 받아옴

        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            //findUser가 존재한다면 Repository로가서 해당 월급을 조회시킨다.
            salaryRepository.DBSalaryAdd(con, DBUser);
        } else {
            return new ResponseData("실패", null);
        }
        //userRepository.save(con, user);
        return new ResponseData("성공", findUser);
    }

    //월급조회 비즈니스로직
    private ResponseData SalarySearchBizLogic(User user, Connection con) throws SQLException {
        //connecter와 유저ID, (관리자or직원) 을 가져와서 해당 유저의 정보를 가져옴
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());
        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            //findUser가 존재한다면 Repository로가서 해당 월급을 조회시킨다.
            salaryRepository.DBSalarySearchAll(con, DBUser);

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
