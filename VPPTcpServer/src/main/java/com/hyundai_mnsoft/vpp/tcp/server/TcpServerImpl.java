package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;
import com.hyundai_mnsoft.vpp.vo.RemoteControlReqInfoVo;
import com.hyundai_mnsoft.vpp.vo.RemoteControlResInfoVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class TcpServerImpl extends UnicastRemoteObject implements TcpServerInterface {
    private static Logger LOGGER = Logger.getLogger(TcpServerImpl.class);

    private final int VPP_004 = 16781315;
    private final int VPP_005 = 16781316;
    private final int VPP_202 = 16781317;

    private MsgSender msgSender = new MsgSender();

    public TcpServerImpl() throws RemoteException {
    }

    @Override
    public int sendVpp002Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException {
        int result = Integer.parseInt(sendMsg(requestVo, remoteControlReqInfoVo, VPP_202).getErrCode());
        return result;
    }

    @Override
    public RemoteControlResInfoVo sendVpp004Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException {
        return sendMsg(requestVo, remoteControlReqInfoVo, VPP_004);
    }

    @Override
    public RemoteControlResInfoVo sendVpp005Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException {
        return sendMsg(requestVo, remoteControlReqInfoVo, VPP_005);
    }

    private RemoteControlResInfoVo sendMsg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo, int msgId) throws RemoteException {
        LOGGER.info("Hello");

        Map headerMap = makeHeaderMap(msgId, requestVo);
        Map bodyMap = makeBodyMap(remoteControlReqInfoVo);

        return msgSender.sendMsgViaRMI(msgId, headerMap, bodyMap);
    }

    private Map makeHeaderMap(int msgId, RequestVo requestVo) {
        Map headerMap = new HashMap();
        headerMap.put("MsgId", msgId);
        headerMap.put("ServiceId", requestVo.getServiceId());
        headerMap.put("Version", requestVo.getVersion());
        headerMap.put("VIN", requestVo.getVIN());
        headerMap.put("NadId", requestVo.getNadId());
        headerMap.put("coordinate", requestVo.getCoordinate());
        headerMap.put("lon", requestVo.getLon());
        headerMap.put("lat", requestVo.getLat());
        headerMap.put("alter", requestVo.getAlter());
        headerMap.put("ReqCompression", requestVo.getReqCompression());
        headerMap.put("ReqEncrytion", requestVo.getReqEncrption());
        headerMap.put("ReqFormat", requestVo.getReqFormat());
        headerMap.put("RespCompression", requestVo.getRespCompression());
        headerMap.put("RespEncrytion", requestVo.getRespEncrption());
        headerMap.put("RespFormat", requestVo.getRespFormat());
        headerMap.put("Country", requestVo.getCountry());
        headerMap.put("Filler2", null);

        LOGGER.debug(headerMap);

        return headerMap;
    }

    private Map makeBodyMap(RemoteControlReqInfoVo remoteControlReqInfoVo) {
        Map bodyMap = new HashMap();
        bodyMap.put("remoteCtl", remoteControlReqInfoVo.getRemoteCtl());
        bodyMap.put("Reserved", remoteControlReqInfoVo.getReserved());
        bodyMap.put("reqTime", remoteControlReqInfoVo.getReqTime());
        bodyMap.put("parkingLotID", remoteControlReqInfoVo.getParkingLotID());

        LOGGER.debug(bodyMap);

        return bodyMap;
    }
}
