package com.hyundai_mnsoft.vpp.biz.http.service.req;

import com.hyundai_mnsoft.vpp.vo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

// ### HTTP Request 시 DB와 작업하는 Dao. (Mybatis 연동)
@Repository
public interface ReqDao {
    // * VPP-101 연동 (주차장 정보 요청)
    List<LaneInfoVo> getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
    // * VPP-103 연동 (차량 상태 요청)
    VehicleStatusInfoVo getVehicleStatusInfo(RequestVo requestVo);
    // * VPP-106 연동 (차량 위치 요청)
    VehicleTraceInfoVo getVehicleTraceInfo(RequestVo requestVo);
}
