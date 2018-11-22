package com.hyundai_mnsoft.vpp.rmi;

import com.hyundai_mnsoft.vpp.vo.RemoteControlReqInfoVo;
import com.hyundai_mnsoft.vpp.vo.RequestVo;
import com.hyundai_mnsoft.vpp.vo.TcpRemoteControlResInfoVo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TcpServerInterface extends Remote {
    public int sendVpp002Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    public TcpRemoteControlResInfoVo sendVpp004Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
    public TcpRemoteControlResInfoVo sendVpp005Msg(RequestVo requestVo, RemoteControlReqInfoVo remoteControlReqInfoVo) throws RemoteException;
}
