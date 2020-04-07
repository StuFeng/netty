package com.fsh.study.aio;

/**
 * @author fengsihan
 * @description 异步io
 * @create 2020-01-29 21:13
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
