package org.example.server.service.declare;

import org.example.server.dto.ResponseData;
import org.example.server.dto.board_dto.BoardDelDto;
import org.example.server.dto.board_dto.BoardSaveDto;
import org.example.server.dto.board_dto.BoardUpdateDto;

import java.sql.SQLException;

public interface BoardService {
    ResponseData removeBoard(BoardDelDto boardDelDto) throws SQLException;

    ResponseData createBoard(BoardSaveDto board) throws SQLException;

    ResponseData findBoardByTitle(String boardTitle) throws SQLException;

    ResponseData findAllBoards(String title) throws SQLException;

    ResponseData findOneBoard(Long boardNum) throws SQLException;

    ResponseData updateBoard(BoardUpdateDto board) throws SQLException;
}
