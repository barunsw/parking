package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 원격 작업의 응답 vo.
public class RemoteControlResInfoVo implements Serializable {
    private String respTime;
    private String routeData;

    // TcpRemoteControlResInfoVo에서 errCode를 제외한 vo를 만들기 위한 생성자.
    public RemoteControlResInfoVo(TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo) {
        this.respTime = tcpRemoteControlResInfoVo.getRespTime();

        try {
            if ( tcpRemoteControlResInfoVo.getRouteData() != null ) {
                this.routeData = tcpRemoteControlResInfoVo.getRouteData();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
