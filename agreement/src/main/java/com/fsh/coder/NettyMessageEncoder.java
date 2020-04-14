package com.fsh.coder;

import com.fsh.bean.NettyMessage;
import com.fsh.coder.base.MarshallingEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author fengsihan
 * @description 消息编码器
 * @create 2020-04-07 20:01
 **/
public final class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    // 在Netty中通过应用Jboss Marshalling的编解码后既压缩了传输对象的体积大小又解决了传输过程中半包粘包的问题。
    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode message is null");
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeByte(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;

        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null){
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        }else{
            sendBuf.writeInt(0);
            // 之前写了crcCode 4bytes，除去crcCode和length 8bytes即为更新之后的字节
            sendBuf.setInt(4, sendBuf.readableBytes());
        }
    }
}
