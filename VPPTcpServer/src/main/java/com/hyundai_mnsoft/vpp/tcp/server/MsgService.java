package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgHeaderVo;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MsgService {
    public List<MsgHeaderVo> getMsgHeaderInfo() {
        return MsgDao.getMsgHeaderInfo();
    }

    // 메시지 수신시 사용.
    public int processMsg() {

        return 0;
    }

    public byte[] makeMsg(String msgId) {
        // 메시지별로 구분
        // 발송되는 메시지에서 사용

        byte[] msg = new byte[0];
        return msg;
    }

    public byte[] makeResponseMsg() {
        // response message 발송 시 사용

        byte[] msg = new byte[0];
        return msg;
    }

    // byte 결합 시 사용
    public static final byte[] append(final byte[]... arrays) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (arrays != null) {
            for (final byte[] array : arrays) {
                if (array != null) {
                    out.write(array, 0, array.length);
                }
            }
        }
        return out.toByteArray();
    }
}
