package com.hyundai_mnsoft.vpp.sim;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class CarSimulator {
    private static Logger LOGGER = Logger.getLogger(CarSimulator.class);

    public static Properties props = new Properties();

    public static void main(String[] args) {
        try {
            loadProperties();
            int port = Integer.parseInt(props.getProperty("car.port"));

            LOGGER.debug(">> Car Sim Running...");

            ServerSocket serverSocket = new ServerSocket(port);

            LOGGER.debug(">> Port : " + port);

            try {
                while(true) {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(3000);

                    LOGGER.debug(">> Socket Accepted");

                    try{
                       // 차량의 로직







                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
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
}
