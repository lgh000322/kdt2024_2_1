package org.example.server.repository;

public class AnswerRepository {
    private static AnswerRepository answerRepository = null;

    public static AnswerRepository createOrGetAnswerRepository() {
        if (answerRepository == null) {
            answerRepository = new AnswerRepository();
            System.out.println("AnswerRepository 싱글톤 생성");
            return answerRepository;
        }

        System.out.println("AnswerRepository 싱글톤 반환");
        return answerRepository;
    }


    public void DBAnswerSearch(){
        /*
        * 아마 Board쪽에서 게시글을 클릭하면 해당 BoardNum을 받아올것임
        * 그럼 여기서는 받아온 BoardNum을 통해 Board_Answer테이블의 BoardNum과
        * 일치하는 댓글들만 가져오면 끝
        */

        System.out.println("DB를통해 댓글을 조회");
    }

    public void DBAnswerAdd() {
        /*
        * Create문으로 선택되어있는 BoardNum에 답글저장
         */
        System.out.println("DB에 답글한 댓글을 저장.");
    }
}
