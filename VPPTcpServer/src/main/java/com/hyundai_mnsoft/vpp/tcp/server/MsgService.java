package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgService extends CommonUtil{
    private static Logger LOGGER = Logger.getLogger(MsgService.class);

    private static final int VPP_001 = 16781312;
    private static final int VPP_002 = 16781313;
    private static final int VPP_003 = 16781314;

    // 메시지 수신시 사용.
    public int processMsg(Socket socket) {
        Map headerMap = new HashMap<>();
        Map bodyMap = new HashMap();
        int bodyLen = 0;

        int result = 0;

        InputStream is = null;
        DataInputStream dis = null;

        OutputStream os = null;
        DataOutputStream dos = null;

        try {
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            // 헤더 정보 열람
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int headerLength = 0;
            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
                headerLength += msgMetaVo.getColLength();
            }

//            LOGGER.debug(String.valueOf(headerLength));
            byte[] headerBuffer = new byte[headerLength];

            dis.read(headerBuffer);

            int startPos = 0;

            LOGGER.debug("@@@@@ Parsing Start @@@@@");

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

            LOGGER.debug("@@@@@ End of Parse @@@@@");

            LOGGER.debug(headerMap.toString());
            LOGGER.debug(bodyMap.toString());

            int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

            headerMap.put("ErrCode", new BigInteger("00000000", 16));

            //DB 작업
            if ( msgId == VPP_001 ) {

            }
            else if ( msgId == VPP_002 ) {
                result = processVehicleTraceInfo(headerMap, bodyMap);
            }
            else if ( msgId == VPP_003 ) {
                result = processVehicleStatusInfo(headerMap, bodyMap);
            }
            else {
                // 정의 되지 않은 메시지 유형
                headerMap.put("ErrCode", new BigInteger("0E000011", 16));
            }

            byte[] resMsg = makeResponseMsg(msgId, headerMap, bodyMap);

            dos.write(resMsg);
            dos.flush();

            LOGGER.debug("Response Sent.");
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
        return result;
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

            LOGGER.debug(bytesToHex(colValue));

            switch (msgMetaVo.getColType()) {
                case "u_int":
                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + k);

                    dataMap.put(msgMetaVo.getFieldName(), k);
                    break;
                case "short_int":
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.put(colValue);

                    Short s = byteBuffer.getShort();

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + s);

                    if (msgMetaVo.getColWorkType().equals("L")) {
                        variableColLength = s;
                    }
                    break;
                case "byte":
                case "char":
                    String str = new String(colValue, StandardCharsets.UTF_8);

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + str.trim());

                    dataMap.put(msgMetaVo.getFieldName(), str.trim());
                    break;
            }
            startPos += msgMetaVo.getColLength();
        }
    }

    public byte[] makeResponseMsg(int msgId, Map reqHeaderMap, Map reqBodyMap) {

        LOGGER.debug("Make Response Message.");

        int msgType = 1;
        // response message 발송 시 사용
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, msgType);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            //bodyMap을 채워주는 작업
            Map resBodyMap = new HashMap();

            if ( msgId == VPP_001 ) {
                // 주차장 정보
                // DB 조회 후 정보 담기.
                TcpParkingLotReqVo tcpParkingLotReqVo = new TcpParkingLotReqVo();
                tcpParkingLotReqVo.setLat(reqBodyMap.get("lat").toString());
                tcpParkingLotReqVo.setLon(reqBodyMap.get("lon").toString());
                tcpParkingLotReqVo.setReqTime(reqBodyMap.get("reqTime").toString());
                tcpParkingLotReqVo.setParkingLotID(reqBodyMap.get("parkingAreaID").toString());

                TcpParkingLotResVo tcpParkingLotResVo = RmiControl.getParkingLotInfo(tcpParkingLotReqVo);

                LOGGER.debug("tcpParkingLotResVo\n" + tcpParkingLotResVo.toString());

                resBodyMap.put("parkingLotNo", tcpParkingLotResVo.getParkingLotNo());
                resBodyMap.put("parkingLotNM", tcpParkingLotResVo.getParkingLotNm());
                resBodyMap.put("parkingLotNMLen", tcpParkingLotResVo.getParkingLotNm().getBytes().length);
                resBodyMap.put("totalListCnt", tcpParkingLotResVo.getLaneInfoList().size());
                resBodyMap.put("list", tcpParkingLotResVo.getLaneInfoList());

            }
            else if ( msgId == VPP_002 ) {
                // 차량 위치 정보 제공
                // bodyMap 작업 불필요
            }
            else if ( msgId == VPP_003 ) {
                // 차량 상태 제공
                // bodyMap 작업 불필요
            }

            //Body를 먼저 만들고 헤더 생성.
            byte[] msgBody = makeMsgBody(msgId, msgType, resBodyMap);
            reqHeaderMap.put("BodyLen", msgBody.length);
            makeMsgByteStream(baos, msgHeaderInfo, reqHeaderMap);
            baos.write(msgBody);
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

    private static byte[] makeMsgBody(int msgId, int msgType, Map bodyMap) {
        ByteArrayOutputStream baos = null;

        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, msgType);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();

            makeMsgByteStream(baos, msgBodyInfo, bodyMap);
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

    public static void makeMsgByteStream(ByteArrayOutputStream baos, List<MsgMetaVo> msgMetaInfo, Map metaMap) throws IOException {
        byte[] temp = new byte[0];
        boolean appendNullFlag = false;

        for (MsgMetaVo msgMetaVo : msgMetaInfo) {
            int colLength = msgMetaVo.getColLength();
            String fieldValue = "";
            LOGGER.debug(msgMetaVo.getFieldName());

            switch (msgMetaVo.getColType()) {
                case "u_int":
                    LOGGER.debug(metaMap.get(msgMetaVo.getFieldName()).toString());

                    if (metaMap.get(msgMetaVo.getFieldName()) == null && msgMetaVo.getColReqType().equals("O")) {
                        temp = new byte[colLength];
                    }
                    else {
                        temp = ByteBuffer.allocate(4).putInt(
                                Integer.parseInt(metaMap.get(msgMetaVo.getFieldName()).toString())).array();
                    }
                    break;
                case "short_int":
                    LOGGER.debug(metaMap.get(msgMetaVo.getFieldName()).toString());

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
                    }

                    if (metaMap.get(msgMetaVo.getFieldName()) == null) {
                        LOGGER.debug("NULL (" + msgMetaVo.getFieldName() + ")");

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

                        LOGGER.debug(metaMap.get(msgMetaVo.getFieldName()).toString());
                    }
                    break;
            }

            baos.write(temp);

            if (appendNullFlag) {
                baos.write(new byte[colLength - fieldValue.getBytes().length]);
                appendNullFlag = false;
            }

            // 배열일 경우에 실행.
            if ( msgMetaVo.getColWorkType().equals("A")) {
                if ( msgMetaVo.getMsgId() == VPP_001 ) {
                    if ( msgMetaVo.getSeq() == 3 ) {
                        appendTcpLaneInfoList(msgMetaVo.getMsgId(), baos, metaMap);
                    }
                }
            }
        }
    }

    private static void appendTcpLaneInfoList(int msgId, ByteArrayOutputStream baos, Map bodyMap) {
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 11);
        List<MsgMetaVo> arrayMetaInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        List<TcpLaneInfoVo> list = (List<TcpLaneInfoVo>) bodyMap.get("list");

        try {
            for (TcpLaneInfoVo tcpLaneInfoVo : list) {

                LOGGER.debug(tcpLaneInfoVo.toString());

                Map tcpLaneInfoMap = new HashMap();
                tcpLaneInfoMap.put("laneCode", tcpLaneInfoVo.getLaneCode());
                tcpLaneInfoMap.put("parkingLotNo", tcpLaneInfoVo.getParkingLotNo());
                tcpLaneInfoMap.put("parkingLevelCode", tcpLaneInfoVo.getParkingLevelCode());
                tcpLaneInfoMap.put("parkingZoneCode", tcpLaneInfoVo.getParkingZoneCode());
                tcpLaneInfoMap.put("laneSeqNum", tcpLaneInfoVo.getLaneSeqNum());
                tcpLaneInfoMap.put("laneNameLen", tcpLaneInfoVo.getLaneName().getBytes().length);
                tcpLaneInfoMap.put("laneName", tcpLaneInfoVo.getLaneName());
                tcpLaneInfoMap.put("laneType", tcpLaneInfoVo.getLaneType());
                tcpLaneInfoMap.put("manageType", tcpLaneInfoVo.getManageType());
                tcpLaneInfoMap.put("laneStatus", tcpLaneInfoVo.getLaneStatus());
                tcpLaneInfoMap.put("sectionId", tcpLaneInfoVo.getSectionId());
                tcpLaneInfoMap.put("slotId", tcpLaneInfoVo.getSlotId());
                tcpLaneInfoMap.put("carStatus", tcpLaneInfoVo.getCarStatus());

//                tcpLaneInfoMap.put("carNo", tcpLaneInfoVo.getCarNo());
                tcpLaneInfoMap.put("carNo", "0");

                tcpLaneInfoMap.put("carInDate", tcpLaneInfoVo.getCarInDate());

                LOGGER.debug("@@@@@ Append One to List @@@@@");
                makeMsgByteStream(baos, arrayMetaInfo, tcpLaneInfoMap);

                LOGGER.debug("@@@@@ End of List @@@@@");
//                LOGGER.debug(tcpLaneInfoMap.toString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private int processVehicleTraceInfo(Map headerMap, Map bodyMap) {
        int result = 0;

        try {
            VehicleTraceInfoVo vo = new VehicleTraceInfoVo();

            vo.setServiceId(headerMap.get("ServiceId").toString());
            vo.setVersion(headerMap.get("Version").toString());
            vo.setVIN(headerMap.get("VIN").toString());
            vo.setNadId(headerMap.get("NadId").toString());

            // 고정값
            vo.setMoId("01012341234");

            vo.setGatherStartDate(bodyMap.get("gatherStartDate").toString());
            vo.setGatherStartTime(bodyMap.get("gatherStartTime").toString());
            vo.setLon(bodyMap.get("lon").toString());
            vo.setLat(bodyMap.get("lat").toString());
            vo.setHeading(bodyMap.get("heading").toString());
            vo.setObjStatic(bodyMap.get("objStatic").toString());
            vo.setObjDynamic(bodyMap.get("objDynamic").toString());

            MsgDao.insertVehicleTraceInfo(vo);
        }
        catch(Exception e){
            e.printStackTrace();
            result = 1;
        }

        return result;
    }

    private int processVehicleStatusInfo(Map headerMap, Map bodyMap) {
        int result = 0;

        try {
            VehicleStatusInfoVo vo = new VehicleStatusInfoVo();

            vo.setServiceId(headerMap.get("ServiceId").toString());
            vo.setVersion(headerMap.get("Version").toString());
            vo.setVIN(headerMap.get("VIN").toString());
            vo.setNadId(headerMap.get("NadId").toString());

            // 고정값
            vo.setMoId("01012341234");

            vo.setDrivingStatus(bodyMap.get("drivingStatus").toString());
            vo.setDoorOpen(bodyMap.get("doorOpen").toString());
            vo.setEngineStatus(bodyMap.get("engineStatus").toString());
            vo.setTransmission(bodyMap.get("transmission").toString());
            vo.setVelocity(bodyMap.get("velocity").toString());
            vo.setSteering(bodyMap.get("steering").toString());
            vo.setControl(bodyMap.get("control").toString());

//            vo.setSteering(bodyMap.get("").toString());
//            vo.setControl(bodyMap.get("").toString());
//            vo.setUseType(bodyMap.get("").toString());
//            vo.setInOut(bodyMap.get("").toString());

            MsgDao.insertVehicleStatusInfo(vo);
        }
        catch(Exception e){
            e.printStackTrace();
            result = 1;
        }

        return result;
    }
}
