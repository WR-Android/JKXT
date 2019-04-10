package mThread;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;

import wang.com.jkxttest.DataInfo;

import static android.content.ContentValues.TAG;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

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
            Log.e(TAG, "===error UDPThread===");
            e.printStackTrace();
        }
    }

    public class UDPSendThread extends Thread {

        @Override
        public void run() {
            //while (true)
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            try {
                Thread.sleep(50);
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(DataInfo.server_ip), DataInfo.server_port);
                //Log.e(TAG, "===error ready==="+data);
                mSocket.send(sendPacket);
            } catch (Exception e) {
                Log.e(TAG, "===error send===");
                e.printStackTrace();
            }
        }

    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
