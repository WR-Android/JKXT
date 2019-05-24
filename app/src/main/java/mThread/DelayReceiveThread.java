package mThread;

import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

import static wang.com.jkxttest.CrashHandler.TAG;

/**
 * Created by Administrator on 2019/4/23.
 */

public class DelayReceiveThread extends Thread {
    public static DatagramSocket dSocket = null;
    byte[] dbuf = new byte[1024];

    public static long LastCheckDelayConnectDate = System.currentTimeMillis();

    @Override
    public void run() {
        DatagramPacket dReceivePacket = new DatagramPacket(dbuf, dbuf.length);
        try {
            dSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dSocket.setSoTimeout(1500);
            dSocket.receive(dReceivePacket);    //阻塞式接收
            LastCheckDelayConnectDate = System.currentTimeMillis();
            DataInfo.DelayConnectionState = true;
            check_delay_data(dbuf);
        } catch (Exception e) {
            Log.e(TAG, "Delay OffLine");
            //e.printStackTrace();
        }

    }

    private void check_delay_data(byte[] data) {
        //处理获得网络继电器返回的数据
        Message msg1 = HomePageActivity.mHandler.obtainMessage();
        //Logger.d(HexByteToStr(data,5,"_"));
        if ((byte) (data[1] & 0xFF) == (byte) 0x02 || (byte) (data[1] & 0xFF) == (byte) 0x02) {
            //设备状态获取返回值
            if ((byte) (data[3] & 0x01) == (byte) 0x01) {     //继电器1吸合状态
                DataInfo.PowerState = true;
                msg1.what = DataInfo.POWERCONNECT;
                HomePageActivity.mHandler.sendMessage(msg1);
            } else {
                DataInfo.PowerState = false;
                msg1.what = DataInfo.POWERDISCONNECT;
                HomePageActivity.mHandler.sendMessage(msg1);
            }
        } else if (data[1] == (byte) 0x0A){     //定时任务设置返回值
            if (data[3] == (byte) 0x00) {
                //定时任务设置成功
                DataInfo.TimingState = true;

                //Toast
            }
        }
    }
}
