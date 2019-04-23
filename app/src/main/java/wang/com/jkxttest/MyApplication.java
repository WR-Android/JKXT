package wang.com.jkxttest;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2019/4/19.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
