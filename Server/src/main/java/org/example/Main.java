package org.example;


import org.example.server.Server;
import org.example.server.container.ApplicationContext;
import org.example.server.controller.front_controller.FrontController;
import org.example.server.scheduler.Scheduler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        // 서버 시작
        Server server = new Server();
        server.start();
    }

}