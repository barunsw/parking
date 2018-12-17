package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

// ### 주차장 정보 응답 vo.
public class ParkingLotResVo implements Serializable {
    private String parkingLotNo;
    private String parkingLotNm;
    private String totalListCnt;
    private List<LaneInfoVo> laneInfoList;

    public String getParkingLotNo() {
        return parkingLotNo;
    }

    public void setParkingLotNo(String parkingLotNo) {
        this.parkingLotNo = parkingLotNo;
    }

    public String getParkingLotNm() {
        return parkingLotNm;
    }

    public void setParkingLotNm(String parkingLotNm) {
        this.parkingLotNm = parkingLotNm;
    }

    public String getTotalListCnt() {
        return totalListCnt;
    }

    public void setTotalListCnt(String totalListCnt) {
        this.totalListCnt = totalListCnt;
    }

    public List<LaneInfoVo> getLaneInfoList() {
        return laneInfoList;
    }

    public void setLaneInfoList(List<LaneInfoVo> laneInfoList) {
        this.laneInfoList = laneInfoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
