package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.LaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReqServiceImpl implements ReqService {

    private final ReqDao reqDao;

    @Autowired
    public ReqServiceImpl(ReqDao reqDao) {
        this.reqDao = reqDao;
    }

    public ParkingLotInfoVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) {
        ParkingLotInfoVo parkingLotInfoVo = reqDao.getParkingLotInfo(parkingLotReqVo);
        List<LaneInfoVo> laneInfoVoList = reqDao.getParkingLotUseInfoList(parkingLotReqVo);

        parkingLotInfoVo.setLaneInfoList(laneInfoVoList);
        parkingLotInfoVo.setTotalListCnt(String.valueOf(laneInfoVoList.size()));
        
        return parkingLotInfoVo;
    }


}
