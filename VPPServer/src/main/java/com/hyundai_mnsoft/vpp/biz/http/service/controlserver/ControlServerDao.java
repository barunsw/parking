package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.ParkingLotBDLVInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlServerDao {
    int getParkingLotUseInfoCount(String lane_code);
    void insertParkingLotUseInfo(ParkingLotUseInfoVo vo);
    void updateParkingLotUseInfo(ParkingLotUseInfoVo vo);

    int getParkingLotBDLVInfoCount(ParkingLotBDLVInfoVo vo);
    void insertParkingLotBDLVInfoCount(ParkingLotBDLVInfoVo vo);
    void updateParkingLotBDLVInfoCount(ParkingLotBDLVInfoVo vo);

}
