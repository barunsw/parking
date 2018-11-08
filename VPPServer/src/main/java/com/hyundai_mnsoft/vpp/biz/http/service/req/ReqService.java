package com.hyundai_mnsoft.vpp.biz.http.service.req;

import com.hyundai_mnsoft.vpp.vo.*;

import java.util.List;

public interface ReqService {
    ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
    VehicleStatusInfoVo getVehicleStatusInfo(RequestVo requestVo);
    VehicleTraceInfoVo getVehicleTraceInfo(RequestVo requestVo);
    List<CodeMasterInfoVo> getCodeMasterInfo(CodeMasterReqVo codeMasterReqVo);
}
