package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.LaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqDao {
    ParkingLotInfoVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
    List<LaneInfoVo> getParkingLotUseInfoList(ParkingLotReqVo parkingLotReqVo);
}
