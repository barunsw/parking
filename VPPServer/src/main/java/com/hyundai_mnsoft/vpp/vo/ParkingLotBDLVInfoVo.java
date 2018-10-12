package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ParkingLotBDLVInfoVo implements Serializable {
    private String parkinglot_no;
    private String parking_level_code;
    private String parking_level_name;
    private String total;
    private String current;
    private String margin;
    private String ratio;
    private String parking_full;
    private String parking_jam;
    private String seq;
    private String updateDate;
    private String insertDate;

    public String getParkinglot_no() {
        return parkinglot_no;
    }

    public void setParkinglot_no(String parkinglot_no) {
        this.parkinglot_no = parkinglot_no;
    }

    public String getParking_level_code() {
        return parking_level_code;
    }

    public void setParking_level_code(String parking_level_code) {
        this.parking_level_code = parking_level_code;
    }

    public String getParking_level_name() {
        return parking_level_name;
    }

    public void setParking_level_name(String parking_level_name) {
        this.parking_level_name = parking_level_name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getParking_full() {
        return parking_full;
    }

    public void setParking_full(String parking_full) {
        this.parking_full = parking_full;
    }

    public String getParking_jam() {
        return parking_jam;
    }

    public void setParking_jam(String parking_jam) {
        this.parking_jam = parking_jam;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
