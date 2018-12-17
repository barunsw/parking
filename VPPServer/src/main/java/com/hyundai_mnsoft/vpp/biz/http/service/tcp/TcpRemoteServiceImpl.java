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

// ### TCP Server 연동 서비스 구현부.
@Service
public class TcpRemoteServiceImpl implements TcpRemoteService {
    private static Logger LOGGER = Logger.getLogger(TcpRemoteServiceImpl.class);

    private TcpRemoteDao tcpRemoteDao;
    private final ControlServerService controlServerService;

    @Autowired
    public TcpRemoteServiceImpl(TcpRemoteDao tcpRemoteDao, ControlServerService controlServerService) {
        this.tcpRemoteDao = tcpRemoteDao;
        this.controlServerService = controlServerService;
    }

    // 주차장 정보 요청.
    @Override
    public TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo parkingLotTcpReqVo) {

        LOGGER.info(parkingLotTcpReqVo.toString());

        // 추후 인근 주차장을 판단하는 로직 필요. (lot, lat).
        // 임시 vo 생성.
        String parkingAreaID = parkingLotTcpReqVo.getParkingLotID();
        ParkingLotReqVo parkingLotReqVo = new ParkingLotReqVo();
        parkingLotReqVo.setParkingAreaID(parkingAreaID);

        // 전체 정보 갱신 ( 2018-11-12 수정 요청 사항).
        controlServerService.reloadLaneInfoStatus(parkingAreaID);
        controlServerService.reloadParkingUse(parkingAreaID);

        List<TcpLaneInfoVo> laneInfoList = tcpRemoteDao.getParkingLotInfo(parkingLotReqVo);

        // 결과값 vo에 담아 전달.
        TcpParkingLotResVo tcpParkingLotResVo = new TcpParkingLotResVo();

        tcpParkingLotResVo.setParkingLotNo(parkingAreaID);
        tcpParkingLotResVo.setParkingLotNm("Test");
        tcpParkingLotResVo.setLaneInfoList(laneInfoList);
        tcpParkingLotResVo.setTotalListCnt(String.valueOf(laneInfoList.size()));

        return tcpParkingLotResVo;
    }
}
