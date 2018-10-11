package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TcpServerImpl extends UnicastRemoteObject implements TcpServerInterface {

    public TcpServerImpl() throws RemoteException {
    }

    @Override
    public String test(String msg) {
        return msg + "ㅎㅎ";
    }

    @Override
    public int sendMsg(String msgId, byte[] data) throws RemoteException {

        return 0;
    }
}
