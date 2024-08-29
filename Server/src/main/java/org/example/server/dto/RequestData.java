package org.example.server.dto;

import org.example.server.domain.user.User;

public class RequestData {
    private String messageType;
    private Object data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
