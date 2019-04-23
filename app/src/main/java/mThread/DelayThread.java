package mThread;

import java.net.DatagramPacket;
import java.net.InetAddress;

import wang.com.jkxttest.DataInfo;

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
}
