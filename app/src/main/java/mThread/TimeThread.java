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
        while (DataInfo.Thread_alive)
            try {
                Thread.sleep(1000);
                SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat formatter_hour = new SimpleDateFormat("HH");
                SimpleDateFormat formatter_min = new SimpleDateFormat("mm");
                SimpleDateFormat formatter_week = new SimpleDateFormat("EEEE");
                Date date = new Date(System.currentTimeMillis());  //获取当前时间
                Message msg = HomePageActivity.mHandler.obtainMessage();    //打包时间到handler主界面更新
                msg.what = DataInfo.UPDATACURRENTTIME;
                Bundle bundle = new Bundle();
                bundle.putString("date", formatter_date.format(date));
                bundle.putString("hour", formatter_hour.format(date));
                bundle.putString("min", formatter_min.format(date));
                bundle.putString("week", formatter_week.format(date));
                msg.setData(bundle);
                HomePageActivity.mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
