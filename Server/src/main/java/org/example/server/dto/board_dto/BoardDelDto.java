package org.example.server.dto.board_dto;

import org.example.server.domain.board.Board;
import org.example.server.domain.user.User;

public class BoardDelDto {
    private Board board; //게시물정보
    private User user; //유저정보

    private BoardDelDto(BoardDelDto.Builder builder) {
        this.board = builder.board; // 빌더의 Board 필드로 초기화
        this.user = builder.user;
    }

    public User getUser() {
        return user;
    }

    public Board getBoard() {
        return board;
    }

    public static class Builder {
        private Board board; // Board 타입으로 수정
        private User user;

        public BoardDelDto.Builder user(User user) {
            this.user = user;
            return this;
        }

        public BoardDelDto.Builder board(Board board) {
            this.board = board;
            return this;
        }

        public BoardDelDto build() {
            return new BoardDelDto(this);
        }
    }
}
