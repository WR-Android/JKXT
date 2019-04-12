package mThread;

import android.os.Bundle;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

/**
 * Created by Administrator on 2019/4/11.
 */

public class TimeThread extends Thread {

    @Override
    public void run() {
        super.run();
        while (true)
            try {
                Thread.sleep(1000);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());  //获取当前时间
                Message msg = HomePageActivity.mHandler.obtainMessage();    //打包时间到handler主界面更新
                msg.what = DataInfo.UPDATACURRENTTIME;
                Bundle bundle = new Bundle();
                bundle.putString("time", formatter.format(date));
                msg.setData(bundle);
                HomePageActivity.mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
