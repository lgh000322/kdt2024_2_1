package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;
import org.example.server.dto.answer_dto.AnswerInBoardDto;
import org.example.server.repository.AnswerRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AnswerService {
    private static AnswerService answerService = null;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource; //DB연결

    public static AnswerService createOrGetAnswerService() {
        if (answerService == null) {
            answerService = new AnswerService(AnswerRepository.createOrGetAnswerRepository(), UserRepository.createOrGetUserRepository());
            System.out.println("AnswerService 싱글톤 생성");
            return answerService;
        }

        System.out.println("AnswerService 싱글톤 반환");
        return answerService;
    }
    public AnswerService(AnswerRepository answerRepository,  UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }
    
    //////////////////////댓글추가
    public ResponseData addAnswer(Board board, BoardAnswer boardAnswer, User user) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = addAnswerBizLogic(board, boardAnswer, user, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    public ResponseData addAnswerBizLogic(Board board, BoardAnswer boardAnswer, User user,Connection con) throws SQLException {
        //댓글을 작성할 유저의 정보를 가져온다. 관리자이거나 일반 직원(게시물작성자) 이어야함
        Optional<User> findUser = userRepository.findUserByIDAndRole(con, user.getUserId(), user.getRole());

        if (findUser.isPresent()) {
            User DBUser = findUser.get();
            if(DBUser.getUserNum().equals(board.getUserNum()) || DBUser.getRole()== Role.ADMIN) {//해당 게시물의 작성자이거나 관리자만 가능
                //댓글을 작성하기위해 게시물정보, 입력한 댓글정보, 작성자정보를 보낸다.
                ResponseData answerResponse = answerRepository.addAnswerOnDB(con, board, boardAnswer, DBUser);

                return new ResponseData("답글 비즈니스 로직 성공", answerResponse.getData());
            }
            else{
                return new ResponseData("작성자가 아니거나 관리자가 아님",null);
            }
        }
        else
            return new ResponseData("실패",null);
    }
    
    ///////////////////////////////게시물의 댓글 조회

    public ResponseData searchAnswer(Board board) throws SQLException {
        Connection con = null;
        ResponseData responseData = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            responseData = searchAnswerBizLogic(board, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        } finally {
            release(con);
        }
        return responseData;
    }

    public ResponseData searchAnswerBizLogic(Board board, Connection con) throws SQLException {
        //가져온 board의 게시물 번호로 해당 번호에 달린 board_answer들을 가져온다. 댓글들은 모두가 볼 수 있는거니까

        List<AnswerInBoardDto> foundedAnswers = answerRepository.searchBoardAndTakeAnswerOnDB(con, board.getBoardNum());


        if (foundedAnswers.size() > 0) {
            return new ResponseData("성공", foundedAnswers);
        } else {
            return new ResponseData("실패", null);
        }
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
