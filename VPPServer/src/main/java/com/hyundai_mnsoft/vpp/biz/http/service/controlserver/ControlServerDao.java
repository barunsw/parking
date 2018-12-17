package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.ParkingLotBDLVInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.springframework.stereotype.Repository;

// ### 관제서버와 연동하여 가져온 정보를 DB에 저장. (Mybatis 연동)
@Repository
public interface ControlServerDao {
    void insertParkingLotUseInfo(ParkingLotUseInfoVo vo);
    void insertParkingLotBDLVInfo(ParkingLotBDLVInfoVo vo);
}
