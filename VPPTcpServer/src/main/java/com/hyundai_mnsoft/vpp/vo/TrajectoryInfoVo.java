package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class TrajectoryInfoVo implements Serializable {
    private int dpLon;
    private int dpLat;
    private int speed;
    private String dpLonSign;
    private String dpLatSign;

    public int getDpLon() {
        return dpLon;
    }

    public void setDpLon(int dpLon) {
        this.dpLon = dpLon;
    }

    public int getDpLat() {
        return dpLat;
    }

    public void setDpLat(int dpLat) {
        this.dpLat = dpLat;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDpLonSign() {
        return dpLonSign;
    }

    public void setDpLonSign(String dpLonSign) {
        this.dpLonSign = dpLonSign;
    }

    public String getDpLatSign() {
        return dpLatSign;
    }

    public void setDpLatSign(String dpLatSign) {
        this.dpLatSign = dpLatSign;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
