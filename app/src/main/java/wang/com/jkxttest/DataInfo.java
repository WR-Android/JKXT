package wang.com.jkxttest;

/**
 * Created by Administrator on 2019/4/9.
 */

public class DataInfo {

    public static String server_ip = "";
    public static int server_port = 20108;
    public static String model_number = "600";
    public static boolean ConnectionState = false;
    public static int agreement_lenth = 8;
//    public static final int RECEIVED_DATA = 1;


    //用separator分割的字符串 转换成byte数组
    public static byte[] StrToHexByte(String str, String separator) {
        if (".".equals(separator)) separator = "\\.";
        String[] split_str = str.split(separator);
        String chars = "0123456789ABCDEF";
        byte[] bytes = new byte[split_str.length];
        for (int i = 0; i < split_str.length; i++) {
            byte[] bit = split_str[i].getBytes();
            bytes[i] = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        }
        return bytes;
    }

    //byte数组转换成用separator分割的字符串
    public static String HexByteToStr(byte[] hex_data, int len, String separator) {
        String chars = "0123456789ABCDEF";
        String buf = "";
        for (int i = 0; i < len; i++) {
            buf += separator + chars.charAt(Integer.valueOf(hex_data[i] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(hex_data[i] & 0x0F));
        }
        buf = buf.substring(1);
        return buf;
    }

}
