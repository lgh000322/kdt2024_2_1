package org.example.server.service;

import org.example.server.repository.BoardRepository;

public class BoardService {
    private static BoardService boardService = null;
    private final BoardRepository boardRepository;

    // 의존성 주입.
    public static BoardService createOrGetBoardService() {
        // 보드 서비스가 없으면
        if (boardService == null) {
            // 보드 레포 주입
            boardService = new BoardService(BoardRepository.createOrGetBoardRepository());
            System.out.println("boardService 싱글톤 생성");
            return boardService;
        }
        // 있으면 기존 서비스 반환.
        System.out.println("boardService 싱글톤 반환");
        return boardService;
    }

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }



}
