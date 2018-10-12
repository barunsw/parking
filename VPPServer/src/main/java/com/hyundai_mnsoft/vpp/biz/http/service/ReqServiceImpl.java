package com.hyundai_mnsoft.vpp.biz.http.service;

import com.hyundai_mnsoft.vpp.vo.LaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;
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

    public ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) {
        ParkingLotResVo parkingLotResVo = reqDao.getParkingLotInfo(parkingLotReqVo);
        List<LaneInfoVo> laneInfoVoList = reqDao.getParkingLotUseInfoList(parkingLotReqVo);

        parkingLotResVo.setParkingLotNo(parkingLotReqVo.getParkingAreaID());
        parkingLotResVo.setLaneInfoList(laneInfoVoList);
        parkingLotResVo.setTotalListCnt(String.valueOf(laneInfoVoList.size()));
        
        return parkingLotResVo;
    }


}
