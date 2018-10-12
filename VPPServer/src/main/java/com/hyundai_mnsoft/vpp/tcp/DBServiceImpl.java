package com.hyundai_mnsoft.vpp.tcp;

import com.hyundai_mnsoft.vpp.biz.http.service.ReqService;
import com.hyundai_mnsoft.vpp.rmi.DBServiceInterface;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.RemoteException;

public class DBServiceImpl implements DBServiceInterface {

    @Autowired
    private ReqService reqService;

    public ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) throws RemoteException {
        return reqService.getParkingLotInfo(parkingLotReqVo);
    }
}
