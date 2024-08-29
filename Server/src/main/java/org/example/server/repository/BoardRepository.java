package org.example.server.repository;

public class BoardRepository {
    private static BoardRepository boardRepository = null;

    public static BoardRepository createOrGetBoardRepository() {
        if (boardRepository == null) {
            boardRepository = new BoardRepository();
            System.out.println("boardRepository 싱글톤 생성");
            return boardRepository;
        }

        System.out.println("boardRepository 싱글톤 반환");
        return boardRepository;
    }
}
