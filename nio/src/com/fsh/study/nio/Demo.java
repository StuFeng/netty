package com.fsh.study.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: fengsihan
 * @description: nio基本方法
 * @create: 2020-01-28 23:09
 **/
public class Demo {

    public static void main(String[] args) {
        System.out.println("测试");
    }

    public void testNioChannel() throws IOException {

        // 打开channel 监听客户端的连接
        ServerSocketChannel acceptorSvr = ServerSocketChannel.open();

        // 绑定监听端口，设置连接为非阻塞模式
        acceptorSvr.socket().bind(new InetSocketAddress(InetAddress.getByName("IP"), 8080));
        acceptorSvr.configureBlocking(false);

        // 创建多路复用器，启动线程
        Selector selector = Selector.open();
        new Thread(() -> System.out.println("")).start();

        //将channel 注册到线程的多路复用器Selector上，监听accept事件
        acceptorSvr.register(selector, SelectionKey.OP_ACCEPT, null);

        // 多路复用器在线程run方法的 无限循环体内 轮训准备就绪的Key
        int num = selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            // del with I/O event
        }


        // 多路复用器 监听到新的客户端的接入，处理新的接入请求
        SocketChannel channel = acceptorSvr.accept();
        // 设置客户端为非阻塞模式
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);


    }



}
