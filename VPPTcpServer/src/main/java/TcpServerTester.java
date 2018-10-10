import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class TcpServerTester {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {

        Socket socket = null;
//        BufferedInputStream bufferedInputStream = null;
//        BufferedOutputStream bufferedOutputStream = null;

        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            socket = new Socket("127.0.0.1", 30000);

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

//            byte[] sendData = new byte[10];
//            sendData[0] = (byte)0x30;
//            sendData[1] = (byte)0x30;
//            sendData[2] = (byte)0x30;
//            sendData[3] = (byte)0x30;
//            sendData[4] = (byte)0x30;
//            sendData[5] = (byte)0x00;
//            sendData[6] = (byte)0x00;
//            sendData[7] = (byte)0x00;
//            sendData[8] = (byte)0x30;
//            sendData[9] = (byte)0x00;

            byte[] sendData = makeTestMsg().getBytes();

            dos.write(sendData);
            dos.flush();

            byte[] receivedData = null;
            byte[] headerBuffer = new byte[84];

            dis.read(headerBuffer);

//            System.out.println(Arrays.toString(headerBuffer));
            String str = new String(headerBuffer, "UTF-8");
            System.out.println(str);

//            LOGGER.debug(str);
//
//
//            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            bw.write("456789");
//            bw.newLine();
//            bw.flush();
//
//            String received = "";
//            received = br.readLine();
//            System.out.println(received);

            os.close();
            dos.close();
            is.close();
            dis.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String makeTestMsg() {
        StringBuilder msgBuilder = new StringBuilder();

        msgBuilder.append("1678000000000000000000000000000000000000123456789012G00000000000000000000000000000000");

        return msgBuilder.toString();
    }
}
