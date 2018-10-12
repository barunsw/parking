package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class VehicleTraceInfoVo implements Serializable {
    private String ServiceId;
    private String Version;
    private String VIN;
    private String NadId;
    private String gatherStartDate;
    private String gatherStartTime;
    private int lon;
    private int lat;
    private String heading;
    private String objStatic;
    private String objDynamic;
    private String updateDate;
    private String insertDate;

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getNadId() {
        return NadId;
    }

    public void setNadId(String nadId) {
        NadId = nadId;
    }

    public String getGatherStartDate() {
        return gatherStartDate;
    }

    public void setGatherStartDate(String gatherStartDate) {
        this.gatherStartDate = gatherStartDate;
    }

    public String getGatherStartTime() {
        return gatherStartTime;
    }

    public void setGatherStartTime(String gatherStartTime) {
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

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getObjStatic() {
        return objStatic;
    }

    public void setObjStatic(String objStatic) {
        this.objStatic = objStatic;
    }

    public String getObjDynamic() {
        return objDynamic;
    }

    public void setObjDynamic(String objDynamic) {
        this.objDynamic = objDynamic;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
