package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlServerDao {
    int getParkingLotUseInfoCount(String lane_code);
    void insertParkingLotUseInfo(ParkingLotUseInfoVo vo);
    void updateParkingLotUseInfo(ParkingLotUseInfoVo vo);
}
