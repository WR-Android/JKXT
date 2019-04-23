package wang.com.jkxttest;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Message;

/**
 * Created by Administrator on 2019/4/19.
 */

public class CountTimer extends CountDownTimer {
    private Context context;

    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）单位ms
     * 参数 countDownInterval    渐变时间（每次倒计1s）单位ms
     */
    public CountTimer(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 计时过程显示

    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        if (DataInfo.ScreenSaveChecked) {
            Message msg = Message.obtain();
            msg.what = DataInfo.SCREENSAVER;
            HomePageActivity.mHandler.sendMessage(msg);
        }
    }


}
