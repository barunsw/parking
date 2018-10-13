package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgHeaderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MsgService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    public List<MsgHeaderVo> getMsgHeaderInfo() {
        return MsgDao.getMsgHeaderInfo();
    }

    // 메시지 수신시 사용.
    public int processMsg(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] headerBuffer = new byte[8];

            dis.read(headerBuffer);

            // 헤더 정보 열람
            List<MsgHeaderVo> msgHeaderInfo = getMsgHeaderInfo();

            int startPos = 0;
            int destPos = 0;

            int aa = 0;

            for ( MsgHeaderVo msgHeaderVo : msgHeaderInfo ) {
                byte[] colValue = new byte[msgHeaderVo.getColLength()];

                System.arraycopy(headerBuffer, startPos, colValue, destPos, msgHeaderVo.getColLength());

                if (msgHeaderVo.getColType().equals("u_int")) {

                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    System.out.println(k);

                    LOGGER.debug("{} | {}", msgHeaderVo.getFieldName(), k);

                }
                else if (msgHeaderVo.getColType().equals("byte")) {

                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.debug("{} | {}", msgHeaderVo.getFieldName(), str);
                }

                destPos = startPos;
                startPos += msgHeaderVo.getColLength();

                aa++;
                if (aa > 1) break;
            }

            LOGGER.debug("End of Parse.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

    public static int convertByteToInt(byte[] b)
    {
        int value= 0;
        for(int i=0; i<b.length; i++)
            value = (value << 8) | b[i];
        return value;
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;

        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }

}
