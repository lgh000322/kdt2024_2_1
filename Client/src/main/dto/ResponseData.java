package main.dto;

public class ResponseData<T> {
    /**
     * messageType="성공" or "실패"
     */
    private String messageType;
    private T data;
    
    public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	

    public ResponseData(String messageType, T data) {
        this.messageType = messageType;
        this.data = data;
    }

   
}