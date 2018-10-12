package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.LaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqDao {
    ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
    List<LaneInfoVo> getParkingLotUseInfoList(ParkingLotReqVo parkingLotReqVo);
}
