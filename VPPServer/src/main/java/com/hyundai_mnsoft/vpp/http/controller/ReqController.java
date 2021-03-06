package com.hyundai_mnsoft.vpp.http.controller;

import com.hyundai_mnsoft.vpp.biz.http.service.req.ReqService;
import com.hyundai_mnsoft.vpp.tcp.RmiControl;
import com.hyundai_mnsoft.vpp.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// ### HTTP Request를 처리하는 메인 Controller.
@Controller
@RequestMapping("/")
public class ReqController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqController.class);

    private final ReqService reqService;

    @Autowired
    public ReqController(ReqService reqService) {
        this.reqService = reqService;
    }

    // 주차장 정보 요청
    @RequestMapping(value = "/vpp101", method = RequestMethod.POST)
    public @ResponseBody
    ParkingLotResVo vpp101(@RequestHeader HttpHeaders headers,
                                @RequestBody(required = false) ParkingLotReqVo requestBody,
                                HttpServletResponse res) {
        LOGGER.debug("\nVPP101\n");

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        ParkingLotResVo parkingLotResVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            // 주차장 정보 조회.
            parkingLotResVo = reqService.getParkingLotInfo(requestBody);

            LOGGER.debug(parkingLotResVo.toString());

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-101");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return parkingLotResVo;
    }

    // 원격시동
    @RequestMapping(value = "/vpp102", method = RequestMethod.POST)
    public @ResponseBody void vpp102(@RequestHeader HttpHeaders headers,
    @RequestBody(required = false) RemoteControlReqInfoVo requestBody,
    HttpServletResponse res) {
        LOGGER.debug("\nVPP102\n");

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            // 메시지 전송 후 결과값(errCode) 받아옴.
            // RMI 통해 TCP Server 호출, 메시지 발송.
            int result = RmiControl.sendVpp202Msg(requestVo, requestBody);

            LOGGER.debug(String.valueOf(result));
            
            if ( result == 0 ) {
                res.setHeader("errCode", "0");
                res.setHeader("errDesc", "Success");
            }
            else {
                res.setHeader("errCode", String.valueOf(result));
                res.setHeader("errDesc", "Fail - tcp service error.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-102");
            res.setHeader("NadId", requestVo.getNadId());
        }
    }

    // 차량상태 요청
    @RequestMapping(value = "/vpp103", method = RequestMethod.POST)
    public @ResponseBody VehicleStatusInfoVo vpp103(@RequestHeader HttpHeaders headers,
                                     HttpServletResponse res) {
        LOGGER.debug("\nVPP103\n");

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        VehicleStatusInfoVo vehicleStatusInfoVo = null;
        try {
            requestVo = processHeader(headers);

            // DB 조회
            vehicleStatusInfoVo = reqService.getVehicleStatusInfo(requestVo);

            LOGGER.debug(vehicleStatusInfoVo.toString());

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-103");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return vehicleStatusInfoVo;
    }

    // 출차 요청
    @RequestMapping(value = "/vpp104", method = RequestMethod.POST)
    public @ResponseBody
    RemoteControlResInfoVo vpp104(@RequestHeader HttpHeaders headers,
                                     @RequestBody(required = false) RemoteControlReqInfoVo requestBody,
                                     HttpServletResponse res) {
        LOGGER.debug("\nVPP104\n");

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo = null;
        RemoteControlResInfoVo remoteControlResInfoVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            // 메시지 전송 후 결과값(vo) 받아옴.
            // RMI 통해 TCP Server 호출, 메시지 발송.
            tcpRemoteControlResInfoVo = RmiControl.sendVpp004Msg(requestVo, requestBody);

            LOGGER.debug(tcpRemoteControlResInfoVo.toString());

            // Tcp 통신의 결과와 errCode를 함께 받아옴
            int result = Integer.parseInt(tcpRemoteControlResInfoVo.getErrCode());

            // 불필요한 errCode를 제거한 후 Response에 담아줌
            remoteControlResInfoVo = new RemoteControlResInfoVo(tcpRemoteControlResInfoVo);

            if ( result == 0 ) {
                res.setHeader("errCode", "0");
                res.setHeader("errDesc", "Success");
            }
            else {
                res.setHeader("errCode", String.valueOf(result));
                res.setHeader("errDesc", "Fail - tcp service error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-104");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return remoteControlResInfoVo;
    }

    // 주차 요청
    @RequestMapping(value = "/vpp105", method = RequestMethod.POST)
    public @ResponseBody
    RemoteControlResInfoVo vpp105(@RequestHeader HttpHeaders headers,
                                     @RequestBody(required = false) RemoteControlReqInfoVo requestBody,
                                     HttpServletResponse res) {
        LOGGER.debug("\nVPP105\n");
        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo = null;
        RemoteControlResInfoVo remoteControlResInfoVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            // 메시지 전송 후 결과값(vo) 받아옴.
            // RMI 통해 TCP Server 호출, 메시지 발송.
            tcpRemoteControlResInfoVo = RmiControl.sendVpp005Msg(requestVo, requestBody);
            LOGGER.debug(tcpRemoteControlResInfoVo.toString());

            int result = Integer.parseInt(tcpRemoteControlResInfoVo.getErrCode());

            remoteControlResInfoVo = new RemoteControlResInfoVo(tcpRemoteControlResInfoVo);

            if ( result == 0 ) {
                res.setHeader("errCode", "0");
                res.setHeader("errDesc", "Success");
            }
            else {
                res.setHeader("errCode", String.valueOf(result));
                res.setHeader("errDesc", "Fail - tcp service error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-105");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return remoteControlResInfoVo;
    }

    // 차량 위치 조회
    @RequestMapping(value = "/vpp106", method = RequestMethod.POST)
    public @ResponseBody VehicleTraceInfoVo vpp106(@RequestHeader HttpHeaders headers,
                                                       HttpServletResponse res) {
        LOGGER.debug("\nVPP106\n");
        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        VehicleTraceInfoVo vehicleTraceInfoVo = null;
        try {
            requestVo = processHeader(headers);

            // DB 조회
            vehicleTraceInfoVo = reqService.getVehicleTraceInfo(requestVo);

            LOGGER.debug(vehicleTraceInfoVo.toString());

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");

        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-106");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return vehicleTraceInfoVo;
    }

    // 주차장 정보 요청
    @RequestMapping(value = "/vpp201", method = RequestMethod.POST)
    public @ResponseBody
    void vpp201(@RequestHeader HttpHeaders headers,
                           @RequestBody(required = false) ParkingLotReqVo requestBody,
                           HttpServletResponse res) {
        LOGGER.debug("\nVPP201\n");

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            reqService.reloadParkingLotInfo(requestBody);

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "VPP-201");
            res.setHeader("NadId", requestVo.getNadId());
        }
    }

    @RequestMapping(value = "/codemaster", method = RequestMethod.POST)
    public @ResponseBody
    List<CodeMasterInfoVo> test(@RequestHeader(required = false) HttpHeaders headers,  @RequestBody(required = false) CodeMasterReqVo requestBody, HttpServletResponse res) {
        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        List<CodeMasterInfoVo> codeMasterInfoVoList = null;
        try {
            // 헤더 정보에 대한 정의가 없기떄문에 임시로 try 감싸줌.
            try {
                requestVo = processHeader(headers);

                LOGGER.debug(requestBody.toString());
            }
            catch(Exception e){

            }

            codeMasterInfoVoList = reqService.getCodeMasterInfo(requestBody);

            LOGGER.debug(codeMasterInfoVoList.toString());

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - working error.");
        }
        finally {
            res.setHeader("MsgId", "TEST");
            if ( requestVo != null ) {
                res.setHeader("NadId", requestVo.getNadId());
            }
        }

        return codeMasterInfoVoList;
    }

    /*
     * 2018-09-10 17:33:06 강현규
     * Header를 Vo로 관리하는게 좋은 듯 하여 값을 전부 집어넣음.
     */
    private RequestVo processHeader(HttpHeaders headers) throws Exception{
        RequestVo requestVo = new RequestVo();

        requestVo.setServiceId(headers.get("ServiceId").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setVersion(headers.get("Version").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setNadId(headers.get("NadId").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setCoordinate(headers.get("coordinate").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setReqCompression(headers.get("ReqCompression").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setReqEncrption(headers.get("ReqEncrption").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setReqFormat(headers.get("ReqFormat").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setRespCompression(headers.get("RespCompression").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setRespEncrption(headers.get("RespEncrption").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setRespFormat(headers.get("RespFormat").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setCountry(headers.get("Country").toString().replaceAll("\\[(.*)]", "$1"));
        requestVo.setDate(headers.get("Date").toString().replaceAll("\\[(.*)]", "$1"));

        if (headers.get("lon") != null) {
            requestVo.setLon(headers.get("lon").toString().replaceAll("\\[(.*)]", "$1"));
        }
        if (headers.get("lat") != null) {
            requestVo.setLat(headers.get("lat").toString().replaceAll("\\[(.*)]", "$1"));
        }
        if (headers.get("alter") != null) {
            requestVo.setAlter(headers.get("alter").toString().replaceAll("\\[(.*)]", "$1"));
        }

        LOGGER.info(requestVo.toString());

        return requestVo;
    }
}
