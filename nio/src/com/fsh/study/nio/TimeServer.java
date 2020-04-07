package com.fsh.study.nio;

/**
 * @author fengsihan
 * @description 事件服务端
 * @create 2020-01-28 23:30
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;


        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
