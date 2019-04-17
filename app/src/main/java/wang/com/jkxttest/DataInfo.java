package wang.com.jkxttest;

/**
 * Created by Administrator on 2019/4/9.
 */

public class DataInfo {

    //数据库的名称
    public final static String DB_NAME = "Agreement.db";
    //数据库的地址
    public final static String DB_PATH = "/data/data/wang.com.jkxttest/databases/";

    public static String input1;
    public static String input2;
    public static String input3;
    public static String input4;
    public static String input5;
    public static String input6;
    public static String last_input;

    public static String server_ip;
    public static int server_port;
    public static String model_number = "";
    public static boolean ConnectionState = false;
    public static int agreement_len = 8;
    public static final int UPDATACURRENTTIME = 1;
    public static final int NEWCONNECT = 2;
    public static final int DISCONNECTED = 3;
    public static final int INPUTCONNECTED = 4;


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
