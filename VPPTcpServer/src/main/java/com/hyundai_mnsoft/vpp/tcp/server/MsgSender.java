package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import com.hyundai_mnsoft.vpp.vo.TcpRemoteControlResInfoVo;
import com.hyundai_mnsoft.vpp.vo.VehicleStatusInfoVo;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ### 메시지 발송 시 사용.
public class MsgSender extends CommonUtil {
    private static Logger LOGGER = Logger.getLogger(MsgSender.class);

    // 차량 연결 정보.
    private String vehicle_ip = TcpServer.props.getProperty("vehicle.ip");
    private int vehicle_port = Integer.parseInt(TcpServer.props.getProperty("vehicle.port"));

    // RMI를 통해 메시지를 발송 할 때에 호출됨.
    public TcpRemoteControlResInfoVo sendMsgViaRMI(int msgId, Map headerMap, Map bodyMap) {
        // 메시지 생성 후 send.
        byte[] msg = makeMsg(msgId, headerMap, bodyMap);
        return sendProcess(msg);
    }

    private byte[] makeMsg(int msgId, Map headerMap, Map bodyMap) {
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            // 메시지 Body를 먼저 만들고 Header 생성.
            byte[] msgBody = makeMsgBody(msgId, 0, bodyMap);
            // headerMap에 길이 정보 추가.
            headerMap.put("BodyLen", msgBody.length);
            MsgService.makeMsgByteStream(baos, msgHeaderInfo, headerMap);
            baos.write(msgBody);

        }
        catch (Exception e){
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

    // 메시지 Body 생성.
    private byte[] makeMsgBody(int msgId, int msgType, Map bodyMap) {
        ByteArrayOutputStream baos = null;
        try {
            // 메시지 Meta 정보 열람 후 List에 저장.
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, msgType);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();

            // 메시지 Meta 정보 토대로 메시지 Stream 생성.
            MsgService.makeMsgByteStream(baos, msgBodyInfo, bodyMap);
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

    private TcpRemoteControlResInfoVo sendProcess(byte[] msg) {
        TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo = new TcpRemoteControlResInfoVo();

        //Socket 통해서 메시지 발송 후 코드 return.
        Socket socket = null;

        try {
            LOGGER.debug("Establishing Connection..." + vehicle_ip + "/" + vehicle_port);
            SocketAddress socketAddress = new InetSocketAddress(vehicle_ip, vehicle_port);

            socket = new Socket();
            socket.setSoTimeout(5000);
            socket.connect(socketAddress, 3000);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("!!! Connection Failed !!!");
            tcpRemoteControlResInfoVo.setErrCode("1");
        }

        if ( socket != null ) {
            try {
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                LOGGER.debug("msg flush.");
                dos.write(msg);
                dos.flush();

                LOGGER.debug(msg.length);

                // 응답 메시지 Meta 정보
                MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
                List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                int headerLength = 0;
                for (MsgMetaVo msgMetaVo : msgHeaderInfo) {
                    headerLength += msgMetaVo.getColLength();
                }

                // Response의 Header
                byte[] headerBuffer = new byte[headerLength];

                dis.read(headerBuffer);

                int startPos = 0;
                int destPos = 0;

                Map headerMap = new HashMap();
                Map bodyMap = new HashMap();
                int bodyLen = 0;

                LOGGER.debug("\n>>> Receive Response Msg");

                for (MsgMetaVo msgMetaVo : msgHeaderInfo) {
                    byte[] colValue = new byte[msgMetaVo.getColLength()];

                    System.arraycopy(headerBuffer, startPos, colValue, destPos, msgMetaVo.getColLength());

                    switch (msgMetaVo.getColType()) {
                        case "u_int":
                            BigInteger k = new BigInteger(byteArrayToHex(colValue), 16);
                            LOGGER.info(msgMetaVo.getFieldName() + " | " + k);
                            headerMap.put(msgMetaVo.getFieldName(), k);

                            if (msgMetaVo.getFieldName().equals("BodyLen")) {
                                bodyLen = k.intValue();
                            }

                            break;
                        case "short_int":
                            int s = byteArrayToShort(colValue);
                            LOGGER.info(msgMetaVo.getFieldName() + " | " + s);
                            headerMap.put(msgMetaVo.getFieldName(), s);
                            break;
                        case "byte":
                        case "char":
                            String str = new String(colValue, StandardCharsets.UTF_8).trim();
                            LOGGER.info(msgMetaVo.getFieldName() + " | " + str);
                            headerMap.put(msgMetaVo.getFieldName(), str);
                            break;
                    }

                    startPos += msgMetaVo.getColLength();
                }

                // Body 부분이 존재할 경우 데이터 읽어옴.
                if (bodyLen > 0) {
                    byte[] bodyBuffer = new byte[bodyLen];
                    int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

                    dis.read(bodyBuffer);

                    msgMetaSearchVo = new MsgMetaSearchVo(msgId, 1);
                    List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                    startPos = 0;

                    MsgService.getMsgData(bodyMap, bodyBuffer, msgBodyInfo, startPos);
                }

                LOGGER.debug("End of Parse.");
                LOGGER.debug("\n>>> Response Msg Ends.");

                os.close();
                dos.close();
                is.close();
                dis.close();

                LOGGER.debug(headerMap.toString());
                LOGGER.debug(bodyMap.toString());

                tcpRemoteControlResInfoVo.setErrCode(headerMap.get("ErrCode").toString());

                try {
                    if (bodyMap.get("respTime") != null) {
                        tcpRemoteControlResInfoVo.setRespTime(bodyMap.get("respTime").toString());
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (bodyMap.get("routeData") != null) {
                        byte[] routeData_byteArr = (byte[]) bodyMap.get("routeData");
                        tcpRemoteControlResInfoVo.setRouteData(routeData_byteArr);

                        // routeData DB에 업데이트.
                        try {
                            VehicleStatusInfoVo vehicleStatusInfoVo = new VehicleStatusInfoVo();
                            // 가장 최근에 update된 ServiceId를 가져옴.
                            vehicleStatusInfoVo.setServiceId(MsgDao.getRecentServiceId());
                            vehicleStatusInfoVo.setRouteData(routeData_byteArr);

                            MsgDao.updateRouteData(vehicleStatusInfoVo);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ignored) {
                }

            } catch (Exception e) {
                e.printStackTrace();

                tcpRemoteControlResInfoVo.setErrCode("1");
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (Exception e) {
                    LOGGER.debug("Socket == null !");
                }
            }
        }

        return tcpRemoteControlResInfoVo;
    }
}
