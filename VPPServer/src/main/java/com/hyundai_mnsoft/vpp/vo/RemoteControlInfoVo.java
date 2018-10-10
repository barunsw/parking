package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class RemoteControlInfoVo implements Serializable {
    private String remoteCtl;
    private String reqTime;
    private String Reserved;

    public String getRemoteCtl() {
        return remoteCtl;
    }

    public void setRemoteCtl(String remoteCtl) {
        this.remoteCtl = remoteCtl;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getReserved() {
        return Reserved;
    }

    public void setReserved(String reserved) {
        Reserved = reserved;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
