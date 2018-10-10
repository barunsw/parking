package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class RequestVo implements Serializable {
    private String ServiceId;
    private String Version;
    private String VIN;
    private String NadId;
    private String coordinate;
    private String lon;
    private String lat;
    private String alter;
    private String ReqCompression;
    private String ReqEncryption;
    private String ReqFormat;
    private String RespCompression;
    private String RespEncryption;
    private String RespFormat;
    private String Country;
    private String Date;

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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAlter() {
        return alter;
    }

    public void setAlter(String alter) {
        this.alter = alter;
    }

    public String getReqCompression() {
        return ReqCompression;
    }

    public void setReqCompression(String reqCompression) {
        ReqCompression = reqCompression;
    }

    public String getReqEncryption() {
        return ReqEncryption;
    }

    public void setReqEncryption(String reqEncryption) {
        ReqEncryption = reqEncryption;
    }

    public String getReqFormat() {
        return ReqFormat;
    }

    public void setReqFormat(String reqFormat) {
        ReqFormat = reqFormat;
    }

    public String getRespCompression() {
        return RespCompression;
    }

    public void setRespCompression(String respCompression) {
        RespCompression = respCompression;
    }

    public String getRespEncryption() {
        return RespEncryption;
    }

    public void setRespEncryption(String respEncryption) {
        RespEncryption = respEncryption;
    }

    public String getRespFormat() {
        return RespFormat;
    }

    public void setRespFormat(String respFormat) {
        RespFormat = respFormat;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
