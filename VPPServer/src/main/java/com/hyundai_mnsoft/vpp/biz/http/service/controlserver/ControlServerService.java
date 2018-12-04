package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.CodeMasterInfoVo;
import com.hyundai_mnsoft.vpp.vo.CodeMasterReqVo;

import java.util.List;

public interface ControlServerService {
    public void reloadLaneInfoStatus(String parkingAreaID);
    public void reloadParkingUse(String parkingAreaID);
    public List<CodeMasterInfoVo> getCodeMaster(CodeMasterReqVo codeMasterReqVo);
}
