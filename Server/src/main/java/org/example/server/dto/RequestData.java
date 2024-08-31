package org.example.server.dto;

public class RequestData {
    /**
     * 클라이언트에서 받은 요청데이터
     * EX: 클라이언트가 로그인 처리를 해달라고 요청을 보낸다
     * => messageType="/member/login"
     * messageType="/qa/list"
     */
    private String messageType;
    /**
     * 클라이언트가 서버에서 처리해주기를 원하는 데이터
     * EX: 로그인을 하는데 필요한 회원데이터
     * RequestData requestData=new RequestData();
     * requestData.setMessageType("login");
     * User user=new User();
     * String id=textarea.getText();
     * user.setUserId(id);
     * user.email(email);
     * RequestData.setData(user)
     * Gson gson=new Gson();
     * String jsonStr=gson.toJson(requestData);
     * dos.writeUTF(jsonStr);
     * dos.fflush();
     * String receiveJson=dis.readUTF();
     * ResponseData responseData=gson.fromJson(receiveJson,ResponseData.class);
     * if(responseData.getMessageType.equals("성공"){
     *
     * }
     *
     * List<Check> result =(List<Check>)</Check>responseData.getData();
     * for(int i=0;i<result.size();i++){
     * ``Check check=result.get(i)
     *      textField.append(check)
     * }
     * }
     */
    private Object data; //클라이언트가 보내는 서버가 비즈니스 로직을 처리할 실제 데이터

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