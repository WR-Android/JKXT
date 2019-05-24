package mThread;

import wang.com.jkxttest.DataInfo;
import static wang.com.jkxttest.HomePageActivity.sendTimingList;

/**
 * Created by Administrator on 2019/5/24.
 */

public class setTimingThread extends Thread {

    @Override
    public void run() {
        while (DataInfo.Thread_alive)
            while (!DataInfo.TimingState)
                try {
                    sendTimingList();   //发送定时任务
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

}
