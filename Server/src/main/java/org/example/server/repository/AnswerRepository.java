package org.example.server.repository;

import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.User;
import org.example.server.dto.answer_dto.AnswerInBoardDto;
import org.example.server.dto.ResponseData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerRepository {

    public void searchAnswerDB(){
        /*
        * 아마 Board쪽에서 게시글을 클릭하면 해당 BoardNum을 받아올것임
        * 그럼 여기서는 받아온 BoardNum을 통해 Board_Answer테이블의 BoardNum과
        * 일치하는 댓글들만 가져오면 끝
        */

        System.out.println("DB를통해 댓글을 조회");
    }

    public ResponseData addAnswerOnDB(Connection con, BoardAnswer boardAnswer) throws SQLException {
        /*
         * 선택된 Board의 board_num을 기반으로 답글을 저장
         */
        // BoardAnswer 테이블에 답글을 저장하는 SQL 쿼리
        String sql = "INSERT INTO board_answer (content, board_num,  user_num, created_date) VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);

            // PreparedStatement에 값 설정
            pstmt.setString(1, boardAnswer.getContents()); // 답글 내용
            pstmt.setLong(2, boardAnswer.getBoardNum()); // 게시물 번호
            pstmt.setLong(3, boardAnswer.getUserNum()); // 답글 작성자의 user_num
            pstmt.setDate(4, Date.valueOf(boardAnswer.getCreatedDate())); // 작성일자 (LocalDate -> java.sql.Date 변환)

            // SQL 쿼리 실행
            int rowsAffected = pstmt.executeUpdate();

            // 삽입 성공 여부에 따라 적절한 메시지 반환
            if (rowsAffected > 0) {
                return new ResponseData("댓글 저장 성공", boardAnswer); // 성공 시 응답 데이터 반환
            } else {
                return new ResponseData("댓글 저장 실패", null); // 실패 시 null 반환
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            return new ResponseData("댓글 저장 중 오류 발생", null); // 예외 발생 시 오류 메시지 반환
        } finally {
            close(pstmt, null); // PreparedStatement 자원 해제
        }
    }

    // 게시물을 검색하여 해당 게시물의 댓글들을 조회하는 DB 메소드
    public List<AnswerInBoardDto> searchBoardAndTakeAnswerOnDB(Connection con, Long boardNum) throws SQLException {
        /*
        2024-09-07수정
         */
        // SQL 쿼리: userNum을 이용하여 user 테이블에서 userId와 게시글에 달린 댓글을 조회
        String sql = "SELECT u.user_id, ba.content " +
                "FROM board_answer ba " +
                "JOIN user u ON ba.user_num = u.user_num " +
                "WHERE ba.board_num = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<AnswerInBoardDto> answers = new ArrayList<>(); // 댓글 리스트 생성

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boardNum); // 게시물 번호로 조회

            rs = pstmt.executeQuery();

            // 쿼리 결과를 처리하여 AnswerInBoardDto 객체로 변환
            while (rs.next()) {
                AnswerInBoardDto answer = new AnswerInBoardDto.Builder()
                        .answerUserId(rs.getString("user_id")) // user 테이블에서 가져온 user_id
                        .answerContent(rs.getString("content")) // 댓글 내용
                        .build();
                answers.add(answer); // 리스트에 추가
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("댓글 조회 실패.");
        } finally {
            // 리소스 해제
            close(pstmt, rs);
        }
        return answers;
    }

    // 게시물 번호에 해당하는 댓글 삭제 로직 추가
    public int deleteAnswersByBoardNum(Long boardNum, Connection conn) throws SQLException {
        String sql = "DELETE FROM board_answer WHERE board_num = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardNum); // 게시물 번호를 설정

            // 댓글 삭제 실행
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("댓글 삭제 중 오류 발생");
        } finally {
            close(pstmt, null);
        }
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
