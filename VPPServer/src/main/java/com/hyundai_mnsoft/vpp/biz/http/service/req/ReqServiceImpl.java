package com.hyundai_mnsoft.vpp.biz.http.service.req;

import com.hyundai_mnsoft.vpp.biz.http.service.controlserver.ControlServerService;
import com.hyundai_mnsoft.vpp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// ### HTTP Request 처리 서비스 구현부.
@Service
public class ReqServiceImpl implements ReqService {

    private final ReqDao reqDao;
    private final ControlServerService controlServerService;

    @Autowired
    public ReqServiceImpl(ReqDao reqDao, ControlServerService controlServerService) {
        this.reqDao = reqDao;
        this.controlServerService = controlServerService;
    }

    public ParkingLotResVo getParkingLotInfo(ParkingLotReqVo parkingLotReqVo) {
        List<LaneInfoVo> laneInfoList = reqDao.getParkingLotInfo(parkingLotReqVo);

        // 전체 정보 갱신 ( 2018-11-12 수정 요청 사항)
        controlServerService.reloadLaneInfoStatus(parkingLotReqVo.getParkingAreaID());
        controlServerService.reloadParkingUse(parkingLotReqVo.getParkingAreaID());

        // 결과를 vo에 담아 전달.
        ParkingLotResVo parkingLotResVo = new ParkingLotResVo();

        parkingLotResVo.setParkingLotNo(parkingLotReqVo.getParkingAreaID());
        parkingLotResVo.setParkingLotNm("");
        parkingLotResVo.setLaneInfoList(laneInfoList);
        parkingLotResVo.setTotalListCnt(String.valueOf(laneInfoList.size()));

        return parkingLotResVo;
    }

    // 차량 상태 조회 (DB 조회)
    @Override
    public VehicleStatusInfoVo getVehicleStatusInfo(RequestVo requestVo) {
        return reqDao.getVehicleStatusInfo(requestVo);
    }

    // 차량 위치 조회 (DB 조회)
    @Override
    public VehicleTraceInfoVo getVehicleTraceInfo(RequestVo requestVo) {
        return reqDao.getVehicleTraceInfo(requestVo);
    }

    // 주차 건물 정보 (관제서버 연동)
    @Override
    public List<CodeMasterInfoVo> getCodeMasterInfo(CodeMasterReqVo codeMasterReqVo) {
        return controlServerService.getCodeMaster(codeMasterReqVo);
    }
}
