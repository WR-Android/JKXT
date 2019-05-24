package mThread;

import android.os.Message;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

import static mThread.DelayReceiveThread.LastCheckDelayConnectDate;

/**
 * Created by Administrator on 2019/4/23.
 */

public class DelayConnectThread extends Thread{

    @Override
    public void run() {
        while (DataInfo.Thread_alive)
            try {
                DelayThread checkDelayThread = new DelayThread();
                checkDelayThread.setDelaydata(DataInfo.StrToHexByte("FF_02_00_BB","_"));
                checkDelayThread.start(); //发送连接测试命令
                Thread.sleep(2000);
                long CurrentTime = System.currentTimeMillis();  //获取当前时间
                long difference = CurrentTime - LastCheckDelayConnectDate;   //计算响应时间 正常情况1900ms左右响应
                if (difference > 4000) {
                    DataInfo.DelayConnectionState = false; //超过4000ms未响应则判定为连接断开
                    Message msg = HomePageActivity.mHandler.obtainMessage();
                    msg.what = DataInfo.POWEROFFLINE;
                    HomePageActivity.mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
