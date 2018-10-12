package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DBServiceInterface extends Remote {
    public ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) throws RemoteException;
}
