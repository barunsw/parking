package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### HTTP Server 로 요청 시 header 정보를 전달하기 위한 Vo.
public class RequestVo implements Serializable {
    private String ServiceId;
    private String Version;
    private String VIN;
    private String NadId;
    private String MoId;
    private String coordinate;
    private String lon;
    private String lat;
    private String alter;
    private String ReqCompression;
    private String ReqEncrption;
    private String ReqFormat;
    private String RespCompression;
    private String RespEncrption;
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

    public String getMoId() {
        return MoId;
    }

    public void setMoId(String moId) {
        MoId = moId;
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

    public String getReqEncrption() {
        return ReqEncrption;
    }

    public void setReqEncrption(String reqEncrption) {
        ReqEncrption = reqEncrption;
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

    public String getRespEncrption() {
        return RespEncrption;
    }

    public void setRespEncrption(String respEncrption) {
        RespEncrption = respEncrption;
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
