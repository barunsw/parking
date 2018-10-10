package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgHeaderVo;

import java.util.List;

public class MsgService {
    public List<MsgHeaderVo> getMsgHeaderInfo() {
        return MsgDao.getMsgHeaderInfo();
    }
}
