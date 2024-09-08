package org.example;


import org.example.server.Server;
import org.example.server.controller.front_controller.FrontController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FrontController frontController = FrontController.createOrGetFrontController();
        Server server = new Server(frontController);
        server.start();
    }
}