package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class CarStatusInfoVo implements Serializable {
    private String drivingStatus;
    private String doorOpen;
    private String doorLock;
    private String engineStatus;

    public String getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(String drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public String getDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(String doorOpen) {
        this.doorOpen = doorOpen;
    }

    public String getDoorLock() {
        return doorLock;
    }

    public void setDoorLock(String doorLock) {
        this.doorLock = doorLock;
    }

    public String getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(String engineStatus) {
        this.engineStatus = engineStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
