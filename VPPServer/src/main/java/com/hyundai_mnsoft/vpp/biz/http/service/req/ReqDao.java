package com.hyundai_mnsoft.vpp.biz.http.service.req;

import com.hyundai_mnsoft.vpp.vo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqDao {
    List<LaneInfoVo> getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
//    List<LaneInfoVo> getParkingLotUseLaneCodeList(ParkingLotUseSearchVo parkingLotUseSearchVo);
    VehicleStatusInfoVo getVehicleStatusInfo(RequestVo requestVo);
    VehicleTraceInfoVo getVehicleTraceInfo(RequestVo requestVo);
}
