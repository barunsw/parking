package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DBServiceInterface extends Remote {
    public TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo) throws RemoteException;
}
