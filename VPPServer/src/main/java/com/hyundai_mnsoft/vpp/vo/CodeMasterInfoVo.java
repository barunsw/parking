package com.hyundai_mnsoft.vpp.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class CodeMasterInfoVo implements Serializable {
    private String code_group;
    private String code_group_name;
    private String code;
    private String code_name;
    private String display_seq;
    private String user_value01;
    private String user_value02;

    public String getCode_group() {
        return code_group;
    }

    public void setCode_group(String code_group) {
        this.code_group = code_group;
    }

    public String getCode_group_name() {
        return code_group_name;
    }

    public void setCode_group_name(String code_group_name) {
        this.code_group_name = code_group_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getDisplay_seq() {
        return display_seq;
    }

    public void setDisplay_seq(String display_seq) {
        this.display_seq = display_seq;
    }

    public String getUser_value01() {
        return user_value01;
    }

    public void setUser_value01(String user_value01) {
        this.user_value01 = user_value01;
    }

    public String getUser_value02() {
        return user_value02;
    }

    public void setUser_value02(String user_value02) {
        this.user_value02 = user_value02;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
