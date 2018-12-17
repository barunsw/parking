package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpLaneInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

// ### TCP Server에서 RMI를 통해 정보 요청 시 사용하는 Dao (Mybatis 연동)
@Repository
public interface TcpRemoteDao {
    // * 주차장 정보 요청 (VPP-001 연동)
    List<TcpLaneInfoVo> getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
}
