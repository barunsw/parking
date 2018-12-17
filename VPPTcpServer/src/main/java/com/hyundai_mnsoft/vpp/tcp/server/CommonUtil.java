package com.hyundai_mnsoft.vpp.tcp.server;

// ### 공통적으로 사용하는 유틸.
public class CommonUtil {
     public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;

        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }

    public static int byteArrayToShort(byte[] bytes) {

        int newValue = 0;
        newValue |= (((int)bytes[0])<<8)&0xFF00;
        newValue |= (((int)bytes[1]))&0xFF;

        return newValue;
    }
}
