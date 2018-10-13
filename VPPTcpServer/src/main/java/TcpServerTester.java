

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class TcpServerTester {

    private static Logger LOGGER = Logger.getLogger(TcpServerTester.class);

    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            socket = new Socket("127.0.0.1", 30000);

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] sendData = makeTestMsg();

            dos.write(sendData);
            dos.flush();

            byte[] receivedData = null;
            byte[] headerBuffer = new byte[8];

            dis.read(headerBuffer);

            String str = new String(headerBuffer, "UTF-8");
            System.out.println(str);

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

    public static String makeTestMsgStr() {
        StringBuilder msgBuilder = new StringBuilder();

        msgBuilder.append("1678000000000000000000000000000000000000123456789012G00000000000000000000000000000000");

        return msgBuilder.toString();
    }

    public static byte[] makeTestMsg() {
        byte[] msg = new byte[0];

        byte[] msgId = intToByteArray(4, 16781312);

        msg = append(msg, msgId);

        byte[] servicdId = intToByteArray(4, 1214);

//        if ( servicdId.length < colleng ) {
//            byte[] nullByte = new byte[colleng - servicdId.length];
//            servicdId = append(servicdId, nullByte);
//        }

        msg = append(msg, servicdId);

        LOGGER.info(String.valueOf(msg.length));

        return msg;
    }

    // byte 결합 시 사용
    public static final byte[] append(final byte[]... arrays) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (arrays != null) {
            for (final byte[] array : arrays) {
                if (array != null) {
                    out.write(array, 0, array.length);
                }
            }
        }
        return out.toByteArray();
    }

    public static byte[] intToByteArray(int length, int value) {
        int zeroCount = length * 2;
        String formatStr = "%" + String.format("%02x", zeroCount) + "x";

        String hexByteStr = String.format(formatStr, value); //0 채워진 hex 값으로 변환.

        byte[] ba = new byte[length];
        for (int i = 0; i < ba.length; i++) {
            LOGGER.info(i);
            ba[i] = (byte) Integer.parseInt(hexByteStr.substring(2 * i, 2 * i + 2), 16);
        }

        return ba;
    }
}
