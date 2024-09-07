package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import java.sql.SQLException;

public interface Controller {
    public ResponseData execute(RequestData requestData) throws SQLException;
}
