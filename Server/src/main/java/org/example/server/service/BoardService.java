package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.dto.ResponseData;
import org.example.server.repository.AnswerRepository;
import org.example.server.repository.BoardRepository;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.sql.Connection;
import java.sql.SQLException;

public class BoardService {
    private static BoardService boardService = null;
    private final BoardRepository boardRepository;
    private final DataSource dataSource;

    // 의존성 주입.
    public static BoardService createOrGetBoardService() {
        // 보드 서비스가 없으면
        if (boardService == null) {
            // 보드 레포 주입
            boardService = new BoardService(BoardRepository.createOrGetBoardRepository(), AnswerRepository.createOrGetAnswerRepository());
            System.out.println("boardService 싱글톤 생성");
            return boardService;
        }
        // 있으면 기존 서비스 반환.
        System.out.println("boardService 싱글톤 반환");
        return boardService;
    }

    public BoardService(BoardRepository boardRepository, AnswerRepository answerRepository) {
        this.boardRepository = boardRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }


    /**
     *  게시물 작성.
    * */
    public ResponseData createBoard(Board board) throws SQLException {
        Connection conn = null;
        ResponseData responseData = null;

        try{
            conn = dataSource.getConnection();
            // 트랜잭션을위해 setAutoCommit(false) 설정 -> 자동 commit 을 하지 않으려고
            conn.setAutoCommit(false);
            responseData = saveBoardBizLogic(board, conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();

        } finally {
            release(conn);
        }

        return responseData;

    }


    /**
    * 모든 게시물을 조회
    * */
    private ResponseData showAllBoards(Board board) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;

        try{
            conn = dataSource.getConnection();
            // commit을 바로 실행하지 않기위해 setAutoCommit(false)설정
            // 쿼리 실행중 에러 발생시 롤백을 위함.
            conn.setAutoCommit(false);
            responseData = boardRepository.getAllBoards(conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        } finally {
            release(conn);
        }

        return responseData;
    }




    /**
     * 게시물 저장 비즈니스 로직 구현한 메서드
    * */
    private ResponseData saveBoardBizLogic(Board board, Connection conn) throws SQLException {

        // 게시물의 제목이 없거나, 공백문자만 입력할경우. 실패메세지 반환.
        if(board.getTitle().trim().equals("") || board.getTitle() == null) {
            return new ResponseData("게시물 작성 실패", null);
        }

        // 게시물 저장 실행
        Long saveBoardNum = boardRepository.saveBoard(board, conn);


        return new ResponseData("게시물 작성 성공", null);
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
