package com.hyundai_mnsoft.vpp.tcp.server;

import com.hyundai_mnsoft.vpp.rmi.TcpServerInterface;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// ### TCP Server 메인.
public class TcpServer {
    private static Logger LOGGER = Logger.getLogger(TcpServer.class);

    // 메시지 수신 처리의 threadPool의 count.
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

                    LOGGER.debug("\n>> Socket Accepted\n>>");

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

    // HTTP Server와 RMI 연동.
    private static void initRmi() {
        try {
            TcpServerInterface serverIf = new TcpServerImpl();

            int serverPort = Integer.parseInt(props.getProperty("httpserver.port"));

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

// 메시지 수신 ConnectionWrap
class ConnectionWrap implements Runnable {
    private static Logger LOGGER = Logger.getLogger(ConnectionWrap.class);

    private Socket socket = null;

    private MsgService msgService = new MsgService();

    ConnectionWrap(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            // 메시지 수신시의 작업.
            msgService.processMsg(socket);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
                LOGGER.debug("\n>> Socket Closed\n>>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}