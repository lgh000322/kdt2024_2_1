package org.example.server.service.declare;

import org.example.server.domain.board.Board;
import org.example.server.domain.board.BoardAnswer;
import org.example.server.domain.user.User;
import org.example.server.dto.ResponseData;

import java.sql.Connection;
import java.sql.SQLException;

public interface AnswerService {
    ResponseData addAnswer(Board board, BoardAnswer boardAnswer, User user) throws SQLException;

    ResponseData addAnswerBizLogic(Board board, BoardAnswer boardAnswer, User user, Connection con) throws SQLException;

    ResponseData searchAnswer(Board board) throws SQLException;

    ResponseData searchAnswerBizLogic(Board board, Connection con) throws SQLException;
}
