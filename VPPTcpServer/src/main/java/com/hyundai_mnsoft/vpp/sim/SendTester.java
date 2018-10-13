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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendTester {
    private static Logger LOGGER = Logger.getLogger(SendTester.class);
    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            socket = new Socket("127.0.0.1", 30000);

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] sendData = makeTestMsg(16781312);

            dos.write(sendData);
            dos.flush();

            byte[] headerBuffer = new byte[23];

            dis.read(headerBuffer);

            MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(0, 1);
            List<MsgMetaVo> msgHeaderInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

            int startPos = 0;
            int destPos = 0;

            Map headerMap = new HashMap();
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
        if (msgId == 16781312) {
            try {
                MsgMetaSearchVo msgMetaSearchVo = new MsgMetaSearchVo(msgId, 0);
                List<MsgMetaVo> msgBodyInfo = MsgDao.getMsgMetaInfo(msgMetaSearchVo);

                baos = new ByteArrayOutputStream();

                Map bodyMap = new HashMap();
                bodyMap.put("reqTime", "20181013183000");
                bodyMap.put("lon", 45729593);
                bodyMap.put("lat", 13498484);

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
