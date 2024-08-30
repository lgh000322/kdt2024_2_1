package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

public interface Controller {
    public ResponseData execute(RequestData requestData);
}
