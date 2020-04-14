package com.fsh.client;

import com.fsh.coder.NettyMessageDecoder;
import com.fsh.coder.NettyMessageEncoder;
import com.fsh.handle.HeartBeatReqHandle;
import com.fsh.handle.LoginAuthRespHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author fengsihan
 * @description 服务端
 * @create 2020-04-13 21:22
 **/
public class NettyServer {

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 用于启动nio服务端的辅助启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                // 对应jdk nio服务端的ServerSocketChannel
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                        ch.pipeline().addLast(new NettyMessageEncoder());
                        ch.pipeline().addLast(new LoginAuthRespHandle());
                        ch.pipeline().addLast(new HeartBeatReqHandle());
                    }
                });

        // 绑定端口，同步等待成功
        serverBootstrap.bind("127.0.0.1", 8081).sync();

        System.out.println("Netty server start ok : 127.0.0.1:" + 8081);
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}
