package mThread;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.List;

import agreement.Models;
import wang.com.jkxttest.DataInfo;

import static wang.com.jkxttest.DataInfo.StrToHexByte;

/**
 * Created by Administrator on 2019/4/16.
 */

public class CheckInputThread extends Thread {
    @Override
    public void run() {
        super.run();
        while (true)
            try {
                Thread.sleep(1000);
                List<Models> check_list = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "check_signal").find(Models.class);
                if (!check_list.isEmpty()) {
                    UDPThread sendThread = new UDPThread();
                    sendThread.setData(StrToHexByte(check_list.get(0).getSend_data(), "_"));
                    sendThread.start();
                } else {
                    Logger.e("Not Found CheckSignal Agreement");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

