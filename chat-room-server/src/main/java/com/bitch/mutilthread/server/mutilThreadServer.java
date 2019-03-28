package com.bitch.mutilthread.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mutilThreadServer {
    public static void main(String[] args) {
        //创建固定大小的线程池
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            //创建一个服务端socket
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("等待客户端连接...");
            //while循环可以接收多个客户端
            while(true){
                Socket client = serverSocket.accept();
                executorService.submit(new ExecuteClient(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
