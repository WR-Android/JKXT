package mThread;

import com.orhanobut.logger.Logger;

import java.net.DatagramPacket;
import java.net.InetAddress;

import wang.com.jkxttest.DataInfo;

/**
 * Created by Administrator on 2019/4/9.
 */

public class UDPThread extends UDPReceiveThread {

    byte[] data = new byte[1024];

    public UDPThread() {
        try {
            UDPSendThread sendThread = new UDPSendThread();
            sendThread.start();

        } catch (Exception e) {
            Logger.e("===error UDPThread===");
            e.printStackTrace();
        }
    }

    public class UDPSendThread extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(50);
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(DataInfo.server_ip), DataInfo.server_port);
                mSocket.send(sendPacket);
            } catch (Exception e) {
                Logger.e("===error send===");
                e.printStackTrace();
            }
        }

    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
