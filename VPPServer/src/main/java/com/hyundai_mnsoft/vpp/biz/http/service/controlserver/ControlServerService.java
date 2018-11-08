package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import com.hyundai_mnsoft.vpp.vo.CodeMasterInfoVo;
import com.hyundai_mnsoft.vpp.vo.CodeMasterReqVo;
import com.hyundai_mnsoft.vpp.vo.ParkingLotUseInfoVo;

import java.util.List;

public interface ControlServerService {
    public void reloadLaneInfoStatus();
    public ParkingLotUseInfoVo getLaneInfoStatus(String lane_code);
    public void reloadParkingUse();
    public List<CodeMasterInfoVo> getCodeMaster(CodeMasterReqVo codeMasterReqVo);
}
