package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.RemoteControlReqInfoVo;
import com.hyundai_mnsoft.vpp.vo.RemoteControlResInfoVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TcpServerInterface extends Remote {
    public int sendVpp002Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    public RemoteControlResInfoVo sendVpp004Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    public RemoteControlResInfoVo sendVpp005Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
}
