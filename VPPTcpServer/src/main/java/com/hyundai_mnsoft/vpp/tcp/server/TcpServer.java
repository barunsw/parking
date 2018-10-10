package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.impl.TcpServerImpl;
import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;
import com.hyundai_mnsoft.vpp.vo.MsgHeaderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private static final int THREAD_COUNT = 10;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    public static Properties props = new Properties();

    private static int port;

    public static void main(String[] args) {
        try{
            loadProperties();
            port = Integer.parseInt(props.getProperty("tcp.port"));

            initRmi();

            LOGGER.debug(">> Tcp Server Running...");

            ServerSocket serverSocket = new ServerSocket(port);

            LOGGER.debug(">> Port : " + port);

            try {
                while(true) {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(3000);

                    LOGGER.debug(">> Socket Accepted");

                    try{
                        threadPool.execute(new ConnectionWrap(socket));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    private static void loadProperties() throws Exception {
        String propFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "config.properties";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(propFile));
            props.load(br);
        }
        finally {
            if (br != null) {
                br.close();
            }
        }
    }

    private static void initRmi() {
        try {
            TcpServerInterface serverIf = new TcpServerImpl();

            int serverPort = Integer.parseInt(System.getProperty("httpserver.port"));

            System.setProperty("java.security.policy", "AllPermission.policy");
            Registry registry = LocateRegistry.createRegistry(serverPort);
            registry.rebind("TCP", serverIf);

            LOGGER.debug("RMI rebind");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ConnectionWrap implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private Socket socket = null;

    private MsgService msgService = new MsgService();

    ConnectionWrap(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            try {
                byte[] headerBuffer = new byte[84];

                dis.read(headerBuffer);

                // 헤더 정보 열람
                List<MsgHeaderVo> msgHeaderInfo = msgService.getMsgHeaderInfo();

                int startPos = 0;
                int destPos = 0;

                for ( MsgHeaderVo msgHeaderVo : msgHeaderInfo ) {
                    byte[] colValue = new byte[msgHeaderVo.getColLength()];

                    System.arraycopy(headerBuffer, startPos, colValue, destPos, msgHeaderVo.getColLength());

                    startPos += msgHeaderVo.getColLength();

                    String str = new String(colValue, "UTF-8");

                    LOGGER.debug("{} | {}", msgHeaderVo.getFieldName(), str);
                }

                LOGGER.debug("End of Parse.");

                LOGGER.debug("RMI Test");
                LOGGER.debug(RmiControl.getParkingLotInfo("ABCD01").toString());

                dos.write(headerBuffer);
                dos.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                socket.close();

                os.close();
                dos.close();
                is.close();
                dis.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}