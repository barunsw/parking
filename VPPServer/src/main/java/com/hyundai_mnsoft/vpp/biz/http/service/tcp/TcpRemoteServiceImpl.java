package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.biz.http.service.controlserver.ControlServerService;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpLaneInfoVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TcpRemoteServiceImpl implements TcpRemoteService {
    private static Logger LOGGER = Logger.getLogger(TcpRemoteServiceImpl.class);

    private TcpRemoteDao tcpRemoteDao;

    public TcpRemoteServiceImpl(TcpRemoteDao tcpRemoteDao) {
        this.tcpRemoteDao = tcpRemoteDao;
    }

    @Autowired
    ControlServerService controlServerService;

    @Override
    public TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo) {

        LOGGER.info(parkingLotTcpReqVo.toString());

        // 인근 주차장을 판단하는 로직 필요. (lot, lat)

        // 임시 vo 생성
        String parkingAreaID = parkingLotTcpReqVo.getParkingLotID();
        ParkingLotReqVo parkingLotReqVo = new ParkingLotReqVo();
        parkingLotReqVo.setParkingAreaID(parkingAreaID);

        // 전체 정보 갱신 ( 2018-11-12 수정 요청 사항)
        controlServerService.reloadLaneInfoStatus(parkingAreaID);
        controlServerService.reloadParkingUse(parkingAreaID);

        List<TcpLaneInfoVo> laneInfoList = tcpRemoteDao.getParkingLotInfo(parkingLotReqVo);

//        for ( TcpLaneInfoVo laneInfoVo : laneCodeList ) {
//            String laneCode = laneInfoVo.getLaneCode();
//
//            // 정보 갱신
//            ParkingLotUseInfoVo parkingLotUseInfoVo = controlServerService.getLaneInfoStatus(laneCode);
//
//            laneInfoVo.setParkingLotNo(parkingLotUseInfoVo.getParkinglot_no());
//            laneInfoVo.setParkingLevelCode(parkingLotUseInfoVo.getParking_level_code());
//            laneInfoVo.setParkingZoneCode(parkingLotUseInfoVo.getParking_zone_code());
//            laneInfoVo.setLaneSeqNum(parkingLotUseInfoVo.getLane_seq_num());
//            laneInfoVo.setLaneName(parkingLotUseInfoVo.getLane_name());
//            laneInfoVo.setLaneNameLen(parkingLotUseInfoVo.getLane_name().getBytes().length);
//            laneInfoVo.setLaneType(parkingLotUseInfoVo.getLane_type());
//            laneInfoVo.setManageType(parkingLotUseInfoVo.getManage_type());
//            laneInfoVo.setLaneStatus(parkingLotUseInfoVo.getLane_status());
//
//            LOGGER.info(laneInfoVo.toString());
//
//            laneInfoList.add(laneInfoVo);
//
//            // 임시값 설정
////            parkingLotNo = parkingLotUseInfoVo.getParkinglot_no();
//        }

        TcpParkingLotResVo tcpParkingLotResVo = new TcpParkingLotResVo();

        tcpParkingLotResVo.setParkingLotNo(parkingAreaID);
        tcpParkingLotResVo.setParkingLotNm("Test");
        tcpParkingLotResVo.setLaneInfoList(laneInfoList);
        tcpParkingLotResVo.setTotalListCnt(String.valueOf(laneInfoList.size()));

        return tcpParkingLotResVo;
    }
}
