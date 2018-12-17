package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

// ### RMI를 통해 TCP Server가 DB에 있는 값을 조회 하는 서비스의 Interface.
public interface DBServiceInterface extends Remote {
    // 주차장 정보 요청
    public TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo) throws RemoteException;
}
