package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.dto.*;
import org.example.server.dto.answer_dto.AnswerInBoardDto;
import org.example.server.dto.board_dto.*;
import org.example.server.repository.AnswerRepository;
import org.example.server.repository.BoardRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BoardService {
    private static BoardService boardService = null;
    private final BoardRepository boardRepository;
    private final AnswerRepository answerRepository;
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
        this.answerRepository = answerRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }






    /**
    * 게시물 삭제.
    * */
    public ResponseData removeBoard(Long boardNum) throws SQLException {
        Connection conn = null;
        ResponseData responseData = null;

        try{

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            responseData = removeBoardBizLogic(boardNum, conn);

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


    /**
     * 검색 조건( 게시글 제목)과 일치하는 게시글들을 조회
     */
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
    * 모든 게시물을 조회 , 검색조건과 일치하는 게시물 조회
    * */
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
    * 특정 게시물 + 해당 게시글에 달린 댓글 조회
    * */
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
        BoardInfoDto boardInfoDto = null;

        List<AnswerInBoardDto> answerInBoardDtos = null;

        BoardAndAnswer boardAndAnswer = null;

        ResponseData responseData;


        // 게시물 번호로 게시물 정보를 가져옴.
        boardInfoDto = boardRepository.getOneBoard(boardNum, conn);

        // 게시물 번호로 게시물 댓글을 가져옴.
        answerInBoardDtos = answerRepository.searchBoardAndTakeAnswerOnDB(conn, boardNum);

        //위의 가져온 게시물과 댓글로 dto 생성
        boardAndAnswer = new BoardAndAnswer(boardInfoDto, answerInBoardDtos);

        //만들어진 게시물 + 댓글 dto를 responseData로 만듦.
        responseData = new ResponseData("특정 게시물 조회 성공", boardAndAnswer);

        return responseData;
    }


    /**
    * 게시물과 게시물에 달린 댓글 삭제를 구현한 로직.
    * */

    private ResponseData removeBoardBizLogic(Long boardNum, Connection conn) throws SQLException {

        int checkRemove = 0;
        ResponseData responseData = null;
        // 게시물 댓글 삭제 -> 삭제로직 구현시 추가.
        // answerRepository.deleteAnswer()

        // 게시물 삭제
        checkRemove =  boardRepository.deleteBoard(boardNum, conn);


        if(checkRemove == 0) {
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
