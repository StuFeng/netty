package com.fsh.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengsihan
 * @description 消息头
 * @create 2020-04-07 19:50
 **/
public class Header {
    // netty消息校验码 0xabef固定值：表示netty协议消息、主版本号、次版本号
    private int crcCode = 0xabef0101;
    // 消息长度
    private int length;
    // 会话ID
    private long sessionID;
    // 消息类型 例如：心跳消息、业务消息、握手消息等
    private byte type;
    // 消息优先级
    private byte priority;
    // 可选字段
    private Map<String, Object> attachment = new HashMap<>();

    public final int getCrcCode() {
        return crcCode;
    }

    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final long getSessionID() {
        return sessionID;
    }

    public final void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final byte getPriority() {
        return priority;
    }

    public final void setPriority(byte priority) {
        this.priority = priority;
    }

    public final Map<String, Object> getAttachment() {
        return attachment;
    }

    public final void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
