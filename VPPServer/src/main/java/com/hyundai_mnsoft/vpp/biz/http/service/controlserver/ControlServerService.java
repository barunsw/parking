package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;

public interface ControlServerService {
    public void reloadLaneInfoStatus();
    public ParkingLotUseInfoVo getLaneInfoStatus(String lane_code);
    public void reloadParkingUse();
}
