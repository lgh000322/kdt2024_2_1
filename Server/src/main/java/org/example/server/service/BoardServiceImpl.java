package org.example.server.service;

import org.example.server.domain.board.Board;
import org.example.server.domain.user.Role;
import org.example.server.domain.user.User;
import org.example.server.dto.*;
import org.example.server.dto.answer_dto.AnswerInBoardDto;
import org.example.server.dto.board_dto.*;
import org.example.server.repository.AnswerRepository;
import org.example.server.repository.BoardRepository;
import org.example.server.service.declare.BoardService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final AnswerRepository answerRepository;
    private final DataSource dataSource;


    public BoardServiceImpl(BoardRepository boardRepository, AnswerRepository answerRepository, DataSource dataSource) {
        this.boardRepository = boardRepository;
        this.answerRepository = answerRepository;
        this.dataSource = dataSource;
    }

    /**
    * 게시물 삭제.
    * */
    @Override
    public ResponseData removeBoard(BoardDelDto boardDelDto) throws SQLException {
        Connection conn = null;
        ResponseData responseData = null;

        try{

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            responseData = removeBoardBizLogic(boardDelDto, conn);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        }


        return responseData;
    }


    /**
     *  게시물 작성.
    * */
    @Override
    public ResponseData createBoard(BoardSaveDto board) throws SQLException {
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

    @Override
    public ResponseData findBoardByTitle(String boardTitle) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;
        List<BoardFindAllDto> boards;

        try{
            conn = dataSource.getConnection();
            // commit을 바로 실행하지 않기위해 setAutoCommit(false)설정
            // 쿼리 실행중 에러 발생시 롤백을 위함.
            conn.setAutoCommit(false);
            boards = boardRepository.getBoardByTitle(boardTitle, conn);
            responseData = new ResponseData("모든 게시물 조회 성공", boards);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        } finally {
            release(conn);
        }

        return responseData;
    }


    /**
     * 검색 조건( 게시글 제목)과 일치하는 게시글들을 조회
     */




    /**
    * 모든 게시물을 조회 , 검색조건과 일치하는 게시물 조회
    * */
    @Override
    public ResponseData findAllBoards(String title) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;


        try{
            conn = dataSource.getConnection();
            // commit을 바로 실행하지 않기위해 setAutoCommit(false)설정
            // 쿼리 실행중 에러 발생시 롤백을 위함.
            conn.setAutoCommit(false);

            responseData = findAllBoardsBizLogic(title, conn);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        } finally {
            release(conn);
        }

        return responseData;
    }

    /**
    * 특정 게시물 + 해당 게시글에 달린 댓글 조회
    * */
    @Override
    public ResponseData findOneBoard(Long boardNum) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            responseData = findOneBoardBizLogic(boardNum, conn);
            conn.commit();
        } catch (SQLException e) {
            // 에러 발생시 롤백
            conn.rollback();
            e.printStackTrace();
        } finally {
            release(conn);
        }
        return responseData;
    }

    /**
    * 게시글 수정
    * */
    @Override
    public ResponseData updateBoard(BoardUpdateDto board) throws SQLException {
        ResponseData responseData = null;
        Connection conn = null;

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            responseData = updateBoardBizLogic(board,conn);
            conn.commit();
        } catch (SQLException e) {
            // 에러 발생시 롤백
            conn.rollback();
        } finally {
            release(conn);
        }

        return responseData;
    }

    /**
    * 게시글 수정 비즈니스로직
    * */
    private ResponseData updateBoardBizLogic(BoardUpdateDto board, Connection conn) throws SQLException {



        int checkUpdate = boardRepository.updateBoard(board, conn);

        // 업데이트 쿼리 실패시 0 반환.
        if (checkUpdate == 0) {
            return new ResponseData("게시글 수정 실패", null);
        }

        return new ResponseData("게시글 수정 성공", null);
    }


    /**
     * 선택된 게시물 + 댓글을 보여주는 비즈니스 로직
    * */
    private ResponseData findOneBoardBizLogic(Long boardNum, Connection conn) throws SQLException {
        BoardInfoDto2 boardInfoDto2 = null;

        List<AnswerInBoardDto> answerInBoardDtos = null;

        BoardAndAnswer boardAndAnswer = null;

        ResponseData responseData;


        // 게시물 번호로 게시물 정보를 가져옴.
        boardInfoDto2 = boardRepository.getOneBoard(boardNum, conn);

        // 게시물 번호로 게시물 댓글을 가져옴.
        answerInBoardDtos = answerRepository.searchBoardAndTakeAnswerOnDB(conn, boardNum);

        //위의 가져온 게시물과 댓글로 dto 생성
        boardAndAnswer = new BoardAndAnswer(boardInfoDto2, answerInBoardDtos);

        //만들어진 게시물 + 댓글 dto를 responseData로 만듦.
        responseData = new ResponseData("특정 게시물 조회 성공", boardAndAnswer);

        return responseData;
    }


    /**
    * 게시물과 게시물에 달린 댓글 삭제를 구현한 로직.
    * */

    private ResponseData removeBoardBizLogic(BoardDelDto boardDelDto, Connection conn) throws SQLException {
        int checkRemove = 0;
        ResponseData responseData = null;

        // 1. 유저가 관리자이거나, 게시물 작성자와 동일한지 확인
        User user = boardDelDto.getUser();
        Board board = boardDelDto.getBoard();

        if (user.getRole() != Role.ADMIN && !user.getUserNum().equals(board.getUserNum())) {
            // 유저가 관리자도 아니고, 게시물 작성자도 아닌 경우
            return new ResponseData("게시글 삭제 권한 없음", null);
        }

        // 2. 댓글 삭제 로직
        int answerDeleteCount = answerRepository.deleteAnswersByBoardNum(board.getBoardNum(), conn);
        System.out.println("삭제된 댓글 개수: " + answerDeleteCount);

        // 3. 게시물 삭제 로직
        checkRemove = boardRepository.deleteBoard(board.getBoardNum(), conn);

        // 4. 삭제 결과 확인
        if (checkRemove == 0) {
            return new ResponseData("게시글 삭제 실패 (일치하는 게시글 없음)", null);
        }

        return new ResponseData("게시글 삭제 성공", null);
    }


    /**
     * 게시물 저장 비즈니스 로직 구현한 메서드
     */
    private ResponseData saveBoardBizLogic(BoardSaveDto board, Connection conn) throws SQLException {

        // 게시물의 제목이 없거나, 공백문자만 입력할경우. 실패메세지 반환.
        if (board.getTitle().trim().equals("") || board.getTitle() == null) {
            return new ResponseData("게시물 작성 실패", null);
        }

        // 게시물 저장 실행
        Long saveBoardNum = boardRepository.saveBoard(board, conn);

        if(saveBoardNum == null) {
            return new ResponseData("게시물 작성 실패", null);
        }

        System.out.println(saveBoardNum);

        return new ResponseData("게시물 작성 성공", null);
    }


    private ResponseData findAllBoardsBizLogic(String title, Connection conn) throws SQLException {
        List<BoardFindAllDto> boards;
        ResponseData responseData = null;

        if(title == null || title.trim().equals("")) {
            boards = boardRepository.getAllBoards(conn);
            responseData = new ResponseData("모든 게시물 조회 성공", boards);
        } else {
            boards = boardRepository.getBoardByTitle(title, conn);
            responseData = new ResponseData("검색조건과 일치한 게시물 조회 성공", boards);
        }

        return responseData;
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
