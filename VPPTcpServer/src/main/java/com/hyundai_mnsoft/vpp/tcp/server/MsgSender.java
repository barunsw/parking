package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.tcp.dao.MsgDao;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import com.hyundai_mnsoft.vpp.vo.TcpRemoteControlResInfoVo;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgSender extends CommonUtil {
    private static Logger LOGGER = Logger.getLogger(MsgSender.class);

    private String vehicle_ip = TcpServer.props.getProperty("vehicle.ip");
    private int vehicle_port = Integer.parseInt(TcpServer.props.getProperty("vehicle.port"));

    public TcpRemoteControlResInfoVo sendMsgViaRMI(int msgId, Map headerMap, Map bodyMap) {
        byte[] msg = makeMsg(msgId, headerMap, bodyMap);
        return sendProcess(msg);
    }

    private byte[] makeMsg(int msgId, Map headerMap, Map bodyMap) {
        MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 0);
        List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            //Body를 먼저 만들고 헤더 생성.
            byte[] msgBody = makeMsgBody(msgId, 0, bodyMap);
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

    private byte[] makeMsgBody(int msgId, int msgType, Map bodyMap) {
        ByteArrayOutputStream baos = null;
        try {
            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, msgType);
            List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            baos = new ByteArrayOutputStream();

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
//        int result = 0;
        TcpRemoteControlResInfoVo tcpRemoteControlResInfoVo = new TcpRemoteControlResInfoVo();

        //Socket 통해서 메시지 발송 후 코드 return.
        Socket socket = null;

        try {
            socket = new Socket(vehicle_ip, vehicle_port);

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            dos.write(msg);
            dos.flush();

            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int headerLength = 0;
            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
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

            for ( MsgMetaVo msgMetaVo : msgHeaderInfo ) {
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

            if ( bodyLen > 0 ) {
                byte[] bodyBuffer = new byte[bodyLen];
                int msgId = Integer.parseInt(headerMap.get("MsgId").toString());

                dis.read(bodyBuffer);

                msgMetaSearchVo = new MsgMetaSearchVo(msgId, 1);
                List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                startPos = 0;

                MsgService.getMsgData(bodyMap, bodyBuffer, msgBodyInfo, startPos);
            }

            LOGGER.debug("End of Parse.");

            os.close();
            dos.close();
            is.close();
            dis.close();

            LOGGER.debug(headerMap.toString());
            LOGGER.debug(bodyMap.toString());

            tcpRemoteControlResInfoVo.setErrCode(headerMap.get("ErrCode").toString());
            if ( bodyMap.get("respTime") != null ) {
                tcpRemoteControlResInfoVo.setRespTime(bodyMap.get("respTime").toString());
            }
            if ( bodyMap.get("routeData") != null ) {
                tcpRemoteControlResInfoVo.setRouteData(bodyMap.get("routeData").toString());
            }

//            result = Integer.parseInt(headerMap.get("errCode").toString());

        } catch (IOException e) {
            e.printStackTrace();

            tcpRemoteControlResInfoVo.setErrCode("1");
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tcpRemoteControlResInfoVo;
    }
}
