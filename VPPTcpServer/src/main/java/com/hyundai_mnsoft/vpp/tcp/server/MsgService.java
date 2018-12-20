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

// ### 메시지 송/수신 처리를 함.
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

            // Header 정보 열람
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int headerLength = 0;
            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
                headerLength += msgMetaVo.getColLength();
            }

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

                // Header 정보 열람
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

            // DB 작업
            if ( msgId == VPP_001 ) {

            }
            else if ( msgId == VPP_002 ) {
                // 차량 위치 정보 갱신 (DB)
                result = processVehicleTraceInfo(headerMap, bodyMap);
            }
            else if ( msgId == VPP_003 ) {
                // 차량 상태 정보 갱신 (DB)
                result = processVehicleStatusInfo(headerMap, bodyMap);
            }
            else {
                // 정의 되지 않은 메시지 유형
                headerMap.put("ErrCode", new BigInteger("0E000011", 16));
            }

            // 응답 메시지 생성 및 전송.
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

    // 메시지 buffer를 Meta정보에 맞추어 자르고 활용 가능한 데이터로 변환.
    public static void getMsgData(Map dataMap, byte[] headerBuffer, List<MsgMetaVo> msgHeaderInfo, int startPos) {
        int variableColLength = 0;
        for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
            if ( msgMetaVo.getColLength() == 0 ) {
                msgMetaVo.setColLength(variableColLength);
                variableColLength = 0;
            }

            byte[] colValue = new byte[msgMetaVo.getColLength()];

            System.arraycopy(headerBuffer, startPos, colValue, 0, msgMetaVo.getColLength());

            LOGGER.debug(msgMetaVo.getFieldName() + "(HEX)" + " | " + byteArrayToHex(colValue));

            switch (msgMetaVo.getColType()) {
                case "u_int":
                    BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);

                    LOGGER.debug(msgMetaVo.getFieldName() + " | " + k);

                    dataMap.put(msgMetaVo.getFieldName(), k);

                    if (msgMetaVo.getColWorkType().equals("L")) {
                        variableColLength = k.intValue();
                    }

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
                case "Binary":
                    dataMap.put(msgMetaVo.getFieldName(), colValue);

                    LOGGER.debug(msgMetaVo.getFieldName() + "(BYTE ARRAY) \t| " + colValue);
                    LOGGER.debug(msgMetaVo.getFieldName() + "(STRING) \t\t| " + new String(colValue, StandardCharsets.UTF_8));

                    break;
            }
            startPos += msgMetaVo.getColLength();
        }
    }

    // 응답 메시지 생성.
    public byte[] makeResponseMsg(int msgId, Map reqHeaderMap, Map reqBodyMap) {

        LOGGER.debug("Make Response Message.");

        int msgType = 1;
        // response message 발송 시 사용
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, msgType);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            // bodyMap을 채워주는 작업
            Map resBodyMap = new HashMap();

            if ( msgId == VPP_001 ) {
                // 주차장 정보
                // RMI를 통해 HTTP Server 에서 DB 조회 후 정보 담기.
                TcpParkingLotReqVo tcpParkingLotReqVo = new TcpParkingLotReqVo();
                tcpParkingLotReqVo.setLat(reqBodyMap.get("lat").toString());
                tcpParkingLotReqVo.setLon(reqBodyMap.get("lon").toString());
                tcpParkingLotReqVo.setReqTime(reqBodyMap.get("reqTime").toString());
                tcpParkingLotReqVo.setParkingLotID(reqBodyMap.get("parkingAreaID").toString());

                // RMI를 통해 HTTP Server 에서 정보를 받아옴.
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

            // Body를 먼저 만들고 Header 생성.
            byte[] msgBody = makeMsgBody(msgId, msgType, resBodyMap);
            // Body의 길이를 Header 정보에 추가.
            reqHeaderMap.put("BodyLen", msgBody.length);
            // Header 정보를 먼저 쓴 후에
            // Body 정보를 write 해줌.
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

        // 작업 후의 buffer를 전달.
        return baos.toByteArray();
    }

    // 메시지 Body를 생성함.
    private static byte[] makeMsgBody(int msgId, int msgType, Map bodyMap) {
        ByteArrayOutputStream baos = null;

        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, msgType);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();

            // 메시지 Body stream 생성.
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
        // byte, char 항목일 때에 길이에 맞추어 Null을 추가해야 하는지에 대한 Flag.
        boolean appendNullFlag = false;

        for (MsgMetaVo msgMetaVo : msgMetaInfo) {
            int colLength = msgMetaVo.getColLength();
            String fieldValue = "";
            LOGGER.debug(msgMetaVo.getFieldName());

            switch (msgMetaVo.getColType()) {
                case "u_int":
                    LOGGER.debug(metaMap.get(msgMetaVo.getFieldName()).toString());

                    // Header 정보의 FieldName이 Null이고 필수 항목이 아닐 때에
                    // 빈 byte만 생성함.
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

                    // Header 정보의 FieldName이 Null이고 필수 항목이 아닐 때에
                    // 빈 byte만 생성함.
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
                    // 항목 길이가 0이면 가변 길이임.
                    if (colLength == 0) {
                        // 가변 길이 처리
                        if ( metaMap.get(msgMetaVo.getFieldName()) != null ) {
                            colLength = metaMap.get(msgMetaVo.getFieldName()).toString().getBytes().length;
                        }
                    }

                    // 해당 항목의 값이 없을 때
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
                    // 해당 항목의 값이 있을 때
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

            // stream에 write.
            baos.write(temp);

            // appendNullFlag가 true 일 때에는 stream 뒷부분에 빈 byte를 필요 길이만큼 write 해준다.
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

    // 주차장 배열 정보 추가 시 사용.
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

                // 임시값
                tcpLaneInfoMap.put("carNo", "0");

                tcpLaneInfoMap.put("carInDate", tcpLaneInfoVo.getCarInDate());

                LOGGER.debug("@@@@@ Append One to List @@@@@");

                // 1개의 TcpLaneInfoVo를 stream에 기록함.
                makeMsgByteStream(baos, arrayMetaInfo, tcpLaneInfoMap);

                LOGGER.debug("@@@@@ End of List @@@@@");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // 차량 위치 정보 수신 시의 작업.
    private int processVehicleTraceInfo(Map headerMap, Map bodyMap) {
        int result = 0;

        try {
            // Vo에 값 지정.
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

            // DB에 insert/update.
            MsgDao.insertVehicleTraceInfo(vo);
        }
        catch(Exception e){
            e.printStackTrace();
            result = 1;
        }

        return result;
    }

    // 차량 상태 정보 수신 시의 작업.
    private int processVehicleStatusInfo(Map headerMap, Map bodyMap) {
        int result = 0;

        try {
            // Vo에 값 지정.
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

            vo.setRouteData((byte[]) bodyMap.get("routeData"));

            // DB에 insert/update.
            MsgDao.insertVehicleStatusInfo(vo);

            try {
                if ( bodyMap.get("routeData") != null ) {
                    MsgDao.updateRouteData(vo);
                }
            }
            catch(Exception e){
//                e.printStackTrace();
                LOGGER.debug("routeData is null");
            }


        }
        catch(Exception e){
            e.printStackTrace();
            result = 1;
        }

        return result;
    }
}
