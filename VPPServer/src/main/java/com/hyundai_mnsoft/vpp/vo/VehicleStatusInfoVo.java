package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// ### 차량 상태 Vo.
public class VehicleStatusInfoVo implements Serializable {
    private String ServiceId;
    private String Version;
    private String VIN;
    private String NadId;
    private String MoId;
    private String drivingStatus;
    private String doorOpen;
    private String engineStatus;
    private String transmission;
    private String velocity;
    private String steering;
    private String control;
    private byte[] routeData;
    private String useType;
    private String inOut;
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

    public String getMoId() {
        return MoId;
    }

    public void setMoId(String moId) {
        MoId = moId;
    }

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

    public String getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(String engineStatus) {
        this.engineStatus = engineStatus;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }

    public String getSteering() {
        return steering;
    }

    public void setSteering(String steering) {
        this.steering = steering;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public byte[] getRouteData() {
        return routeData;
    }

    public void setRouteData(byte[] routeData) {
        this.routeData = routeData;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
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
