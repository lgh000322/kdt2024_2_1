package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

import java.sql.SQLException;

public interface Controller {
    ResponseData execute(RequestData requestData) throws SQLException;
}
