package org.example.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.server.adapter.LocalDateTypeAdapter;
import org.example.server.adapter.LocalTimeTypeAdapter;
import org.example.server.container.ApplicationContext;
import org.example.server.controller.front_controller.FrontController;
import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.scheduler.Scheduler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final FrontController frontController;
    private final ExecutorService executorService;
    private final Scheduler scheduler;

    public Server() throws Exception {
        // 컨테이너 초기화
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        applicationContext.initialize();

        this.frontController = new FrontController(applicationContext);
        this.scheduler = applicationContext.getBean(Scheduler.class);
        this.executorService = Executors.newFixedThreadPool(100);
    }

    /**
     * 메인에서는 서버 인스턴스를 생성하고 start() 메소드를 실행시켜주어야 한다.
     *
     * @throws IOException
     */

    public void start() throws IOException {
        scheduler.start();
        ServerSocket serverSocket = new ServerSocket(50001);
        System.out.println("Server 시작");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            executorService.execute(() -> {
                try {
                    handleClient(socket);
                } catch (IOException e) {
                    System.out.println("클라이언트가 종료됨");
                }
            });
        }
    }

    /**
     * 쓰레드 풀에서 동작하는 메소드다. 이 메소드에서는 단순히 frontController로 자신이 원래 해야만 했던 일을 위임한다.
     *
     * @param socket 클라이언트와 서버간 연결을 유지하기위한 소켓이다. 서버와 클라이언트가 연결을 종료할 때 파라미터로 받은 이 소켓을 종료해야
     *               한다.
     */
    private void handleClient(Socket socket) throws IOException {
        DataInputStream dis = null;
        DataOutputStream dos = null;

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                    .create();

            String receivedJsonStr = dis.readUTF();
            RequestData requestData = gson.fromJson(receivedJsonStr, RequestData.class);
            ResponseData responseData = frontController.execute(requestData);
            writeAtDataOutputStream(dos, gson, responseData);

            dos.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("클라이언트와의 연결이 끊김");
            if (dos != null) dos.close();
            if (dis != null) dis.close();
            if (socket!= null) socket.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeAtDataOutputStream(DataOutputStream dos, Gson gson, ResponseData responseData) throws IOException {
        String jsonStr = gson.toJson(responseData);
        dos.writeUTF(jsonStr);
        dos.flush();
    }
}