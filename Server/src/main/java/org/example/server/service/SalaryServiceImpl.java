package org.example.server.service;


import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.salary_dto.BonusDto;
import org.example.server.dto.salary_dto.UserSalaryDto;
import org.example.server.repository.SalaryRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.declare.SalaryService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource; //DB연결

    public SalaryServiceImpl(SalaryRepository salaryRepository, UserRepository userRepository,DataSource dataSource) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
        this.dataSource = dataSource;
    }

    //월급내역 조회
    @Override
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


    @Override
    public ResponseData addSalary() throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try{
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = insertSalaryBizLogic(con);
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            release(con);
        }

        return responseData;

    }

    @Override
    public ResponseData addBonus(BonusDto bonus) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;


        try{
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = addBonusBizLogic(bonus, con);
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            release(con);
        }

        return responseData;
    }



    private ResponseData insertSalaryBizLogic(Connection con) throws SQLException {
        List<UserSalaryDto> userSalaryDtos = new ArrayList<>();
        userSalaryDtos = salaryRepository.findUserNumAndSalary(con);
        int row = 0;
        for (UserSalaryDto userSalaryDto : userSalaryDtos) {
             row = salaryRepository.addSalary(con, userSalaryDto);
        }


        if (row > 0) {
            return new ResponseData("월급 내역 등록 성공", null);
        } else {
            return new ResponseData("월급 내역 등록 실패", null);
        }
    }


    private ResponseData addBonusBizLogic(BonusDto bonus, Connection con) throws SQLException {

        int row = 0;

        row = salaryRepository.addBonus(bonus, con);

        if(row == 0){
            return new ResponseData("급여 내역 없음.", null);
        }

        return new ResponseData("성과급 추가 성공", null);
    }



    /* //월급내역을 추가
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
 */
    private ResponseData searchSalaryBizLogic(User user, Connection con) throws SQLException {
        /*
        2024-09-07수정
         */
        //connecter와 유저ID, (관리자or직원) 을 가져와서 해당 유저의 정보를 가져옴
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());
        ResponseData salaryResponse = null;

        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            if (DBUser.getRole() == Role.ADMIN) { // 관리자인 경우 전체 월급 데이터를 가져옴
                if (user.getSearchUserName().equals("")) {
                    salaryResponse = salaryRepository.searchSalaryAdminOnDB(con); // 관리자의 월급 데이터 조회
                }else{
                    salaryResponse = salaryRepository.searchSalaryAdminOnDBBySearchUserName(con, user.getSearchUserName());
                }
            } else { // 일반 유저의 월급을 조회
                if (user.getMoneySearchDate() == null) {
                    salaryResponse = salaryRepository.searchSalaryUserOnDB(con, DBUser); // 일반 유저의 월급 데이터 조회
                }

                if (user.getMoneySearchDate() != null) {
                    salaryResponse=salaryRepository.searchSalaryUserOnDBByDate(con,DBUser,user.getMoneySearchDate());
                }
            }
        } else {
            return new ResponseData("User not found", null);  // 사용자가 없을 경우 반환
        }

        return salaryResponse;
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
