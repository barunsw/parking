package com.hyundai_mnsoft.vpp.sim;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendTester {
    private static Logger LOGGER = Logger.getLogger(SendTester.class);

    private static final int VPP_001 = 16781312;
    private static final int VPP_002 = 16781313;
    private static final int VPP_003 = 16781314;

    public static void main(String[] args) {

        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 30000);
            socket.setSoTimeout(3000);

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] sendData = makeTestMsg(VPP_001);

            dos.write(sendData);
            dos.flush();

            // Response의 Header
            byte[] headerBuffer = new byte[23];

            dis.read(headerBuffer);

            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int startPos = 0;
            int destPos = 0;

            Map headerMap = new HashMap();
            Map bodyMap = new HashMap();
            int bodyLen = 0;

            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
                byte[] colValue = new byte[msgMetaVo.getColLength()];

                System.arraycopy(headerBuffer, startPos, colValue, destPos, msgMetaVo.getColLength());

                if (msgMetaVo.getColType().equals("u_int")) {
                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + k);

                    headerMap.put(msgMetaVo.getFieldName(), k);

                    if (msgMetaVo.getFieldName().equals("BodyLen")) {
                        bodyLen = k.intValue();
                    }
                }
                else if (msgMetaVo.getColType().equals("byte")) {
                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + str);

                    headerMap.put(msgMetaVo.getFieldName(), str);
                }

                startPos += msgMetaVo.getColLength();
            }

            if ( bodyLen > 0 ) {
                byte[] bodyBuffer = new byte[bodyLen];
                int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

                dis.read(bodyBuffer);

                // 헤더 정보 열람
                msgMetaSearchVo = new MsgMetaSearchVo(msgId, 1);
                List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                startPos = 0;

                getMsgData(bodyMap, bodyBuffer, msgBodyInfo, startPos);
            }

            LOGGER.debug("End of Parse.");

            os.close();
            dos.close();
            is.close();
            dis.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] makeTestMsg(int msgId) {
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            Map headerMap = new HashMap();
            headerMap.put("MsgId", msgId);
            headerMap.put("ServiceId", 1214);
            headerMap.put("Version", "0.0.1");
            headerMap.put("VIN", "KMISTEST012100056");
            headerMap.put("NadId", "01043214321");
            headerMap.put("coordinate", "G");
            headerMap.put("lon", null);
            headerMap.put("lat", null);
            headerMap.put("alter", null);
            headerMap.put("ReqCompression", "O");
            headerMap.put("ReqEncryption", "O");
            headerMap.put("ReqFormat", "O");
            headerMap.put("RespCompression", "O");
            headerMap.put("RespEncryption", "O");
            headerMap.put("RespFormat", "J");
            headerMap.put("Country", 1);
            headerMap.put("Filler2", null);
            headerMap.put("BodyLen", 0);

            makeMsgByteStream(msgId, baos, msgHeaderInfo, headerMap);

            return baos.toByteArray();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] makeBody(int msgId) {
        ByteArrayOutputStream baos = null;
        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 0);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();

            Map bodyMap = new HashMap();

            if ( msgId == VPP_001 ) {
                bodyMap.put("reqTime", "20181013183000");
                bodyMap.put("lon", 45729593);
                bodyMap.put("lat", 13498484);
                bodyMap.put("parkingAreaID", "01");
            }
            else if ( msgId == VPP_002 ) {
                bodyMap.put("gatherStartDate", "20181015");
                bodyMap.put("gatherStartTime", "142000");
                bodyMap.put("lon", 45729593);
                bodyMap.put("lat", 13498484);
                bodyMap.put("heading", "A");
                bodyMap.put("objStatic", "0");
                bodyMap.put("objDynamic", "0");
            }
            else if (msgId == VPP_003 ) {
                bodyMap.put("drivingStatus", "00");
                bodyMap.put("doorOpen", "00");
                bodyMap.put("engineStatus", "00");
                bodyMap.put("transmission", "P");
                bodyMap.put("velocity", 60000);
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
                try {
                    LOGGER.debug(metaMap.get(msgMetaVo.getFieldName()).toString());
                }catch (Exception e){
                    LOGGER.debug("NULL!");
                }

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
                        }
                        break;
                    case "byte":
                    case "char":
                        if (colLength == 0) {
                            // 가변 길이 처리
                            if ( metaMap.get(msgMetaVo.getFieldName()) != null ) {
                                colLength = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes().length;
                            }

                            LOGGER.warn(msgMetaVo.getFieldName());
                            LOGGER.warn(String.valueOf(colLength));
                        }

                        if (metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O")) {
                            temp = new byte[colLength];
                        }
                        else {
                            fieldValue = metaMap.get(msgMetaVo.getFieldName()).toString();
                            temp = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes();

                            if (fieldValue.length() < colLength) {
                                appendNullFlag = true;
                            }
                        }
                        break;
                }

                baos.write(temp);

                if (appendNullFlag) {
                    baos.write(new byte[colLength - fieldValue.length()]);
                    appendNullFlag = false;
                }
            }
            else {
                byte[] body = makeBody(msgId);

                LOGGER.info("BodyLen");
                LOGGER.debug(body.length);

                baos.write(ByteBuffer.allocate(4).putInt(body.length).array());
                baos.write(body);
            }
        }
    }

    private static void getMsgData(Map infoMap, byte[] buffer, List<MsgMetaVo> msgHeaderInfo, int startPos) {
        int variableColLength = 0;
        for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
            if ( msgMetaVo.getColLength() == 0 ) {
                msgMetaVo.setColLength(variableColLength);
                variableColLength = 0;
            }

            byte[] colValue = new byte[msgMetaVo.getColLength()];

            System.arraycopy(buffer, startPos, colValue, 0, msgMetaVo.getColLength());

            if (msgMetaVo.getColType().equals("u_int")) {
                BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                LOGGER.info(msgMetaVo.getFieldName() + " | " + k);

                infoMap.put(msgMetaVo.getFieldName(), k);
            }
            else if ( msgMetaVo.getColType().equals("short_int")) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(2);
                byteBuffer.put(colValue);

                Short s = byteBuffer.getShort(0);

                LOGGER.info(msgMetaVo.getFieldName() + " | " + s);

                // 다음 필드의 길이를 나타내는 경우 사용.
                if ( msgMetaVo.getColWorkType().equals("L")) {
                    variableColLength = s;
                }
            }
            else if (msgMetaVo.getColType().equals("byte")) {
                String str = new String(colValue, StandardCharsets.UTF_8);

                LOGGER.info(msgMetaVo.getFieldName() + " | " + str);

                infoMap.put(msgMetaVo.getFieldName(), str);
            }
            else if (msgMetaVo.getColType().equals("char")) {
                String str = new String(colValue, StandardCharsets.UTF_8);

                LOGGER.info(msgMetaVo.getFieldName() + " | " + str);

                infoMap.put(msgMetaVo.getFieldName(), str);
            }

            startPos += msgMetaVo.getColLength();

            if ( msgMetaVo.getColWorkType().equals("A")) {
                if ( msgMetaVo.getMsgId() == VPP_001) {
                    if ( msgMetaVo.getSeq() == 3) {
                        readList(buffer, msgMetaVo.getMsgId(), 11, startPos,
                                Integer.parseInt(infoMap.get(msgMetaVo.getFieldName()).toString()), infoMap);
                    }
                }
            }
        }
    }

    private static void readList(byte[] buffer, int msgId, int msgType, int startPos, int listSize, Map infoMap) {
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, msgType);
        List<MsgMetaVo> msgMetaInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        List<Map> list = new ArrayList<>();

        for ( int i = 0; i < listSize; i++ ) {

            LOGGER.info("@@@@@ Parsing One of List @@@@@");

            Map listInfoMap = new HashMap();
            int variableColLength = 0;

            List<MsgMetaVo> msgMetaInfoTemp = new ArrayList<>(msgMetaInfo);

            for ( MsgMetaVo msgMetaVo : msgMetaInfoTemp ) {

                if (msgMetaVo.getColLength() == 0) {
                    msgMetaVo.setColLength(variableColLength);
//                    LOGGER.debug(variableColLength);
                }

                byte[] colValue = new byte[msgMetaVo.getColLength()];

                System.arraycopy(buffer, startPos, colValue, 0, msgMetaVo.getColLength());

                switch (msgMetaVo.getColType()) {
                    case "u_int":
                        BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                        LOGGER.info(msgMetaVo.getFieldName() + " | " + k);

                        listInfoMap.put(msgMetaVo.getFieldName(), k);
                        break;
                    case "short_int":
                        int s = byteToShort(colValue);
                        LOGGER.info(msgMetaVo.getFieldName() + " | " + s);

                        // 다음 필드의 길이를 나타내는 경우 사용.
                        if (msgMetaVo.getColWorkType().equals("L")) {
                            variableColLength = s;
                        }
                        break;
                    case "byte":
                    case "char":
                        String str = new String(colValue, StandardCharsets.UTF_8).trim();

                        LOGGER.info(msgMetaVo.getFieldName() + " | " + str);

                        listInfoMap.put(msgMetaVo.getFieldName(), str);
                        break;
                }

                startPos += msgMetaVo.getColLength();

            }
            list.add(listInfoMap);
        }
        infoMap.put("list", list);
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

    public static int byteToShort(byte[] bytes) {
        int newValue = 0;
        newValue |= (((int)bytes[0])<<8)&0xFF00;
        newValue |= (((int)bytes[1]))&0xFF;

//        LOGGER.warn(newValue);

        return newValue;
    }

}
