package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.RemoteControlReqInfoVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;
import com.hyundai_mnsoft.vpp.vo.TcpRemoteControlResInfoVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

// ### HTTP Server에서 TCP Server로 작업 (메시지 발송)을 요청하는 서비스의 Interface.
public interface TcpServerInterface extends Remote {
    // * 차량 원격시동 요청
    public int sendVpp202Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    // * 차량 출차 요청
    public TcpRemoteControlResInfoVo sendVpp004Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    // * 차량 주차 요청
    public TcpRemoteControlResInfoVo sendVpp005Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
}
