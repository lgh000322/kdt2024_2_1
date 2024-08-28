package org.example.server.controller;

import org.example.server.dto.RequestData;

public interface Controller {
    public <T> T execute(RequestData requestData);
}
