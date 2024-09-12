package main.util;

import java.io.*;
import java.net.*;

public class ServerConnectUtils {
	
    private Socket socket;
    private DataOutputStream dataOutputStream = null;
	private DataInputStream dataInputStream = null;
    
	public Socket getSocket() {
		return socket;
	}

	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}

	public DataInputStream getDataInputStream() {
		return dataInputStream;
	}
	
    public void connect() {
        String serverAddress = "127.0.0.1";
        int port = 50001;
        
        try {
            socket = new Socket(serverAddress, port);
            System.out.println("서버에 연결되었습니다: " + socket.getInetAddress());

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("호스트를 찾을 수 없습니다: " + serverAddress);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    public void close() throws IOException {
    	dataOutputStream.close();
    	dataInputStream.close();
    	socket.close();
    }
}
