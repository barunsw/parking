package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.ParkingLotInfoVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DBServiceInterface extends Remote {
    public ParkingLotInfoVo getParkingLotInfo(String areaId) throws RemoteException;
}
