package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;

public interface ReqService {
    ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
}
