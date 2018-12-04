package com.hyundai_mnsoft.vpp.sim;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.tcp.server.CommonUtil;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimMsgService extends CommonUtil {
    private static Logger LOGGER = Logger.getLogger(SimMsgService.class);


    // 메시지 수신시 사용.
    public int processMsg(Socket socket) {

        Map headerMap = new HashMap<>();
        Map bodyMap = new HashMap();
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

            byte[] headerBuffer = new byte[88];

            dis.read(headerBuffer);

            // 헤더 정보 열람
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int startPos = 0;
            int destPos = 0;

            LOGGER.info("@@@@@ Parsing Start @@@@@");

            getMsgData(headerMap, headerBuffer, msgHeaderInfo, startPos);

            bodyLen = Integer.parseInt(headerMap.get("BodyLen").toString());

            if ( bodyLen > 0 ) {
                byte[] bodyBuffer = new byte[bodyLen];
                int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

                dis.read(bodyBuffer);

                // 헤더 정보 열람
                msgMetaSearchVo = new MsgMetaSearchVo(msgId, 0);
                List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                startPos = 0;

                getMsgData(bodyMap, bodyBuffer, msgBodyInfo, startPos);
            }

            LOGGER.info("@@@@@ End of Parse @@@@@");

            String s;
            int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

//            Map bodyMap = new HashMap();
//            switch (msgId) {
//                case 16781312:
//                    bodyMap = vpp001(bodyLen, dis, headerMap);
//                    break;
//                case 16781313:
//                    bodyMap = vpp002(bodyLen, dis, headerMap);
//                    break;
//                case 16781314:
//                    bodyMap = vpp003(bodyLen, dis, headerMap);
//                    break;
//            }

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

    public static void getMsgData(Map dataMap, byte[] headerBuffer, List<MsgMetaVo> msgHeaderInfo, int startPos) {
        int variableColLength = 0;
        for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
            if ( msgMetaVo.getColLength() == 0 ) {
                msgMetaVo.setColLength(variableColLength);
                variableColLength = 0;
            }

            byte[] colValue = new byte[msgMetaVo.getColLength()];

            System.arraycopy(headerBuffer, startPos, colValue, 0, msgMetaVo.getColLength());

            switch (msgMetaVo.getColType()) {
                case "u_int":
                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.info(msgMetaVo.getFieldName() + " | " + k);

                    dataMap.put(msgMetaVo.getFieldName(), k);
                    break;
                case "short_int":
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.put(colValue);

                    Short s = byteBuffer.getShort();

                    LOGGER.info(msgMetaVo.getFieldName() + " | " + s);

                    if (msgMetaVo.getColWorkType().equals("L")) {
                        variableColLength = s;
                    }
                    break;
                case "byte":
                case "char":
                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.info(msgMetaVo.getFieldName() + " | " + str.trim());

                    dataMap.put(msgMetaVo.getFieldName(), str.trim());
                    break;
            }
            startPos += msgMetaVo.getColLength();
        }
    }

    public byte[] makeResponseMsg(int msgId, Map headerMap, Map bodyMap) {
        // response message 발송 시 사용
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            headerMap.put("ErrCode", 0);

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

    private static byte[] makeResponseMsgBody(int msgId) {
        ByteArrayOutputStream baos = null;

        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 1);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();
            Map bodyMap = new HashMap();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
            String date = sdf.format(new Date());

            //bodyMap을 채워주는 작업 필요
            if ( msgId == 16781315 ) {
                bodyMap.put("respTime", date);
                bodyMap.put("routeData", null);
            }
            else if ( msgId == 16781316 ) {
                bodyMap.put("respTime", date);
                bodyMap.put("routeData", null);
            }
            else if ( msgId == 16781317 ) {
                // ResponseBody 없음.
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

    private static void makeMsgByteStream(int msgId, ByteArrayOutputStream baos, List<MsgMetaVo> msgMetaInfo, Map metaMap) throws IOException {
        byte[] temp = new byte[0];
        boolean appendNullFlag = false;

        for (MsgMetaVo msgMetaVo : msgMetaInfo) {
            if ( !msgMetaVo.getFieldName().equals("BodyLen")) {
                int colLength = msgMetaVo.getColLength();
                String fieldValue = "";
                LOGGER.info(msgMetaVo.getFieldName());

                switch (msgMetaVo.getColType()) {
                    case "u_int":
                        if (metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O")) {
                            temp = new byte[colLength];
                        }
                        else {
                            temp = ByteBuffer.allocate(4).putInt(
                                    Integer.parseInt(metaMap.get(msgMetaVo.getFieldName()).toString())).array();
                        }
                        break;
                    case "short_int":
                        LOGGER.warn(metaMap.get(msgMetaVo.getFieldName()).toString());

                        if (metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O")) {
                            temp = new byte[colLength];
                        }
                        else {
                            temp = ByteBuffer.allocate(2).putShort(
                                    Short.parseShort(metaMap.get(msgMetaVo.getFieldName()).toString())).array();

                            LOGGER.debug(String.valueOf(byteArrayToShort(temp)));
                        }
                        break;
                    case "byte":
                    case "char":
                        if (colLength == 0) {
                            // 가변 길이 처리
                            if ( metaMap.get(msgMetaVo.getFieldName()) != null ) {
                                colLength = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes().length;
                            }
                        }

                        if (metaMap.get(msgMetaVo.getFieldName()) == null) {
                            LOGGER.info("NULL " + msgMetaVo.getFieldName());

                            if ( msgMetaVo.getColReqType().equals("O") ) {
                                temp = new byte[colLength];
                            }
                            else {
                                temp = new byte[0];
                                appendNullFlag = true;
                            }
                        }
                        else {
                            fieldValue = metaMap.get(msgMetaVo.getFieldName()).toString();
                            temp = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes();

                            if (fieldValue.getBytes().length < colLength) {
                                appendNullFlag = true;


                            }

                            LOGGER.warn(metaMap.get(msgMetaVo.getFieldName()).toString());
                        }
                        break;
                }

                baos.write(temp);

                LOGGER.debug(temp.toString());

                if (appendNullFlag) {
                    baos.write(new byte[colLength - fieldValue.getBytes().length]);
                    appendNullFlag = false;
                }
            }
            else {
                byte[] body = makeResponseMsgBody(msgId);
                baos.write(ByteBuffer.allocate(4).putInt(body.length).array());
                baos.write(body);
            }

            // 배열일 경우에 실행.
            if ( msgMetaVo.getColWorkType().equals("A")) {

            }
        }
    }
}
