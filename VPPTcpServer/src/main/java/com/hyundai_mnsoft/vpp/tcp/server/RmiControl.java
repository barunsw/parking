package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.DBServiceInterface;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;
import org.apache.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiControl {
	private static Logger LOGGER = Logger.getLogger(RmiControl.class);
	
	private static DBServiceInterface serverIf;

	static {
		new RmiControl();
	}
	
	public RmiControl() {
		try {
//			String serverHost = System.getProperty("server.host");
//			int serverPort = Integer.parseInt(System.getProperty("server.port"));
			
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 30001);
			
			serverIf = (DBServiceInterface)registry.lookup("SERVER");
		}
		catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}

	public static TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo tcpParkingLotReqVo) throws Exception {
		return serverIf.getParkingLotInfo(tcpParkingLotReqVo);
	}

}
