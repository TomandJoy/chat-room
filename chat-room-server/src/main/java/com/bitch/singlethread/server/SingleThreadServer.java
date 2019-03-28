package com.bitch.singlethread.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadServer {
    public static void main(String[] args) {

        try {
            //0.通过命令行获取服务器端口
            int port = 6666;
            if(args.length>0){
                try {
                    port = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("参数不正确");
                 }
            }

            //1.创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(port);
            //2.等待客户端连接
            System.out.println("等待客户端连接....");
            Socket clientSocket = serverSocket.accept();
            //3.接收数据，发送数据
            //3.1接收数据
            InputStream inputFromClient = clientSocket.getInputStream();
            Scanner scanner = new Scanner(inputFromClient);
            String dataFromClient = scanner.next();
           // scanner.useDelimiter("\n");
            System.out.println("来自客户端的消息:"+dataFromClient);
            //3.2发送数据
            OutputStream outputToClient = clientSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputToClient);
            writer.write("这是服务器...\n");
            writer.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
