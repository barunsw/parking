package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.google.gson.Gson;
import com.hyundai_mnsoft.vpp.vo.CodeMasterInfoVo;
import com.hyundai_mnsoft.vpp.vo.CodeMasterReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotBDLVInfoVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// ### 관제서버 연동 서비스 구현부.
@Service
public class ControlServerServiceImpl implements ControlServerService {
    private static Logger LOGGER = Logger.getLogger(ControlServerServiceImpl.class);

    private ControlServerDao controlServerDao;
    // Gson 으로 Json 데이터를 Vo로 변환.
    private Gson gson = new Gson();

    @Autowired
    public ControlServerServiceImpl(ControlServerDao controlServerDao) {
        this.controlServerDao = controlServerDao;
    }

    private ControlServerUtil util = new ControlServerUtil();

    // String 을 JSONObject 형태로 만들어 전달.
    private JSONObject getJsonObjectByString(String result) throws Exception {
        // result가 "" 이면 관제서버와 연동 불가한 상황.
        if ( result.equals("") ) {
            throw new Exception("관제서버 연동 에러.");
        }

        JSONParser parser = new JSONParser();
        Object resultJson = parser.parse(result);

        return (JSONObject) resultJson;
    }

    // 주차면의 주차한 차량의 정보 갱신.
    public void reloadLaneInfoStatus(String parkingAreaID) {
        try {
            String result = util.getResFromControlServer(parkingAreaID, "/main/laneinfo/status", null);

            JSONObject jsonObj = getJsonObjectByString(result);

            List<JSONObject> laneInfoStatusList = (List<JSONObject>) jsonObj.get("laneInfoStatus");

            for ( JSONObject laneInfoStatus : laneInfoStatusList ) {
                ParkingLotUseInfoVo vo = gson.fromJson(laneInfoStatus.toJSONString(), ParkingLotUseInfoVo.class);
                LOGGER.info(vo);
                // 해당 정보 DB 갱신.
                controlServerDao.insertParkingLotUseInfo(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 주차장 층별 주차현황 정보 갱신.
    public void reloadParkingUse(String parkingAreaID) {
        try {
            String result = util.getResFromControlServer(parkingAreaID,"/main/parkinguse", null);

            JSONObject jsonObj = getJsonObjectByString(result);

            List<JSONObject> parkingUseList = (List<JSONObject>) jsonObj.get("parkingUseSub");

            for ( JSONObject parkingUseInfo : parkingUseList ) {
                ParkingLotBDLVInfoVo vo = gson.fromJson(parkingUseInfo.toJSONString(), ParkingLotBDLVInfoVo.class);
                LOGGER.info(vo);
                // 해당 정보 DB 갱신
                controlServerDao.insertParkingLotBDLVInfo(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 주차 건물 정보 갱신.
    @Override
    public List<CodeMasterInfoVo> getCodeMaster(CodeMasterReqVo codeMasterReqVo) {
        try{
            String param = "{\"code_group\" : \"" + codeMasterReqVo.getCode_group() + "\"," +
                    "\"code\" : \"" + codeMasterReqVo.getCode() + "\"" +
                    "}";
            String result = util.getResFromControlServer(codeMasterReqVo.getParkingAreaID(), "/manage/codemaster", param);

            JSONObject jsonObj = getJsonObjectByString(result);

            List<JSONObject> codeMasterSub = (List<JSONObject>) jsonObj.get("codeMasterSub");

            List<CodeMasterInfoVo> resultList = new ArrayList<>();

            for ( JSONObject a : codeMasterSub ) {
                CodeMasterInfoVo oneCodeMasterSub = gson.fromJson(a.toJSONString(), CodeMasterInfoVo.class);
                resultList.add(oneCodeMasterSub);
            }

            // 현재는 조회된 값만 전달하게 되어있음. (DB 작업 없음)
            return resultList;
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }
}
