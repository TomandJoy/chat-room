package com.bitch.mutilthread.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ExecuteClient implements Runnable {
    //创建Map,存储客户信息
    private static final Map<String, Socket> ONLINE_USER_MAP = new
            ConcurrentHashMap<String, Socket>();
    private final Socket client;

    public ExecuteClient(Socket client) {
        this.client = client;
    }

    public void run() {
        //1.获取客户端输入
        try {
            InputStream clientInput = this.client.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            while (true) {
                //获取一行
                String line = scanner.nextLine();
                //1.注册  userName:<name>
                if (line.startsWith("userName")) {
                    String useName = line.split("\\:")[1];
                    //调用注册的方法
                    this.register(useName, client);
                    continue;
                }
                //2.私聊  private:<name>:<message>
                if (line.startsWith("private")) {
                    String[] segment = line.split("\\:");
                    String userName = segment[1];
                    String message = segment[2];
                    this.privateChat(userName, message);
                    continue;
                }
                //3.群聊  group:<message>
                if (line.startsWith("group")) {
                    String message = line.split("\\:")[1];
                    this.groupChat(message);
                    continue;

                }
                //4.退出  bye
                if (line.equals("bye")) {
                    this.quit();
                    break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void register(String userName, Socket client) {
        System.out.println(userName + "欢迎加入聊天室" + client.getInetAddress());
        //将信息添加到Map
        ONLINE_USER_MAP.put(userName, client);
        printOnlineUser();
        //向客户端发送数据，告诉客户端注册成功！
        /*
        try {
            OutputStream outputToClient = client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputToClient);
            writer.write(userName + "注册成功！\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        sendMessage(this.client,userName+"注册成功！");
    }

    //打印当前在线人数
    private void printOnlineUser() {
        System.out.println("当前在线人数" + ONLINE_USER_MAP.size() + "用户名如下列表：");
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private void privateChat(String userName, String message) {
        //A发消息给B 这是A
        String userSender = this.getCurrentUserName();
        //根据userName在Map找到聊天对象
        //这是B
        Socket target = ONLINE_USER_MAP.get(userName);
        if (target != null) {
            /*
            OutputStream send = null;//为什么=null?

            try {
                send = target.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(send);
                writer.write(userSender + "对你说：" + message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            this.sendMessage(target,userSender+"对你说："+message);
        }
    }

    private void groupChat(String message) {
        for (Socket socket : ONLINE_USER_MAP.values()) {
            this.sendMessage(socket, this.getCurrentUserName() + "说" + message);
        }
    }

    private void quit() {
        String currentUserName = this.getCurrentUserName();
        System.out.println("用户：" + currentUserName + "下线");
        Socket socket = ONLINE_USER_MAP.get(currentUserName);
        this.sendMessage(socket,"bye");
        ONLINE_USER_MAP.remove(currentUserName);
        printOnlineUser();
    }

    private void sendMessage(Socket socket, String message) {
        try {
            OutputStream send = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(send);
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUserName() {
        String userSender = "";
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            if (this.client.equals(entry.getValue())) {
                userSender = entry.getKey();
                break;
            }
        }
        return userSender;
    }


}
