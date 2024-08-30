package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.salary_log.SalaryLog;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.repository.SalaryRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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

    //
    public ResponseData SearchSalary(User user) throws SQLException {
        Connection con=null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = SalarySearchBizLogic(user ,con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        }finally {
            release(con);
        }

        return responseData;
    }

    //월급조회 비즈니스로직
    private ResponseData SalarySearchBizLogic( User user, Connection con) throws SQLException {
        Optional<User> findUser = userRepository.findById(con, user.getUserId());
        if(findUser.isPresent()){
            User nowUser = findUser.get();
            System.out.println(nowUser.getUserId()); //테스트
        }
        //Optional<Salary> findUser = salaryRepository.DBSalarySearch(salary, userId, con);

        if (findUser.isPresent()) {
            return new ResponseData("실패", null);
        }

        //userRepository.save(con, user);
        return new ResponseData("성공", null);
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
