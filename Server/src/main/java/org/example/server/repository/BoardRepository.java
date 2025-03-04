package org.example.server.repository;

import org.example.server.dto.board_dto.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BoardRepository {
    /**
     * 특정 게시물(게시물 번호로 매칭) 가져오는 함수
     */
    public BoardInfoDto2 getOneBoard(Long boardNum, Connection conn) throws SQLException {

        BoardInfoDto2 boardInfoDto2 = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        // 특정 게시물 pk 값과 일치하는 게시물 데이터를 가져오는 쿼리문
        String sql = "select b.title, b.contents, u.user_id, u.name, u.user_num " +
                "from board b inner join user u " +
                "on b.user_num = u.user_num " +
                "where b.board_num = ?";


        try {
            // 특정 게시물 정보를 가져오는 쿼리 실행.
            ps = conn.prepareStatement(sql);
            ps.setLong(1, boardNum);
            rs = ps.executeQuery();

            // 해당 게시물이 있으면 실행
            if(rs.next()) {
                boardInfoDto2 = new BoardInfoDto2.Builder()
                        .boardTitle(rs.getString("title"))
                        .boardContents(rs.getString("contents"))
                        .boardUserId( rs.getString("user_id"))
                        .boardUserName(rs.getString("u.name"))
                        .userNum(rs.getLong("u.user_num"))
                        .build();

            } else {
                System.out.println("게시물 조회 실패(해당 게시물 없음)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }

        return boardInfoDto2;
    }




    /**
     * 동적 쿼리를 통해서 조건과 일치하는 게시글을 가져와야한다. (수정 필요)
    * 검색 조건과 일치하는 모든 게시물을 가져오는 메서드
    * */
    public List<BoardFindAllDto> getBoardByTitle(String boardTitle, Connection conn) throws SQLException {
        List<BoardFindAllDto> boards = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select b.board_num, b.title, u.user_id, b.created_date, u.user_num " +
                "from board b " +
                "inner join user u on b.user_num = u.user_num " +
                "where b.title like ?";

        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + boardTitle + "%");
            rs = ps.executeQuery();

            while(rs.next()) {
                // 게시물 번호
                Long boardNum = rs.getLong("board_num");
                // 게시물 제목
                String title = rs.getString("title");
                // 게시물 내용
                String userId = rs.getString("user_id");
                // 게시물 작성일
                LocalDate createdDate = rs.getDate("created_date").toLocalDate();
                // 게시글 작성자
                Long userNum = rs.getLong("u.user_num");



                BoardFindAllDto board = new BoardFindAllDto.Builder()
                        .boardNum(boardNum)
                        .title(title)
                        .userId(userId)
                        .createdDate(createdDate)
                        .userNum(userNum)
                        .build();

                boards.add(board);
            }

            return boards;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }

    }


    /**
    *  모든 게시물을 가져오는함수
    * */
    public List<BoardFindAllDto> getAllBoards(Connection conn) throws SQLException {

        List<BoardFindAllDto> boards = new ArrayList<>();

        //sql 실행을 위한 객체 생성
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select b.board_num, b.title, u.user_id, b.created_date, u.user_num " +
                "from board b inner join user u on b.user_num = u.user_num";


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
                String userId = rs.getString("user_id");
                // 게시물 작성일
                LocalDate createdDate = rs.getDate("created_date").toLocalDate();
                //사용자 번호
                Long userNum = rs.getLong("u.user_num");



                BoardFindAllDto board = new BoardFindAllDto.Builder()
                        .boardNum(boardNum)
                        .title(title)
                        .userId(userId)
                        .createdDate(createdDate)
                        .userNum(userNum)
                        .build();

                boards.add(board);
            }

            return boards;

        } catch (SQLException e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }


    /**
    * 게시물을 저장하는 함수.
    * */
    public Long saveBoard(BoardSaveDto board, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        //value -> 제목, 내용, 유저번호, 작성일자 순으로 입력
        String sql = "insert into board (title, contents, user_num, created_date)" +
                " values(?, ?, ?, ?)";


        String title = board.getTitle();
        String contents = board.getContents();
        Long userNum = board.getUserNum();
        Date createdDate = Date.valueOf(board.getCreatedDate());

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
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, rs);
        }

    }


    /**
    * 게시물 삭제하는 함수
    * */
    public int deleteBoard(Long boardNum, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        // 게시물을 삭제하는 쿼리
        String deleteBoardSql = "delete from board where board_num = ?";
        int row = 0;

        try{

            //게시물을 삭제하는 쿼리
            ps = conn.prepareStatement(deleteBoardSql);
            ps.setLong(1, boardNum);
            row =  ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        } finally {
            close(ps, null);
        }

        return row;
    }


    /**
    * 게시물을 수정하는 함수
    * */
    public int updateBoard(BoardUpdateDto board, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        String sql = "update board set title = ?, contents = ? where board_num = ?";

        int row = 0;

        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContents());
            ps.setLong(3, board.getBoardNum());
            row = ps.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        close(ps, null);

        return row; // 0 일경우 수정 실패 , 0이 아니면 수정 성공.
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


