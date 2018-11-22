package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class LaneInfoVo implements Serializable {
    private String laneCode;
    private String parkingLotNo;
    private String parkingLevelCode;
    private String parkingZoneCode;
    private String laneSeqNum;
    private String laneName;
    private String laneType;
    private String manageType;
    private String laneStatus;
    private String sectionId;
    private String slotId;

    public String getLaneCode() {
        return laneCode;
    }

    public void setLaneCode(String laneCode) {
        this.laneCode = laneCode;
    }

    public String getParkingLotNo() {
        return parkingLotNo;
    }

    public void setParkingLotNo(String parkingLotNo) {
        this.parkingLotNo = parkingLotNo;
    }

    public String getParkingLevelCode() {
        return parkingLevelCode;
    }

    public void setParkingLevelCode(String parkingLevelCode) {
        this.parkingLevelCode = parkingLevelCode;
    }

    public String getParkingZoneCode() {
        return parkingZoneCode;
    }

    public void setParkingZoneCode(String parkingZoneCode) {
        this.parkingZoneCode = parkingZoneCode;
    }

    public String getLaneSeqNum() {
        return laneSeqNum;
    }

    public void setLaneSeqNum(String laneSeqNum) {
        this.laneSeqNum = laneSeqNum;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public String getLaneType() {
        return laneType;
    }

    public void setLaneType(String laneType) {
        this.laneType = laneType;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public String getLaneStatus() {
        return laneStatus;
    }

    public void setLaneStatus(String laneStatus) {
        this.laneStatus = laneStatus;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
