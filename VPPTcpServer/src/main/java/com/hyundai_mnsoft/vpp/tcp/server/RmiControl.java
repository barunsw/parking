package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.DBServiceInterface;
import com.hyundai_mnsoft.vpp.vo.ParkingLotInfoVo;
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

	public static ParkingLotInfoVo getParkingLotInfo(String areaId) throws Exception {
		return serverIf.getParkingLotInfo(areaId);
	}

}
