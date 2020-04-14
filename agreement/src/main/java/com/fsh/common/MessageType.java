package com.fsh.common;

/**
 * @author fengsihan
 * @description 消息类型
 * @create 2020-04-13 19:51
 **/
public class MessageType {
    // 握手请求消息
    public static final byte LOGIN_RESP = 4;
    public static final byte LOGIN_REQ = 3;
    public static final byte HEARTBEAT_RESP = 6;
    public static final byte HEARTBEAT_REQ = 5;
}
