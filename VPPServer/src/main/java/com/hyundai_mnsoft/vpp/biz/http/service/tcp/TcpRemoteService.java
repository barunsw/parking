package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;

public interface TcpRemoteService {
    TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo);
}
