package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

// ### TCP Server에서 주차장 정보 요청 시의 응답 vo.
public class TcpParkingLotResVo implements Serializable {
    private String parkingLotNo;
    private int parkingLotNmLen;
    private String parkingLotNm;
    private String totalListCnt;
    private List<TcpLaneInfoVo> laneInfoList;

    public String getParkingLotNo() {
        return parkingLotNo;
    }

    public void setParkingLotNo(String parkingLotNo) {
        this.parkingLotNo = parkingLotNo;
    }

    public int getParkingLotNmLen() {
        return parkingLotNmLen;
    }

    public void setParkingLotNmLen(int parkingLotNmLen) {
        this.parkingLotNmLen = parkingLotNmLen;
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

    public List<TcpLaneInfoVo> getLaneInfoList() {
        return laneInfoList;
    }

    public void setLaneInfoList(List<TcpLaneInfoVo> laneInfoList) {
        this.laneInfoList = laneInfoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
