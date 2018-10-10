package com.hyundai_mnsoft.vpp.impl;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TcpServerImpl extends UnicastRemoteObject implements TcpServerInterface {

    public TcpServerImpl() throws RemoteException {

    }

    public String test(String msg) {
        return msg + "ㅎㅎ";
    }
}
