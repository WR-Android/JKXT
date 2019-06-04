package wang.com.jkxttest;

/**
 * Created by Administrator on 2019/4/9.
 */

public class DataInfo {

    //数据库的名称
    public final static String DB_NAME = "Agreement.db";
    //数据库的地址
    public final static String DB_PATH = "/data/data/wang.com.jkxttest/databases/";
    public static boolean Thread_alive = true;

    public static String input1;
    public static String input2;
    public static String input3;
    public static String input4;
    public static String input5;
    public static String input6;

    public static String input1_Name;
    public static String input2_Name;
    public static String input3_Name;
    public static String input4_Name;
    public static String input5_Name;
    public static String input6_Name;

    public static String last_input;

    public static String server_ip;
    public static int server_port;
    public static String delay_ip;
    public static int delay_port = 6548;
    public static String model_number = "";
    public static boolean TimingState = false;
    //public static boolean synTimeState = false;
    public static boolean ConnectionState = false;
    public static boolean PowerState = false;
    public static boolean DelayConnectionState = false;
    public static int agreement_len = 8;
    public static String lock_pwd = "1234";
    public static String ScreenSaveTime = "60";
    public static boolean ScreenSaveChecked = false;
    public static final int UPDATACURRENTTIME = 1;
    public static final int NEWCONNECT = 2;
    public static final int DISCONNECTED = 3;
    public static final int INPUTCONNECTED = 4;
    public static final int SCREENSAVER = 5;
    public static final int POWERCONNECT = 6;
    public static final int POWERDISCONNECT = 7;
    public static final int POWEROFFLINE = 8;
    public static final int COMPLETEEDIT = 9;


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

    public static String WeekToString(char week) {
        String s = "";
        if (week == (char) 0x7F) {
            s = "每天";
        } else if (week == (char) 0x1F) {
            s = "工作日";
        } else if (week == (char) 0x60) {
            s = "周末";
        } else {
            s = "每周";
            if ((week & 0x01) != (byte) 0x00) {
                s += "一、";
            }
            if ((week & 0x01 << 1) != (byte) 0x00) {
                s += "二、";
            }
            if ((week & 0x01 << 2) != (byte) 0x00) {
                s += "三、";
            }
            if ((week & 0x01 << 3) != (byte) 0x00) {
                s += "四、";
            }
            if ((week & 0x01 << 4) != (byte) 0x00) {
                s += "五、";
            }
            if ((week & 0x01 << 5) != (byte) 0x00) {
                s += "六、";
            }
            if ((week & 0x01 << 6) != (byte) 0x00) {
                s += "日、";
            }
            // 删掉末尾的、
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String GetStandardTime(String time) {
        String[] time_split = time.split(":");
        int hour = Integer.parseInt(time_split[0]);
        int minute = Integer.parseInt(time_split[1]);
        StringBuilder sb = new StringBuilder();
//        if (hour<10){
//            sb.append("0"+hour);
//        }else {
//            sb.append(hour);
//        }
        sb.append(hour); //小时数 不补0
        sb.append(":");
        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append(minute);
        }
        return sb.toString();
    }

}
