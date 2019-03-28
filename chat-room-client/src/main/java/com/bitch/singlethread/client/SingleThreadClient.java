package com.bitch.singlethread.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadClient {
    public static void main(String[] args) {
        //String serverName = "127.0.0.1";
       // Integer port = 6666;
        try {
            int port = 6666;
            if(args.length>0){
                try {
                    port = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("参数不正确");
                }
            }
            String serverName = "127.0.0.1";
            if(args.length>1){
                serverName = args[1];
            }

            //准备Socket对象,连接服务器
            Socket client = new Socket(serverName,port);
            System.out.println("连接上服务器,服务器地址为："+client.getInetAddress());
            //发送数据
            OutputStream outputToServer = client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputToServer);
            writer.write("这是客户端...\n");
            writer.flush();


            //接收数据
            InputStream inputStream = client.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String dataFromServer=scanner.next();
            //scanner.useDelimiter("\n");
            System.out.println("这是来自服务器端的消息:"+dataFromServer);


            //关闭流，关闭客户端；
            outputToServer.close();
            inputStream.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
