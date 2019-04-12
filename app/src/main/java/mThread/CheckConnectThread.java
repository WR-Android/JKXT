package mThread;

import android.os.Message;

import java.util.Date;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

import static mThread.UDPReceiveThread.CurrentTime;
import static mThread.UDPReceiveThread.LastCheckConnectDate;

/**
 * Created by Administrator on 2019/4/11.
 */

public class CheckConnectThread extends Thread {

    @Override
    public void run() {
        //super.run();
        while (true)
            try {
                UDPThread checkThread = new UDPThread();
                checkThread.setData(DataInfo.StrToHexByte("E9_01_01_00_00_EB_0D_0A","_"));
                checkThread.start(); //发送连接测试命令
                Thread.sleep(2000);
                CurrentTime = new Date(System.currentTimeMillis());  //获取当前时间
                long difference = CurrentTime.getTime() - LastCheckConnectDate.getTime();   //计算响应时间 正常情况1900ms左右响应
                if (difference > 3000) {
                    DataInfo.ConnectionState = false; //超过3000ms未响应则判定为连接断开
                    Message msg = HomePageActivity.mHandler.obtainMessage();
                    msg.what = DataInfo.DISCONNECTED;
                    HomePageActivity.mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
