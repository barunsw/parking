package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 주차장 정보 요청 vo.
public class ParkingLotReqVo implements Serializable {
    private String parkingAreaID;
    private String reqTime;

    public String getParkingAreaID() {
        return parkingAreaID;
    }

    public void setParkingAreaID(String parkingAreaID) {
        this.parkingAreaID = parkingAreaID;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
