package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 메시지 Meta 정보 Vo.
public class MsgMetaVo implements Serializable {
    private int msgId;
    private String fieldName;
    private int seq;
    private String colType;
    private int colLength;
    private String colReqType;
    private String colWorkType;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public int getColLength() {
        return colLength;
    }

    public void setColLength(int colLength) {
        this.colLength = colLength;
    }

    public String getColReqType() {
        return colReqType;
    }

    public void setColReqType(String colReqType) {
        this.colReqType = colReqType;
    }

    public String getColWorkType() {
        return colWorkType;
    }

    public void setColWorkType(String colWorkType) {
        this.colWorkType = colWorkType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
