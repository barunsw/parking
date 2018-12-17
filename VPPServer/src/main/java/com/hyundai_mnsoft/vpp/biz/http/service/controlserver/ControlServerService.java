package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.CodeMasterInfoVo;
import com.hyundai_mnsoft.vpp.vo.CodeMasterReqVo;

import java.util.List;

// ### 관제서버와 연동하는 서비스의 interface.
// 주차 유도 프로토콜 정의서 참조.
public interface ControlServerService {
    // * 주차면의 주차한 차량의 정보
    public void reloadLaneInfoStatus(String parkingAreaID);
    // * 주차장 층별 주차현황 정보
    public void reloadParkingUse(String parkingAreaID);
    // * 주차 건물 정보
    public List<CodeMasterInfoVo> getCodeMaster(CodeMasterReqVo codeMasterReqVo);
}
