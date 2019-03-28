package com.bitch.mutilthread.client;
//从服务器接收数据

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class receiveDataFromServerThread extends Thread{
    private final Socket client;

    public receiveDataFromServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream clientReceiveData = client.getInputStream();
            Scanner scanner = new Scanner(clientReceiveData);
            while(scanner.hasNext()){
                String message = scanner.nextLine();
                System.out.println("来自服务器的消息"+message);
                if(message.equals("bye")){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
