package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.DBServiceInterface;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotReqVo;
import com.hyundai_mnsoft.vpp.vo.TcpParkingLotResVo;
import org.apache.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// ### HTTP Server와 RMI로 연동.
public class RmiControl {
	private static Logger LOGGER = Logger.getLogger(RmiControl.class);
	
	private static DBServiceInterface serverIf;

	static {
		new RmiControl();
	}
	
	public RmiControl() {
		try {
			// VPP Server와 RMI 연결
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 30001);
			serverIf = (DBServiceInterface)registry.lookup("SERVER");
		}
		catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}

	// 주차장 정보 요청시 사용.
	public static TcpParkingLotResVo getParkingLotInfo(TcpParkingLotReqVo tcpParkingLotReqVo) throws Exception {
		try {
			return serverIf.getParkingLotInfo(tcpParkingLotReqVo);
		}
		catch(java.rmi.ConnectException e){
			new RmiControl();
			return serverIf.getParkingLotInfo(tcpParkingLotReqVo);
		}
	}
}
