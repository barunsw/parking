package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;

import java.util.List;

public interface ControlServerService {
    public List<ParkingLotUseInfoVo> getLaneInfoStatus();
}
