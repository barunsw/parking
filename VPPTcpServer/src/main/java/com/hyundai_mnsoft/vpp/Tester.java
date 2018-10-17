package com.hyundai_mnsoft.vpp;

public class Tester {

    public static void main(String[] args) {
//        MsgSender msgSender = new MsgSender();

//        msgSender.sendMsgViaRMI(16781315);

        char[] a = {'1'};

        System.out.println(new String(a).getBytes().length);
    }

//    public static void main(String[] args) {
//
//        int a = 1214;
//        String hex = Integer.toHexString(a);
//
//        String ZEROES = "00000000";
//        String hex4byte = hex.length() <= 8 ? ZEROES.substring(hex.length()) + hex: hex;
//
//        System.out.println(hex4byte);
//
//        byte[] b = hexToByteArray(hex4byte);
//
//        System.out.println(b.length);
//
//        BigInteger k = new BigInteger(hex4byte, 16);
//
//        System.out.println(k);
//
////        byte[] bytes = ByteBuffer.allocate(4).putInt(16781313).array();
////
////        for (byte b : bytes) {
////            System.out.format("0x%x ", b);
////            System.out.println(b);
////        }
////
////        for (Byte b : bytes) {
////            int i = b.intValue();
////            System.out.println(i);
////        }
////
////        int j = convertByteToInt(bytes);
////
////        System.out.println(j);
//    }

    public static int convertByteToInt(byte[] b)
    {
        int value= 0;
        for(int i=0; i<b.length; i++)
            value = (value << 8) | b[i];
        return value;
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return ba;
    }
}
