package com.hyundai_mnsoft.vpp.biz.http.service.tcp;

import com.hyundai_mnsoft.vpp.biz.http.service.controlserver.ControlServerService;
import com.hyundai_mnsoft.vpp.vo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        ParkingLotUseSearchVo parkingLotUseSearchVo = new ParkingLotUseSearchVo();
        parkingLotUseSearchVo.setPlId("43046");
        parkingLotUseSearchVo.setRlId("23550");

        List<TcpLaneInfoVo> laneCodeList = tcpRemoteDao.getParkingLotUseLaneCodeList(parkingLotUseSearchVo);
        List<TcpLaneInfoVo> laneInfoList = new ArrayList<>();

        String parkingLotNo = "";

        for ( TcpLaneInfoVo laneInfoVo : laneCodeList ) {
            String laneCode = laneInfoVo.getLaneCode();

            // 정보 갱신
            ParkingLotUseInfoVo parkingLotUseInfoVo = controlServerService.getLaneInfoStatus(laneCode);

            laneInfoVo.setParkingLotNo(parkingLotUseInfoVo.getParkinglot_no());
            laneInfoVo.setParkingLevelCode(parkingLotUseInfoVo.getParking_level_code());
            laneInfoVo.setParkingZoneCode(parkingLotUseInfoVo.getParking_zone_code());
            laneInfoVo.setLaneSeqNum(parkingLotUseInfoVo.getLane_seq_num());
            laneInfoVo.setLaneName(parkingLotUseInfoVo.getLane_name());
            laneInfoVo.setLaneNameLen(parkingLotUseInfoVo.getLane_name().getBytes().length);
            laneInfoVo.setLaneType(parkingLotUseInfoVo.getLane_type());
            laneInfoVo.setManageType(parkingLotUseInfoVo.getManage_type());
            laneInfoVo.setLaneStatus(parkingLotUseInfoVo.getLane_status());

            LOGGER.info(laneInfoVo.toString());

            laneInfoList.add(laneInfoVo);

            // 임시값 설정
            parkingLotNo = parkingLotUseInfoVo.getParkinglot_no();
        }

        TcpParkingLotResVo tcpParkingLotResVo = new TcpParkingLotResVo();

        tcpParkingLotResVo.setParkingLotNo(parkingLotNo);
        tcpParkingLotResVo.setParkingLotNm("Test");
        tcpParkingLotResVo.setLaneInfoList(laneInfoList);
        tcpParkingLotResVo.setTotalListCnt(String.valueOf(laneInfoList.size()));

        return tcpParkingLotResVo;
    }
}
