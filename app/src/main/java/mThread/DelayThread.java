package mThread;

import org.litepal.LitePal;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

import agreement.TimingLists;
import wang.com.jkxttest.DataInfo;

import static wang.com.jkxttest.DataInfo.StrToHexByte;

/**
 * Created by Administrator on 2019/4/23.
 */

public class DelayThread extends DelayReceiveThread {

    byte[] delay_data = new byte[1024];

    public DelayThread() {
        try {
            DelaySendThread delaySendThread = new DelaySendThread();
            delaySendThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class DelaySendThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(50);
                DatagramPacket delaySendPacket = new DatagramPacket(delay_data, delay_data.length, InetAddress.getByName(DataInfo.delay_ip), DataInfo.delay_port);
                dSocket.send(delaySendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDelaydata(byte[] delaydata) {
        this.delay_data = delaydata;
    }

    public static void sendTimingList() {
        //设置网络继电器周循环定时任务
        List<TimingLists> all = LitePal.findAll(TimingLists.class);
        byte[] TimingData = new byte[all.size() * 14 + 4];
        if (all.isEmpty()) {
            //定时任务为空
            DelayThread delayThread = new DelayThread();
            delayThread.setDelaydata(StrToHexByte("FF_0A_01_00_BB", "_"));
            delayThread.start();
            return;
        }
        int i = 0;
        TimingData[i++] = (byte) 0xFF;    //开始位
        TimingData[i++] = (byte) 0x0A;    //功能标志位
        TimingData[i++] = (byte) ((all.size() * 14) & 0xff); //指令长度
        for (TimingLists buf : all) {
            String[] open = buf.getOpentime().split(":");
            String[] close = buf.getClosetime().split(":");

            TimingData[i++] = (byte) (Integer.valueOf(open[0]) & 0xff); //开机时间：时
            TimingData[i++] = (byte) (Integer.valueOf(open[1]) & 0xff); //开机时间：分
            TimingData[i++] = (byte) 0x00;                                 //开机时间：秒
            TimingData[i++] = (byte) 0x01;//继电器控制位
            TimingData[i++] = (byte) 0x01;//设置开关继电器的状态
            TimingData[i++] = (byte) (buf.getWeek() & 0xff);//设置循环周期
            TimingData[i++] = (byte) 0x01;//定时功能开关

            TimingData[i++] = (byte) (Integer.valueOf(close[0]) & 0xff); //关机时间：时
            TimingData[i++] = (byte) (Integer.valueOf(close[1]) & 0xff); //关机时间：分
            TimingData[i++] = (byte) 0x00;                                  //关机时间：秒
            TimingData[i++] = (byte) 0x01;//继电器控制位
            TimingData[i++] = (byte) 0x00;//设置开关继电器的状态
            TimingData[i++] = (byte) (buf.getWeek() & 0xff);//设置循环周期
            TimingData[i++] = (byte) 0x01;//定时功能开关
        }
        TimingData[i] = (byte) 0xBB; //结束位

        //发送定时任务
        DelayThread delayThread = new DelayThread();
        delayThread.setDelaydata(TimingData);
        delayThread.start();
    }
}
