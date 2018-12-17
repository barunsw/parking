package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;

// ### TCP Server에서 RMI를 통해 정보 요청 시 연동되는 서비스의 Interface.
public interface TcpRemoteService {
    // * 주차장 정보 요청.
    TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo);
}
