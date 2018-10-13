package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ParkingLotUseInfoVo implements Serializable {
    private String lane_code;
    private String parkinglot_no;
    private String parking_level_code;
    private String parking_zone_code;
    private String lane_seq_num;
    private String lane_name;
    private String lane_type;
    private String manage_type;
    private String lane_status;
    private String car_status;
    private String car_no;
    private String car_indate;
    private String car_file;
    private String car_server_file;
    private String section_id;
    private String slot_id;
    private String updateDate;
    private String insertDate;

    public String getLane_code() {
        return lane_code;
    }

    public void setLane_code(String lane_code) {
        this.lane_code = lane_code;
    }

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

    public String getParking_zone_code() {
        return parking_zone_code;
    }

    public void setParking_zone_code(String parking_zone_code) {
        this.parking_zone_code = parking_zone_code;
    }

    public String getLane_seq_num() {
        return lane_seq_num;
    }

    public void setLane_seq_num(String lane_seq_num) {
        this.lane_seq_num = lane_seq_num;
    }

    public String getLane_name() {
        return lane_name;
    }

    public void setLane_name(String lane_name) {
        this.lane_name = lane_name;
    }

    public String getLane_type() {
        return lane_type;
    }

    public void setLane_type(String lane_type) {
        this.lane_type = lane_type;
    }

    public String getManage_type() {
        return manage_type;
    }

    public void setManage_type(String manage_type) {
        this.manage_type = manage_type;
    }

    public String getLane_status() {
        return lane_status;
    }

    public void setLane_status(String lane_status) {
        this.lane_status = lane_status;
    }

    public String getCar_status() {
        return car_status;
    }

    public void setCar_status(String car_status) {
        this.car_status = car_status;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getCar_indate() {
        return car_indate;
    }

    public void setCar_indate(String car_indate) {
        this.car_indate = car_indate;
    }

    public String getCar_file() {
        return car_file;
    }

    public void setCar_file(String car_file) {
        this.car_file = car_file;
    }

    public String getCar_server_file() {
        return car_server_file;
    }

    public void setCar_server_file(String car_server_file) {
        this.car_server_file = car_server_file;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
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
