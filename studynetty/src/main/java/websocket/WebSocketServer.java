package websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author fengsihan
 * @description 时间服务器
 * @create 2020-02-01 20:09
 **/
public class WebSocketServer {
    public static void main(String[] args) {
        int port = 8080;
        try {
            new WebSocketServer().run(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run(int port) throws Exception {
        // 配置服务端的NIO线程组
        // 包含了一组NIO线程，专门用于网络事件的处理，实际就是Reactor线程组
        // 一个用于服务端接收客户端的连接
        // 一个用于进行SocketChannel网络读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 用于启动nio服务端的辅助启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 对应jdk nio服务端的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // io事件处理类, 主要任务 记录日志，对消息进行编解码
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 将请求应答消息编码或者解码为HTTP消息
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            // 将多个消息转化为单一的FullHttpRequest或者FullHttpRequest
                            // Http解码器在每个Http消息中会生成多个消息对象
                            // 多个部分的对象组合成一条完整的http消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            // 支持异步发送大的码流，但不占用过多的内存
                            // 向客户端发送HTML5文件，用于支持浏览器和服务端进行wenSocket通信
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            pipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            Channel f = serverBootstrap.bind(port).sync().channel();

            System.out.println("Web socket server started at port " + port + '.');
            System.out.println("Open your browser and navigate to http://localhost:" + port + '/');

            // 等待服务端监听接口关闭
            f.closeFuture().sync();
        } finally {
            // 退出 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
