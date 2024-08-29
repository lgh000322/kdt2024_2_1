package org.example.server.dto;

public class ResponseData {
    /**
     * messageType="성공" or "실패"
     */
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
