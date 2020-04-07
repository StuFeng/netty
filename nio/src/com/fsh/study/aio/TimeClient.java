package com.fsh.study.aio;

/**
 * @author fengsihan
 * @description 异步io客户端
 * @create 2020-01-29 23:22
 **/
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();

    }
}
