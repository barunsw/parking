package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 차량 원격 작업의 응답 Vo.
public class TcpRemoteControlResInfoVo implements Serializable {
    private String errCode;

    private String respTime;
    private byte[] routeData;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public byte[] getRouteData() {
        return routeData;
    }

    public void setRouteData(byte[] routeData) {
        this.routeData = routeData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
