package com.bitch.mutilthread.client;

import java.io.IOException;
import java.net.Socket;

public class mutilThreadClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",6666);
            //1.向服务器发送数据
            new sendDataToServerThread(socket).start();
            //2.从服务器接收数据
            new receiveDataFromServerThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
