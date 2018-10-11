package com.hyundai_mnsoft.vpp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TcpServerInterface extends Remote {
    public String test(String msg) throws RemoteException;
    public int sendMsg(String msgId, byte[] data) throws RemoteException;
}
