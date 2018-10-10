package com.hyundai_mnsoft.vpp.tcp;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;
import org.apache.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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

    public static String test(String msg) throws Exception {
        return serverIf.test(msg);
    }
}
