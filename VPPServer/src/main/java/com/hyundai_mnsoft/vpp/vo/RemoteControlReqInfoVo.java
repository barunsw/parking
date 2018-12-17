package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 원격 작업의 요청 vo.
public class RemoteControlReqInfoVo implements Serializable {
    private String remoteCtl;
    private String Reserved;
    private String reqTime;
    private String parkingLotID;

    public String getRemoteCtl() {
        return remoteCtl;
    }

    public void setRemoteCtl(String remoteCtl) {
        this.remoteCtl = remoteCtl;
    }

    public String getReserved() {
        return Reserved;
    }

    public void setReserved(String reserved) {
        Reserved = reserved;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getParkingLotID() {
        return parkingLotID;
    }

    public void setParkingLotID(String parkingLotID) {
        this.parkingLotID = parkingLotID;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
