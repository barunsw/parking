package com.hyundai_mnsoft.vpp.tcp;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;
import com.hyundai_mnsoft.vpp.vo.RemoteControlReqInfoVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;
import com.hyundai_mnsoft.vpp.vo.TcpRemoteControlResInfoVo;
import org.apache.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// ### TCP Server와 RMI로 연동.
public class RmiControl {
    private static Logger LOGGER = Logger.getLogger(RmiControl.class);

    private static TcpServerInterface serverIf;

    static {
        new RmiControl();
    }

    public RmiControl() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 30002);

            serverIf = (TcpServerInterface)registry.lookup("TCP");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // 원격 시동 요청
    public static int sendVpp202Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws Exception {
        int result;
        try {
            result = serverIf.sendVpp202Msg(requestVo, remoteControlReqInfoVo);
        }
        catch(java.rmi.ConnectException e){
            // RMI 연결이 끊어지면 새로 연결을 수립하기 위함.
            new RmiControl();
            result = serverIf.sendVpp202Msg(requestVo, remoteControlReqInfoVo);
        }
        return result;
    }

    // 차량 출차 요청
    public static TcpRemoteControlResInfoVo sendVpp004Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws Exception {
        TcpRemoteControlResInfoVo result = null;
        try {
            result = serverIf.sendVpp004Msg(requestVo, remoteControlReqInfoVo);
        }
        catch(java.rmi.ConnectException e){
            new RmiControl();
            result = serverIf.sendVpp004Msg(requestVo, remoteControlReqInfoVo);
        }
        return result;
    }

    // 차량 주차 요청
    public static TcpRemoteControlResInfoVo sendVpp005Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws Exception {
        TcpRemoteControlResInfoVo result = null;
        try {
            result = serverIf.sendVpp005Msg(requestVo, remoteControlReqInfoVo);
        }
        catch(java.rmi.ConnectException e){
            new RmiControl();
            result = serverIf.sendVpp005Msg(requestVo, remoteControlReqInfoVo);
        }
        return result;
    }

}
