package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.google.gson.Gson;
import com.hyundai_mnsoft.vpp.vo.CodeMasterInfoVo;
import com.hyundai_mnsoft.vpp.vo.CodeMasterReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotBDLVInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControlServerServiceImpl implements ControlServerService {
    private static Logger LOGGER = Logger.getLogger(ControlServerServiceImpl.class);

    private ControlServerDao controlServerDao;
    private Gson gson = new Gson();

    @Autowired
    public ControlServerServiceImpl(ControlServerDao controlServerDao) {
        this.controlServerDao = controlServerDao;
    }

    private ControlServerUtil util = new ControlServerUtil();

    public void reloadLaneInfoStatus() {
        try {
            String result = util.getResFromControlServer("/main/laneinfo/status");

            LOGGER.debug(result);

            JSONParser parser = new JSONParser();
            Object resultJson = parser.parse(result);

            JSONObject jsonObj = (JSONObject) resultJson;

            LOGGER.debug(jsonObj.get("laneInfoStatus"));

            List<JSONObject> laneInfoStatusList = (List<JSONObject>) jsonObj.get("laneInfoStatus");

            for ( JSONObject laneInfoStatus : laneInfoStatusList ) {
                ParkingLotUseInfoVo vo = gson.fromJson(laneInfoStatus.toJSONString(), ParkingLotUseInfoVo.class);
                LOGGER.info(vo);
                controlServerDao.insertParkingLotUseInfo(vo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ParkingLotUseInfoVo getLaneInfoStatus(String lane_code) {
        try{
            String param = "{\"lane_code\" : \"" + lane_code + "\"}";
            String result = util.getResFromControlServer("/main/laneinfo/status", param);

            JSONParser parser = new JSONParser();
            Object resultJson = parser.parse(result);

            JSONObject jsonObj = (JSONObject) resultJson;

            LOGGER.debug(jsonObj.get("laneInfoStatus"));

            List<JSONObject> laneInfoStatusList = (List<JSONObject>) jsonObj.get("laneInfoStatus");

            ParkingLotUseInfoVo resultVo = gson.fromJson(laneInfoStatusList.get(0).toJSONString(), ParkingLotUseInfoVo.class);

            controlServerDao.insertParkingLotUseInfo(resultVo);

            // 정보 갱신 후 해당 내용 return.

            return resultVo;
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }

    public void reloadParkingUse() {
        try {
            String result = util.getResFromControlServer("/main/parkinguse");

            LOGGER.debug(result);

            JSONParser parser = new JSONParser();
            Object resultJson = parser.parse(result);

            JSONObject jsonObj = (JSONObject) resultJson;

            LOGGER.debug(jsonObj.get("laneInfoStatus"));

            List<JSONObject> parkingUseList = (List<JSONObject>) jsonObj.get("parkingUseSub");

            for ( JSONObject parkingUseInfo : parkingUseList ) {
                ParkingLotBDLVInfoVo vo = gson.fromJson(parkingUseInfo.toJSONString(), ParkingLotBDLVInfoVo.class);

                LOGGER.info(vo);
                controlServerDao.insertParkingLotBDLVInfo(vo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CodeMasterInfoVo> getCodeMaster(CodeMasterReqVo codeMasterReqVo) {
        try{
            String param = "{\"code_group\" : \"" + codeMasterReqVo.getCode_group() + "\"," +
                    "\"code\" : \"" + codeMasterReqVo.getCode() + "\"" +
                    "}";
            String result = util.getResFromControlServer("/manage/codemaster", param);

            JSONParser parser = new JSONParser();
            Object resultJson = parser.parse(result);

            JSONObject jsonObj = (JSONObject) resultJson;

            LOGGER.debug(jsonObj.get("codeMasterSub"));

            List<JSONObject> codeMasterSub = (List<JSONObject>) jsonObj.get("codeMasterSub");

            List<CodeMasterInfoVo> resultList = new ArrayList<>();

            for ( JSONObject a : codeMasterSub ) {
                CodeMasterInfoVo oneCodeMasterSub = gson.fromJson(a.toJSONString(), CodeMasterInfoVo.class);
                resultList.add(oneCodeMasterSub);
            }

            return resultList;
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }
}
