package wang.com.jkxttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2019/6/4.
 */

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent intent0 = new Intent(context, HomePageActivity.class);
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent0);
        }
    }
}
