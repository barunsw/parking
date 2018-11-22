package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpLaneInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcpRemoteDao {
    List<TcpLaneInfoVo> getParkingLotInfo(ParkingLotReqVo parkingLotReqVo);
//    List<TcpLaneInfoVo> getParkingLotUseLaneCodeList(ParkingLotUseSearchVo parkingLotUseSearchVo);
}
