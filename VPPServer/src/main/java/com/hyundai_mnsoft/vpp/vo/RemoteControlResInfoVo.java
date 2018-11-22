package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class RemoteControlResInfoVo implements Serializable {
    private String respTime;
    private String routeData;

    public RemoteControlResInfoVo(TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo) {
        this.respTime = tcpRemoteControlResInfoVo.getRespTime();
        this.routeData = tcpRemoteControlResInfoVo.getRouteData();
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public String getRouteData() {
        return routeData;
    }

    public void setRouteData(String routeData) {
        this.routeData = routeData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
