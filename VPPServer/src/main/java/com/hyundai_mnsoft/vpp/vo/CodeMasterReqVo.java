package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class CodeMasterReqVo implements Serializable {
    private String parkingAreaID;

    private String code_group;
    private String code;

    public String getParkingAreaID() {
        return parkingAreaID;
    }

    public void setParkingAreaID(String parkingAreaID) {
        this.parkingAreaID = parkingAreaID;
    }

    public String getCode_group() {
        return code_group;
    }

    public void setCode_group(String code_group) {
        this.code_group = code_group;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
