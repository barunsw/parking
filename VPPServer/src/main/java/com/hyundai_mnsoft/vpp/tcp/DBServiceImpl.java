package com.hyundai_mnsoft.vpp.tcp;

import com.hyundai_mnsoft.vpp.biz.http.service.tcp.TcpRemoteService;
import com.hyundai_mnsoft.vpp.rmi.DBServiceInterface;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.RemoteException;

public class DBServiceImpl implements DBServiceInterface {

    @Autowired
    private TcpRemoteService tcpRemoteService;

    @Override
    public TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo) throws RemoteException {
        return tcpRemoteService.getParkingLotInfo(parkingLotTcpReqVo);
    }
}
