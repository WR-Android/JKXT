package mThread;

import java.util.Calendar;

import wang.com.jkxttest.DataInfo;

import static mThread.DelayThread.sendTimingList;

/**
 * Created by Administrator on 2019/5/24.
 */

public class setTimingThread extends Thread {

    @Override
    public void run() {
        while (DataInfo.Thread_alive)
            while (!DataInfo.TimingState)
                try {
                    synTime();          //发送同步时间
                    sendTimingList();   //发送定时任务
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    /**
     * 同步时间（以下从第四个字节开始）
     * 时间：     2019年 2月 15日8:00: 00
     * 时间格式为   13  02  0F  08  00  00
     * 说明
     * 第1字节：年份-2000的值（例如：2019 第一个字节表示19）
     * 第3~7字节：月  日 时 分 秒
     * 发送消息：FF 09 06 13 02 0F 08 00 00 BB
     * 返回消息：FF 09 01 00 BB 				执行成功
     * 返回消息：FF 09 01 01 BB 				执行失败
     */
    public void synTime() {

        byte[] buf = new byte[10];
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 2000;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int i = 0;
        buf[i++] = (byte) 0xFF;
        buf[i++] = (byte) 0x09;
        buf[i++] = (byte) 0x06;
        //转换时间为byte发送
        buf[i++] = (byte) (year & 0xFF);
        buf[i++] = (byte) (month & 0xFF);
        buf[i++] = (byte) (day & 0xFF);
        buf[i++] = (byte) (hour & 0xFF);
        buf[i++] = (byte) (minute & 0xFF);
        buf[i++] = (byte) (second & 0xFF);
        buf[i] = (byte) 0xBB;

        //同步系统时间
        DelayThread delayThread = new DelayThread();
        delayThread.setDelaydata(buf);
        delayThread.start();
    }

}
