package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.vo.ParkingLotUseSearchVo;
import com.hyundai_mnsoft.vpp.vo.TcpLaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcpRemoteDao {
    TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo);
    List<TcpLaneInfoVo> getParkingLotUseLaneCodeList(ParkingLotUseSearchVo parkingLotUseSearchVo);
}
