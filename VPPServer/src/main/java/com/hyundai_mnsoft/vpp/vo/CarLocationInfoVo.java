package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class CarLocationInfoVo implements Serializable {
    private String gatherStartDate;
    private int gatherStartTime;
    private int lon;
    private int lat;
    private int listCount;
    private TrajectoryInfoVo trajectoryInfoVo;

    public String getGatherStartDate() {
        return gatherStartDate;
    }

    public void setGatherStartDate(String gatherStartDate) {
        this.gatherStartDate = gatherStartDate;
    }

    public int getGatherStartTime() {
        return gatherStartTime;
    }

    public void setGatherStartTime(int gatherStartTime) {
        this.gatherStartTime = gatherStartTime;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getListCount() {
        return listCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public TrajectoryInfoVo getTrajectoryInfoVo() {
        return trajectoryInfoVo;
    }

    public void setTrajectoryInfoVo(TrajectoryInfoVo trajectoryInfoVo) {
        this.trajectoryInfoVo = trajectoryInfoVo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
