package com.fsh.client;

import com.fsh.coder.NettyMessageDecoder;
import com.fsh.coder.NettyMessageEncoder;
import com.fsh.handle.HeartBeatReqHandle;
import com.fsh.handle.LoginAuthReqHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsihan
 * @description 客户端
 * @create 2020-04-13 21:03
 **/
public class NettyClient {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandle());
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandle());
                        }
                    });

            ChannelFuture future = b.connect(new InetSocketAddress(host, port), new InetSocketAddress("127.0.0.1", 8080))
                    .sync();
            future.channel().closeFuture().sync();
        } finally {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    connect(port, host);// 重连
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(8081, "127.0.0.1");
    }

}
