package wang.com.jkxttest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yf_a64_api.YF_A64_API_Manager;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import agreement.Models;
import mFragment.AboutsDialog;
import mFragment.LockDialog;
import mFragment.LockSettingDialog;
import mFragment.ScreenSaveDialog;
import mFragment.WarningDialog;
import mThread.CheckConnectThread;
import mThread.CheckInputThread;
import mThread.DelayConnectThread;
import mThread.TimeThread;
import mThread.UDPThread;
import mThread.setTimingThread;
import mView.inputButton;

import static agreement.CreateDataBase.create_database;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    public static CountTimer countTimerView;
    public static Handler mHandler;
    private inputButton btn_1;
    private inputButton btn_2;
    private inputButton btn_3;
    private inputButton btn_4;
    private inputButton btn_5;
    private inputButton btn_6;
    private ImageView img_connect_state;
    private ImageButton btn_setting;
    private ImageButton btn_lock;
    private ImageButton btn_abouts;
    private ImageButton btn_sleep;

    private Button cover; //休眠覆盖全屏按钮

    private RangeSeekBar sound_seekbar;
    private TextView tv_sound;
    private TextView tv_date;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_week;

    private ImageButton btn_power;

    private Button btn_m1;
    private Button btn_m2;
    private Button btn_m3;
    private Button btn_m4;
    private Button btn_m5;


    private YF_A64_API_Manager yfapi;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataInfo.Thread_alive = true;   //线程开启标记
        Logger.init().hideThreadInfo();
        yfapi = new YF_A64_API_Manager(this);
        yfapi.yfsetNavigationBarVisibility(false);  //关闭导航栏
        setContentView(R.layout.homepage);
        try {
            if (!(new File(DataInfo.DB_PATH + DataInfo.DB_NAME).exists())) {    //判断数据库文件是否已存在
                Logger.d("===创建数据库===");
                create_database();      //初始化数据库
            } else {
                Logger.d("===数据库已存在 没有创建数据库===");
            }
        } catch (Exception e) {
            Logger.e("DB File Error");
            e.printStackTrace();
        }
        initView();     //初始化控件
        initSP();   //初始化提取保存的ip,port,pwd,model_number
        initModeView();
        countTimerView = new CountTimer(Long.parseLong(DataInfo.ScreenSaveTime) * 1000, 1000, this); //打开计时器用于计算屏保倒计时
        new TimeThread().start();           //开时钟线程
        new CheckConnectThread().start();   //开设备连接线程
        new DelayConnectThread().start();   //开网络继电器连接检测线程
        new CheckInputThread().start();     //开input端口检测线程
        new setTimingThread().start();      //开定时任务发送线程
        update_input_view();//加载默认主界面布局
        InitInputName();    //初始化端口名称
        DataInfo.last_input = DataInfo.input1;
        initTime();//初始化获取当前时间日期

        cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        mGestureDetector = new GestureDetector(HomePageActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                cover.setVisibility(View.GONE);
                countTimerView.start(); //休眠结束，开启定时
                yfapi.yfSetLCDOn();
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {//长按事件
                super.onLongPress(e);
            }
        });

        sound_seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {  //声音滑块数值监听
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                tv_sound.setText("" + String.valueOf(Float.valueOf(leftValue).intValue()) + "%");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
    }

    private void initTime() {
        SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat formatter_hour = new SimpleDateFormat("HH");
        SimpleDateFormat formatter_min = new SimpleDateFormat("mm");
        SimpleDateFormat formatter_week = new SimpleDateFormat("EEEE");
        Date date = new Date(System.currentTimeMillis());  //获取当前时间
        tv_date.setText(formatter_date.format(date));
        tv_hour.setText(formatter_hour.format(date));
        tv_min.setText(formatter_min.format(date));
        tv_week.setText(formatter_week.format(date));
    }

    private void update_input_view() {
        //找到当前设备的端口信息
        List<Models> input_list = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "input").find(Models.class);
        int res_id;
        if (!input_list.isEmpty()) {

            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            sp.edit().putString("model_number", DataInfo.model_number).apply();

            DataInfo.input1 = input_list.get(0).getAction_name();
            DataInfo.input2 = input_list.get(1).getAction_name();
            DataInfo.input3 = input_list.get(2).getAction_name();
            DataInfo.input4 = input_list.get(3).getAction_name();
            DataInfo.input5 = input_list.get(4).getAction_name();
            DataInfo.input6 = input_list.get(5).getAction_name();

            UpdateInputName();

            btn_1.setBackgroundResource(R.drawable.input_button);
            btn_2.setBackgroundResource(R.drawable.input_button);
            btn_3.setBackgroundResource(R.drawable.input_button);
            btn_4.setBackgroundResource(R.drawable.input_button);
            btn_5.setBackgroundResource(R.drawable.input_button);
            btn_6.setBackgroundResource(R.drawable.input_button);

            res_id = getResources().getIdentifier(DataInfo.input1.toLowerCase(), "drawable", getPackageName());
            btn_1.setImageResource(res_id);
            btn_1.setImgPadding(5);
            res_id = getResources().getIdentifier(DataInfo.input2.toLowerCase(), "drawable", getPackageName());
            btn_2.setImageResource(res_id);
            btn_2.setImgPadding(5);
            res_id = getResources().getIdentifier(DataInfo.input3.toLowerCase(), "drawable", getPackageName());
            btn_3.setImageResource(res_id);
            btn_3.setImgPadding(5);
            res_id = getResources().getIdentifier(DataInfo.input4.toLowerCase(), "drawable", getPackageName());
            btn_4.setImageResource(res_id);
            btn_4.setImgPadding(5);
            res_id = getResources().getIdentifier(DataInfo.input5.toLowerCase(), "drawable", getPackageName());
            btn_5.setImageResource(res_id);
            btn_5.setImgPadding(5);
            res_id = getResources().getIdentifier(DataInfo.input6.toLowerCase(), "drawable", getPackageName());
            btn_6.setImageResource(res_id);
            btn_6.setImgPadding(5);
        }
    }

    private void UpdateInputName() {

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        if (DataInfo.NewModel) {
            btn_1.setText(DataInfo.input1);
            btn_2.setText(DataInfo.input2);
            btn_3.setText(DataInfo.input3);
            btn_4.setText(DataInfo.input4);
            btn_5.setText(DataInfo.input5);
            btn_6.setText(DataInfo.input6);
            DataInfo.input1_Name = DataInfo.input1;
            DataInfo.input2_Name = DataInfo.input2;
            DataInfo.input3_Name = DataInfo.input3;
            DataInfo.input4_Name = DataInfo.input4;
            DataInfo.input5_Name = DataInfo.input5;
            DataInfo.input6_Name = DataInfo.input6;
            sp.edit().putString("input1_Name",DataInfo.input1).apply();
            sp.edit().putString("input2_Name",DataInfo.input2).apply();
            sp.edit().putString("input3_Name",DataInfo.input3).apply();
            sp.edit().putString("input4_Name",DataInfo.input4).apply();
            sp.edit().putString("input5_Name",DataInfo.input5).apply();
            sp.edit().putString("input6_Name",DataInfo.input6).apply();
            DataInfo.NewModel = false;
        } else {
            btn_1.setText(sp.getString("input1_Name", DataInfo.input1));
            btn_2.setText(sp.getString("input2_Name", DataInfo.input2));
            btn_3.setText(sp.getString("input3_Name", DataInfo.input3));
            btn_4.setText(sp.getString("input4_Name", DataInfo.input4));
            btn_5.setText(sp.getString("input5_Name", DataInfo.input5));
            btn_6.setText(sp.getString("input6_Name", DataInfo.input6));
        }

        // TODO: 2019/6/3 文字颜色跟随端口图案
        //btn_1.setTextColor(getResources().getIdentifier(DataInfo.input1.toLowerCase(),"colors",getPackageName()));
    }


    private void initView() {
        btn_power = findViewById(R.id.btn_power);   //大屏电源
        btn_setting = findViewById(R.id.btn_setting);
        btn_lock = findViewById(R.id.btn_lock);
        btn_abouts = findViewById(R.id.btn_abouts);
        btn_sleep = findViewById(R.id.btn_sleep);
        cover = findViewById(R.id.Cover);
        img_connect_state = findViewById(R.id.img_connect_state);
        tv_date = findViewById(R.id.tv_date);
        tv_hour = findViewById(R.id.tv_hour);
        tv_min = findViewById(R.id.tv_min);
        tv_week = findViewById(R.id.tv_week);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_m1 = findViewById(R.id.btn_mode1);
        btn_m2 = findViewById(R.id.btn_mode2);
        btn_m3 = findViewById(R.id.btn_mode3);
        btn_m4 = findViewById(R.id.btn_mode4);
        btn_m5 = findViewById(R.id.btn_mode5);
        sound_seekbar = findViewById(R.id.sound_seekbar);
        tv_sound = findViewById(R.id.tv_sound);
        btn_power.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_lock.setOnClickListener(this);
        btn_abouts.setOnClickListener(this);
        btn_sleep.setOnClickListener(this);
        TouchScaleAnim.attachView(btn_power);
        TouchScaleAnim.attachView(btn_setting);
        TouchScaleAnim.attachView(btn_lock);
        TouchScaleAnim.attachView(btn_abouts);
        TouchScaleAnim.attachView(btn_sleep);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_m1.setOnClickListener(this);
        btn_m2.setOnClickListener(this);
        btn_m3.setOnClickListener(this);
        btn_m4.setOnClickListener(this);
        btn_m5.setOnClickListener(this);
        MyHandler();
    }

    private void initModeView() {
        btn_m1.setBackgroundResource(R.drawable.input_button);
        btn_m2.setBackgroundResource(R.drawable.input_button);
        btn_m3.setBackgroundResource(R.drawable.input_button);
        btn_m4.setBackgroundResource(R.drawable.input_button);
        btn_m5.setBackgroundResource(R.drawable.input_button);
    }

    private void initSP() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getString("ip", null) != null) {
            DataInfo.server_ip = sp.getString("ip", "192.168.1.223");
        } else {
            DataInfo.server_ip = "192.168.1.223";
            sp.edit().putString("ip", "192.168.1.223").apply();
        }

        if (sp.getString("port", null) != null) {
            DataInfo.server_port = Integer.parseInt(sp.getString("port", "20108"));
        } else {
            DataInfo.server_port = 20108;
            sp.edit().putString("port", "20108").apply();
        }

        if (sp.getString("delay_ip", null) != null) {
            DataInfo.delay_ip = sp.getString("delay_ip", "192.168.1.245");
        } else {
            DataInfo.delay_ip = "192.168.1.245";
            sp.edit().putString("delay_ip", "192.168.1.245").apply();
        }

        if (sp.getString("pwd", null) != null) {
            DataInfo.lock_pwd = sp.getString("pwd", "1234");
        } else {
            DataInfo.lock_pwd = "1234";
            sp.edit().putString("pwd", "1234").apply();
        }

        if (sp.getString("ScreenSaveTime", null) != null) {
            DataInfo.ScreenSaveTime = sp.getString("ScreenSaveTime", "60");
        } else {
            DataInfo.ScreenSaveTime = "60";
            sp.edit().putString("ScreenSaveTime", "60").apply();
        }

        if (sp.getBoolean("ScreenSaveChecked", false)) {
            DataInfo.ScreenSaveChecked = sp.getBoolean("ScreenSaveChecked", false);
        } else {
            DataInfo.ScreenSaveChecked = false;
            sp.edit().putBoolean("ScreenSaveChecked", false).apply();
        }

        if (sp.getString("model_number", null) != null) {
            DataInfo.model_number = sp.getString("model_number", "600");
        } else {
            DataInfo.model_number = "600";
            sp.edit().putString("model_number", "600").apply();
        }

        if (sp.getBoolean("TimingState", false)) { //定时任务设置成功标志位
            DataInfo.TimingState = sp.getBoolean("TimingState", false);
        } else {
            DataInfo.TimingState = false;
            sp.edit().putBoolean("TimingState", false).apply();
        }
    }

    private void InitInputName() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getString("input1_Name", null) != null) {
            DataInfo.input1_Name = sp.getString("input1_Name", "HDMI");
        } else {
            DataInfo.input1_Name = DataInfo.input1;
            sp.edit().putString("input1_Name", DataInfo.input1_Name).apply();
        }

        if (sp.getString("input2_Name", null) != null) {
            DataInfo.input2_Name = sp.getString("input2_Name", "HDMI");
        } else {
            DataInfo.input2_Name = DataInfo.input2;
            sp.edit().putString("input2_Name", DataInfo.input2_Name).apply();
        }

        if (sp.getString("input3_Name", null) != null) {
            DataInfo.input3_Name = sp.getString("input3_Name", "HDMI");
        } else {
            DataInfo.input3_Name = DataInfo.input3;
            sp.edit().putString("input3_Name", DataInfo.input3_Name).apply();
        }

        if (sp.getString("input4_Name", null) != null) {
            DataInfo.input4_Name = sp.getString("input4_Name", "HDMI");
        } else {
            DataInfo.input4_Name = DataInfo.input4;
            sp.edit().putString("input4_Name", DataInfo.input4_Name).apply();
        }

        if (sp.getString("input5_Name", null) != null) {
            DataInfo.input5_Name = sp.getString("input5_Name", "HDMI");
        } else {
            DataInfo.input5_Name = DataInfo.input5;
            sp.edit().putString("input5_Name", DataInfo.input5_Name).apply();
        }

        if (sp.getString("input6_Name", null) != null) {
            DataInfo.input6_Name = sp.getString("input6_Name", "HDMI");
        } else {
            DataInfo.input6_Name = DataInfo.input6;
            sp.edit().putString("input6_Name", DataInfo.input6_Name).apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:        //端口切换按钮 1-6
                ChangeInput(DataInfo.input1);
                break;
            case R.id.btn_2:
                ChangeInput(DataInfo.input2);
                break;
            case R.id.btn_3:
                ChangeInput(DataInfo.input3);
                break;
            case R.id.btn_4:
                ChangeInput(DataInfo.input4);
                break;
            case R.id.btn_5:
                ChangeInput(DataInfo.input5);
                break;
            case R.id.btn_6:
                ChangeInput(DataInfo.input6);
                break;
            case R.id.btn_setting:
                showLockSettingDialog(); //打开设置界面(先弹出密码框)
                break;
            case R.id.btn_lock:
                showLockDialog();   //锁定界面
                break;
            case R.id.btn_abouts:
                showAboutsDialog(); //关于界面
                break;
            case R.id.btn_power:
                if (DataInfo.DelayConnectionState)
                    showWarningDialog();  //网络继电器开启关闭确认界面
                break;
            case R.id.btn_sleep:
                sleep();    //休眠操作 仅关闭背光源
                break;
            case R.id.btn_mode1:        //端口切换按钮 1-6
                ChangeMode("mode1");
                break;
            case R.id.btn_mode2:
                ChangeMode("mode2");
                break;
            case R.id.btn_mode3:
                ChangeMode("mode3");
                break;
            case R.id.btn_mode4:
                ChangeMode("mode4");
                break;
            case R.id.btn_mode5:
                ChangeMode("mode5");
                break;
        }
    }

    private void sleep() {
        //休眠操作 仅关闭背光源
        yfapi.yfSetLCDOff();
        cover.setVisibility(View.VISIBLE);
        countTimerView.cancel();    //休眠开始，关闭屏保定时器
    }

    private void ChangeMode(String ModeName) {
        List<Models> modelsList = LitePal.where("model_name = ? and action_name = ?", DataInfo.model_number, ModeName).find(Models.class);
        if (!modelsList.isEmpty()) {
            String str = modelsList.get(0).getSend_data();
            byte[] data = StrToHexByte(str, "_");
            UDPThread sendThread = new UDPThread();
            sendThread.setData(data);
            sendThread.start();
        }
    }

    private void ChangeInput(String input_name) {
        List<Models> modelsList = LitePal.where("model_name = ? and action_name = ?", DataInfo.model_number, input_name).find(Models.class);
        if (!modelsList.isEmpty()) {
            String str = modelsList.get(0).getSend_data();
            byte[] data = StrToHexByte(str, "_");
            UDPThread sendThread = new UDPThread();
            sendThread.setData(data);
            sendThread.start();
        }
    }

    //更新当前连接input按钮颜色
    private void connection_input(String input) {

        btn_1.setBackgroundResource(R.drawable.input_button);
        btn_2.setBackgroundResource(R.drawable.input_button);
        btn_3.setBackgroundResource(R.drawable.input_button);
        btn_4.setBackgroundResource(R.drawable.input_button);
        btn_5.setBackgroundResource(R.drawable.input_button);
        btn_6.setBackgroundResource(R.drawable.input_button);

        if (input.equals(DataInfo.input1)) {
            btn_1.setBackgroundResource(R.drawable.input_connected);
        } else if (input.equals(DataInfo.input2)) {
            btn_2.setBackgroundResource(R.drawable.input_connected);
        } else if (input.equals(DataInfo.input3)) {
            btn_3.setBackgroundResource(R.drawable.input_connected);
        } else if (input.equals(DataInfo.input4)) {
            btn_4.setBackgroundResource(R.drawable.input_connected);
        } else if (input.equals(DataInfo.input5)) {
            btn_5.setBackgroundResource(R.drawable.input_connected);
        } else if (input.equals(DataInfo.input6)) {
            btn_6.setBackgroundResource(R.drawable.input_connected);
        }
    }

    private void showWarningDialog() {
        WarningDialog warningDialog = new WarningDialog();
        warningDialog.show(getSupportFragmentManager(), "warning");
    }

    private void showLockSettingDialog() {
        LockSettingDialog lockSettingDialog = new LockSettingDialog();
        lockSettingDialog.show(getSupportFragmentManager(), "LockSetting");
    }

    private void showLockDialog() {
        LockDialog lockDialog = new LockDialog();
        lockDialog.show(getSupportFragmentManager(), "Lock");
    }

    //屏保
    private void showScreenSaveDialog() {
        ScreenSaveDialog screenSaveDialog = new ScreenSaveDialog();
        screenSaveDialog.show(getSupportFragmentManager(), "ScreenSave");
    }

    private void showAboutsDialog() {
        AboutsDialog aboutsDialog = new AboutsDialog();
        aboutsDialog.show(getSupportFragmentManager(), "AboutsDialog");
    }

    public void MyHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DataInfo.UPDATACURRENTTIME:    //每秒更新当前时间
                        tv_date.setText(msg.getData().getString("date"));
                        tv_week.setText(msg.getData().getString("week"));
                        tv_hour.setText(msg.getData().getString("hour"));
                        tv_min.setText(msg.getData().getString("min"));
                        break;
                    case DataInfo.NEWCONNECT:   //新连接设备
                        img_connect_state.setImageResource(R.drawable.input_green);
                        update_input_view();    //动态加载端口布局
                        break;
                    case DataInfo.DISCONNECTED:   //设备离线状态
                        img_connect_state.setImageResource(R.drawable.input_red);
                        break;
                    case DataInfo.INPUTCONNECTED:   //刷新按钮连接状态
                        connection_input(msg.getData().getString("input"));
                        break;
                    case DataInfo.SCREENSAVER:  //开启休眠
                        sleep();
                        break;
                    case DataInfo.POWERCONNECT: //网络继电器开启状态
                        btn_power.setImageResource(R.drawable.on);
                        break;
                    case DataInfo.POWERDISCONNECT:  //网络继电器关闭状态
                        btn_power.setImageResource(R.drawable.off);
                        break;
                    case DataInfo.POWEROFFLINE: //网络继电器离线
                        btn_power.setImageResource(R.drawable.offline);
                        break;
                    case DataInfo.COMPLETEEDIT: //端口名称编辑完成
                        UpdateInputName(); //刷新端口名称
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static void TimerRestart() {
        countTimerView.cancel();
        countTimerView.start();
    }

    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                countTimerView.start();
                break;
            //否则其他动作计时取消
            default:
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TimerRestart();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy");
        DataInfo.ConnectionState = false;
        DataInfo.DelayConnectionState = false;
        DataInfo.Thread_alive = false;
        countTimerView.cancel();
    }

}
