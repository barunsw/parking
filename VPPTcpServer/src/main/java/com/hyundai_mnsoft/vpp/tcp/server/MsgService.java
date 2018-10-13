package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

//    public List<MsgMetaVo> getMsgMetaInfo() {
//        return MsgDao.getMsgMetaInfo();
//    }

    // 메시지 수신시 사용.
    public int processMsg(Socket socket) {

        Map headerMap = new HashMap<>();
        int bodyLen = 0;

        InputStream is = null;
        DataInputStream dis = null;

        OutputStream os = null;
        DataOutputStream dos = null;

        try {
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            byte[] headerBuffer = new byte[84];

            dis.read(headerBuffer);

            // 헤더 정보 열람
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int startPos = 0;
            int destPos = 0;

            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
                byte[] colValue = new byte[msgMetaVo.getColLength()];

                System.arraycopy(headerBuffer, startPos, colValue, destPos, msgMetaVo.getColLength());

                if (msgMetaVo.getColType().equals("u_int")) {


                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.debug("{} | {}", msgMetaVo.getFieldName(), k);

                    headerMap.put(msgMetaVo.getFieldName(), k);

                    if (msgMetaVo.getFieldName().equals("BodyLen")) {
                        bodyLen = k.intValue();
                    }

                }
                else if (msgMetaVo.getColType().equals("byte")) {
                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.debug("{} | {}", msgMetaVo.getFieldName(), str);

                    headerMap.put(msgMetaVo.getFieldName(), str);
                }


                startPos += msgMetaVo.getColLength();
            }

            LOGGER.debug("End of Parse.");

            String s;
            int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

            Map bodyMap = new HashMap();
            switch (msgId) {
                case 16781312:
                    bodyMap = vpp001(bodyLen, dis, headerMap);
                    break;
                case 16781313:
                    bodyMap = vpp002(bodyLen, dis, headerMap);
                    break;
                case 16781314:
                    bodyMap = vpp003(bodyLen, dis, headerMap);
                    break;
            }

            byte[] resMsg = makeResponseMsg(msgId, headerMap, bodyMap);

            dos.write(resMsg);
            dos.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                dis.close();
                is.close();
                dos.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    // 주차장 정보 요청
    private Map vpp001(int bodyLen, DataInputStream dis, Map headerMap) {
        byte[] bodyBuffer = new byte[bodyLen];
        int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

        Map bodyMap = new HashMap();

        try {
            dis.read(bodyBuffer);
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 0);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int startPos = 0;
            int destPos = 0;

            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
                byte[] colValue = new byte[msgMetaVo.getColLength()];

                System.arraycopy(bodyBuffer, startPos, colValue, destPos, msgMetaVo.getColLength());

                if (msgMetaVo.getColType().equals("u_int")) {
                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.debug("{} | {}", msgMetaVo.getFieldName(), k);

                    bodyMap.put(msgMetaVo.getFieldName(), k);
                }
                else if (msgMetaVo.getColType().equals("byte")) {
                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.debug("{} | {}", msgMetaVo.getFieldName(), str);

                    bodyMap.put(msgMetaVo.getFieldName(), str);
                }
                startPos += msgMetaVo.getColLength();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyMap;
    }

    // 차량 위치 정보 제공
    private Map vpp002(int bodyLen, DataInputStream dis, Map headerMap) {
        Map bodyMap = new HashMap();

        return bodyMap;
    }

    // 차량 상태 제공
    private Map vpp003(int bodyLen, DataInputStream dis, Map headerMap) {
        Map bodyMap = new HashMap();

        return bodyMap;
    }

    public byte[] makeMsg(String msgId) {
        // 메시지별로 구분
        // 발송되는 메시지에서 사용

        byte[] msg = new byte[0];
        return msg;
    }

    public byte[] makeResponseMsg(int msgId, Map headerMap, Map bodyMap) {
        // response message 발송 시 사용
        byte[] msg = new byte[0];

        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            makeMsgByteStream(msgId, baos, msgHeaderInfo, headerMap);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return baos.toByteArray();
    }

    private static void makeMsgByteStream(int msgId, ByteArrayOutputStream baos, List<MsgMetaVo> msgMetaInfo, Map metaMap) throws IOException {
        byte[] temp = new byte[0];
        boolean appendNullFlag = false;
        for (MsgMetaVo msgMetaVo : msgMetaInfo) {
            if ( !msgMetaVo.getFieldName().equals("BodyLen")) {
                int colLength = msgMetaVo.getColLength();
                String fieldValue = "";
                LOGGER.info(msgMetaVo.getFieldName());

                if (msgMetaVo.getColType().equals("u_int")) {
                    if ( metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O") ) {
                        temp = new byte[colLength];
                    }
                    else {
                        temp = ByteBuffer.allocate(4).putInt(
                                Integer.parseInt(metaMap.get(msgMetaVo.getFieldName()).toString())).array();
                    }
                }
                else if (msgMetaVo.getColType().equals("byte")) {
                    if ( metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O")) {
                        temp = new byte[colLength];
                    }
                    else {
                        fieldValue = metaMap.get(msgMetaVo.getFieldName()).toString();
                        temp = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes();

                        if ( fieldValue.length() < colLength ) {
                            appendNullFlag = true;
                        }
                    }
                }
                baos.write(temp);

                if (appendNullFlag) {
                    baos.write(new byte[colLength - fieldValue.length()]);
                    appendNullFlag = false;
                }
            }
            else {
                byte[] body = makeBody(msgId);
                baos.write(ByteBuffer.allocate(4).putInt(body.length).array());
                baos.write(body);
            }
        }
    }

    private static byte[] makeBody(int msgId) {
        ByteArrayOutputStream baos = null;

        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 1);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();
            Map bodyMap = new HashMap();

            //bodyMap을 채워주는 작업 필요
            if ( msgId == 16781312 ) {

            }
            else if ( msgId == 16781313 ) {

            }
            else if ( msgId == 16781314 ) {

            }

            makeMsgByteStream(msgId, baos, msgBodyInfo, bodyMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return baos.toByteArray();
    }

//    public static int convertByteToInt(byte[] b)
//    {
//        int value= 0;
//        for(int i=0; i<b.length; i++)
//            value = (value << 8) | b[i];
//        return value;
//    }
//
//    public static final byte[] intToByteArray(int value) {
//        return new byte[] {
//                (byte)(value >>> 24),
//                (byte)(value >>> 16),
//                (byte)(value >>> 8),
//                (byte)value};
//    }

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
