package mThread;

import android.os.Message;

import com.orhanobut.logger.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

import static wang.com.jkxttest.DataInfo.HexByteToStr;

/**
 * Created by Administrator on 2019/4/23.
 */

public class DelayReceiveThread extends Thread {
    public static DatagramSocket dSocket = null;
    byte[] dbuf = new byte[1024];

    @Override
    public void run() {
        DatagramPacket dReceivePacket = new DatagramPacket(dbuf, dbuf.length);
        try {
            dSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dSocket.receive(dReceivePacket);
            check_delay_data(dbuf);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void check_delay_data(byte[] data) {
        //处理获得网络继电器返回的数据
        Message msg1 = HomePageActivity.mHandler.obtainMessage();
        Message msg2 = HomePageActivity.mHandler.obtainMessage();
        Logger.d(HexByteToStr(data,5,"_"));
        if((byte)(data[3]&0x01) == (byte)0x01){     //继电器1吸合状态
            DataInfo.PowerState = true;
            msg1.what = DataInfo.POWERCONNECT;
            HomePageActivity.mHandler.sendMessage(msg1);
        }else {
            DataInfo.PowerState = false;
            msg1.what = DataInfo.POWERDISCONNECT;
            HomePageActivity.mHandler.sendMessage(msg1);
        }
        if((byte)((data[3]>>1)&0x01) == (byte)0x01){    //继电器2吸合状态
            DataInfo.MpowerState = true;
            msg2.what = DataInfo.MPOWERCONNECT;
            HomePageActivity.mHandler.sendMessage(msg2);
        }else {
            DataInfo.MpowerState = false;
            msg2.what = DataInfo.MPOWERDISCONNECT;
            HomePageActivity.mHandler.sendMessage(msg2);
        }
    }
}
