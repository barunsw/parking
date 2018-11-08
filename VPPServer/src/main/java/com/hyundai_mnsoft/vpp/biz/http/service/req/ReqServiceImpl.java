package com.hyundai_mnsoft.vpp.biz.http.service.req;

import com.hyundai_mnsoft.vpp.biz.http.service.controlserver.ControlServerService;
import com.hyundai_mnsoft.vpp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReqServiceImpl implements ReqService {

    private final ReqDao reqDao;

    @Autowired
    ControlServerService controlServerService;

    @Autowired
    public ReqServiceImpl(ReqDao reqDao) {
        this.reqDao = reqDao;
    }

    public ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) {
        ParkingLotUseSearchVo parkingLotUseSearchVo = reqDao.getParkingLotInfo(parkingLotReqVo);
        List<LaneInfoVo> laneCodeList = reqDao.getParkingLotUseLaneCodeList(parkingLotUseSearchVo);
        List<LaneInfoVo> laneInfoList = new ArrayList<>();

        String parkingLotNo = "";

        for ( LaneInfoVo laneInfoVo : laneCodeList ) {
            String laneCode = laneInfoVo.getLaneCode();

            // 정보 갱신
            ParkingLotUseInfoVo parkingLotUseInfoVo = controlServerService.getLaneInfoStatus(laneCode);

            laneInfoVo.setParkingLotNo(parkingLotUseInfoVo.getParkinglot_no());
            laneInfoVo.setParkingLevelCode(parkingLotUseInfoVo.getParking_level_code());
            laneInfoVo.setParkingZoneCode(parkingLotUseInfoVo.getParking_zone_code());
            laneInfoVo.setLaneSeqNum(parkingLotUseInfoVo.getLane_seq_num());
            laneInfoVo.setLaneName(parkingLotUseInfoVo.getLane_name());
            laneInfoVo.setLaneType(parkingLotUseInfoVo.getLane_type());
            laneInfoVo.setManageType(parkingLotUseInfoVo.getManage_type());
            laneInfoVo.setLaneStatus(parkingLotUseInfoVo.getLane_status());

            laneInfoList.add(laneInfoVo);

            parkingLotNo = parkingLotUseInfoVo.getParkinglot_no();
        }


        ParkingLotResVo parkingLotResVo = new ParkingLotResVo();

        parkingLotResVo.setParkingLotNo(parkingLotNo);
        parkingLotResVo.setParkingLotNm("");
        parkingLotResVo.setLaneInfoList(laneInfoList);
        parkingLotResVo.setTotalListCnt(String.valueOf(laneInfoList.size()));

        return parkingLotResVo;
    }

    @Override
    public VehicleStatusInfoVo getVehicleStatusInfo(RequestVo requestVo) {
        return reqDao.getVehicleStatusInfo(requestVo);
    }

    @Override
    public VehicleTraceInfoVo getVehicleTraceInfo(RequestVo requestVo) {
        return reqDao.getVehicleTraceInfo(requestVo);
    }

    @Override
    public List<CodeMasterInfoVo> getCodeMasterInfo(CodeMasterReqVo codeMasterReqVo) {
        return controlServerService.getCodeMaster(codeMasterReqVo);
    }
}
