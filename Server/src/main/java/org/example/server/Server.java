package org.example.server;

import com.google.gson.Gson;
import org.example.server.controller.front_controller.FrontController;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final FrontController frontController ;
    private final ExecutorService executorService = Executors.newFixedThreadPool(100);

    public Server(FrontController frontController) {
        this.frontController = frontController;
    }

    /**
     * 메인에서는 서버 인스턴스를 생성하고 start() 메소드를 실행시켜주어야 한다.
     * @throws IOException
     */

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(50001);
        System.out.println("Server 시작");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            executorService.execute(() -> handleClient(socket));
        }
    }

    /**
     * 쓰레드 풀에서 동작하는 메소드다. 이 메소드에서는 단순히 frontController로 자신이 원래 해야만 했던 일을 위임한다.
     * @param socket 클라이언트와 서버간 연결을 유지하기위한 소켓이다. 서버와 클라이언트가 연결을 종료할 때 파라미터로 받은 이 소켓을 종료해야
     *               한다.
     */
    private void handleClient(Socket socket) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Gson gson = new Gson();

            boolean flag = true;
            while(flag){
                String receivedJsonStr = dis.readUTF();
                RequestData requestData = gson.fromJson(receivedJsonStr, RequestData.class);
                ResponseData responseData = frontController.execute(requestData);
                writeAtDataOutputStream(dos, gson, responseData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeAtDataOutputStream(DataOutputStream dos, Gson gson, ResponseData responseData) throws IOException {
        String jsonStr = gson.toJson(responseData);
        dos.writeUTF(jsonStr);
        dos.flush();
    }
}