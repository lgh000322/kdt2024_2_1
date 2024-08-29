package org.example.server.service;

import org.example.server.repository.BoardRepository;

public class BoardService {
    private static BoardService boardService = null;
    private final BoardRepository boardRepository;

    public static BoardService createOrGetBoardService() {
        if (boardService == null) {
            boardService = new BoardService(BoardRepository.createOrGetBoardRepository());
            System.out.println("boardService 싱글톤 생성");
            return boardService;
        }

        System.out.println("boardService 싱글톤 반환");
        return boardService;
    }
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
}
