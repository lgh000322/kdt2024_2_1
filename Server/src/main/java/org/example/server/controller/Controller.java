package org.example.server.controller;

import org.example.server.dto.RequestData;

import java.sql.SQLException;

public interface Controller {
    public <T> T execute(RequestData requestData) throws SQLException;
}
