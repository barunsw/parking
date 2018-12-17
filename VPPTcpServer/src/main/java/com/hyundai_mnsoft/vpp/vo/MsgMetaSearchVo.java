package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 메시지 Meta 정보를 검색하는 Vo
public class MsgMetaSearchVo implements Serializable {
    private int msgId;
    private int msgType;

    public MsgMetaSearchVo(int msgId, int msgType) {
        this.msgId = msgId;
        this.msgType = msgType;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
