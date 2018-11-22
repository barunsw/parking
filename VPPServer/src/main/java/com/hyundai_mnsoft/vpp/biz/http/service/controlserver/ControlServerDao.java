package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.ParkingLotBDLVInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlServerDao {
    void insertParkingLotUseInfo(ParkingLotUseInfoVo vo);
    void insertParkingLotBDLVInfo(ParkingLotBDLVInfoVo vo);
}
