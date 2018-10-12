package com.hyundai_mnsoft.vpp.http.controller;

import com.google.gson.Gson;
import com.hyundai_mnsoft.vpp.biz.http.service.ReqService;
import com.hyundai_mnsoft.vpp.vo.ParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotResVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;
import com.hyundai_mnsoft.vpp.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class ReqController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqController.class);

    private final ReqService reqService;

    @Autowired
    public ReqController(ReqService reqService) {
        this.reqService = reqService;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public @ResponseBody
    ResponseVo reqWorker(@RequestHeader HttpHeaders headers, @RequestBody(required = false) Object requestBody) {
        ResponseVo responseVo = new ResponseVo();

        LOGGER.debug(headers.toString());

        try {
            LOGGER.debug(processHeader(headers).toString());

            if (requestBody != null) {
                LOGGER.debug(requestBody.toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseVo;
    }

    @RequestMapping(value = "/vpp101", method = RequestMethod.POST)
    public @ResponseBody
    ParkingLotResVo parkinglots(@RequestHeader HttpHeaders headers,
                                @RequestBody(required = false) Object requestBody,
                                HttpServletResponse res) {

        LOGGER.debug(headers.toString());
        RequestVo requestVo = null;
        ParkingLotResVo parkingLotResVo = null;
        try {
            requestVo = processHeader(headers);

            LOGGER.debug(requestBody.toString());

            Gson gson = new Gson();
            ParkingLotReqVo requestInfo = gson.fromJson(requestBody.toString(), ParkingLotReqVo.class);

            LOGGER.debug(requestInfo.toString());

            parkingLotResVo = reqService.getParkingLotInfo(requestInfo);

            LOGGER.debug(parkingLotResVo.toString());

            res.setHeader("errCode", "0");
            res.setHeader("errDesc", "Success");

        } catch (Exception e) {
            e.printStackTrace();
            res.setHeader("errCode", "1");
            res.setHeader("errDesc", "Fail - 작업 중 오류.");
        }
        finally {
            res.setHeader("MsgId", "VPP-101");
            res.setHeader("NadId", requestVo.getNadId());
        }

        return parkingLotResVo;
    }

    /*
     * 2018-09-10 17:33:06 강현규
     * Header를 Vo로 관리하는게 좋은 듯 하여 값을 전부 집어넣음.
     */
    private RequestVo processHeader(HttpHeaders headers) throws Exception{
        RequestVo requestVo = new RequestVo();

        requestVo.setServiceId(headers.get("ServiceId").toString());
        requestVo.setVersion(headers.get("Version").toString());
        requestVo.setVIN(headers.get("VIN").toString());
        requestVo.setNadId(headers.get("NadId").toString());
        requestVo.setCoordinate(headers.get("coordinate").toString());
        requestVo.setReqCompression(headers.get("ReqCompression").toString());
        requestVo.setReqEncryption(headers.get("ReqEncryption").toString());
        requestVo.setReqFormat(headers.get("ReqFormat").toString());
        requestVo.setRespCompression(headers.get("RespCompression").toString());
        requestVo.setRespEncryption(headers.get("RespEncryption").toString());
        requestVo.setRespFormat(headers.get("RespFormat").toString());
        requestVo.setCountry(headers.get("Country").toString());
        requestVo.setDate(headers.get("Date").toString());

        if (headers.get("lon") != null) {
            requestVo.setLon(headers.get("lon").toString());
        }
        if (headers.get("lat") != null) {
            requestVo.setLat(headers.get("lat").toString());
        }
        if (headers.get("alter") != null) {
            requestVo.setAlter(headers.get("alter").toString());
        }

        return requestVo;
    }
}
