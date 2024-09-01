package org.example.server.repository;

import org.example.server.domain.board.Board;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class BoardRepository {
    private static BoardRepository boardRepository = null;




    // 싱글톤으로 보드 레포생성 -> 기존에 있으면 있는 레포를 사용, 없으면 레포를 새로 만듦.
    public static BoardRepository createOrGetBoardRepository() {
        if (boardRepository == null) {
            boardRepository = new BoardRepository();
            System.out.println("boardRepository 싱글톤 생성");
            return boardRepository;
        }

        System.out.println("boardRepository 싱글톤 반환");
        return boardRepository;
    }



    /**
    *  모든 게시물을 가져오는함수
    * */
    public ResponseData getAllBoards(Connection conn) throws SQLException {

        List<Board> boards = new ArrayList<>();

        //sql 실행을 위한 객체 생성
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select * from board order by board_num desc";


        try {
            ps = conn.prepareStatement(sql);
            //read 시 - executeQuery() <--> update, delete, insert 시 .executeUpdate()사용
            rs = ps.executeQuery();


            while (rs.next()) {

                // 게시물 번호
                Long boardNum = rs.getLong("board_num");
                // 게시물 제목
                String title = rs.getString("title");
                // 게시물 내용
                String contents = rs.getString("contents");
                // 게시물 작성일
                LocalDate createdDate = rs.getDate("created_date").toLocalDate();
                // 게시자 번호
                Long userNum = rs.getLong("user_num");


                Board board = new Board.Builder()
                        .boardNum(boardNum)
                        .title(title)
                        .contents(contents)
                        .createdDate(createdDate)
                        .userNum(userNum)
                        .build();

                boards.add(board);
            }

            return new ResponseData("게시물 조회 성공", boards);

        } catch (SQLException e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }


    /*
<<<<<<< HEAD
    * 게시물을 저장하는 함수.
    * */
    public Long saveBoard(Board board, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        //value -> 제목, 내용, 유저번호, 작성일자 순으로 입력
        String sql = "insert into board values(?, ?, ?, ?)";


        String title = board.getTitle();
        String contents = board.getContents();
        Long userNum = board.getUserNum();
        java.sql.Date createdDate = java.sql.Date.valueOf(board.getCreatedDate());

        try{

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, title);
            ps.setString(2, contents);
            ps.setLong(3, userNum);
            ps.setDate(4, createdDate);

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("게시물 작성 실패.");
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            close(ps, null);
        }

    }


    /*
<<<<<<< HEAD
    * 게시물 삭제하는 함수
    * */
    public void deleteBoard(Board board, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        // 게시물을 삭제하는 쿼리
        String deleteBoardSql = "delete from board where board_num = ?";
        // 게시물의 댓글을 삭제하는 쿼리
        String deleteAnswerSql = "delete from board_answer where board_num = ?";

        try{
            //게시물 댓글을 삭제한다.
            ps = conn.prepareStatement(deleteAnswerSql);
            ps.setLong(1, board.getBoardNum());
            ps.executeUpdate();

            //게시물을 삭제하는 쿼리
            ps = conn.prepareStatement(deleteBoardSql);
            ps.setLong(1, board.getBoardNum());
            ps.executeUpdate();


        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, null);
        }

    }


    /*
    * 게시물을 수정하는 함수
    * */
    public void updateBoard(Board board, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        String sql = "update board set title = ?, contents = ? where board_num = ?";



        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContents());
            ps.setLong(3, board.getBoardNum());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        close(ps, null);

    }


    private static void close(PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


