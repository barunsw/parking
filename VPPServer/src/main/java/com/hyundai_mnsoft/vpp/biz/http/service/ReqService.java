package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.ParkingLotInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;

public interface ReqService {
    ParkingLotInfoVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
}
