package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ParkingLotUseSearchVo implements Serializable {
    private String plId;
    private String rlId;

    public String getPlId() {
        return plId;
    }

    public void setPlId(String plId) {
        this.plId = plId;
    }

    public String getRlId() {
        return rlId;
    }

    public void setRlId(String rlId) {
        this.rlId = rlId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
