package com.hyundai_mnsoft.vpp.biz.http.service;

import com.google.gson.Gson;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlServerServiceImpl implements ControlServerService{
    private static Logger LOGGER = Logger.getLogger(ControlServerServiceImpl.class);

    private ControlServerDao controlServerDao;
    private Gson gson = new Gson();

    @Autowired
    public ControlServerServiceImpl(ControlServerDao controlServerDao) {
        this.controlServerDao = controlServerDao;
    }

    ControlServerUtil util = new ControlServerUtil();

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

                int count = controlServerDao.getParkingLotUseInfoCount(vo.getLane_code());

                if ( count == 0 ) {
                    controlServerDao.insertParkingLotUseInfo(vo);
                }
                else {
                    controlServerDao.updateParkingLotUseInfo(vo);
                }
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

            JSONObject laneInfoStatus = (JSONObject)jsonObj.get("laneInfoStatus");

            return gson.fromJson(laneInfoStatus.toJSONString(), ParkingLotUseInfoVo.class);
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }
}
