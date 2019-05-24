package wang.com.jkxttest;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import agreement.Models;
import agreement.TimingLists;
import mThread.CheckConnectThread;
import mThread.CheckInputThread;
import mThread.DelayConnectThread;
import mThread.DelayThread;
import mThread.TimeThread;
import mThread.UDPThread;
import mThread.setTimingThread;
import swipeMenu.SwipeBean;
import swipeMenu.SwipeMenuLayout;
import wheelView.FromToTimePicker;

import static agreement.CreateDataBase.create_database;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private CountTimer countTimerView;
    public static Handler mHandler;
    private ImageButton btn_1;
    private ImageButton btn_2;
    private ImageButton btn_3;
    private ImageButton btn_4;
    private ImageButton btn_5;
    private ImageButton btn_6;
    private ImageView img_connect_state;
    private ImageButton btn_setting;
    private ImageButton btn_lock;
    private ImageButton btn_abouts;
    private ImageButton btn_sleep;
    private Dialog mDialog;
    private Dialog ScreenSaverDialog;
    private Button bt_confirm;
    private Button bt_cancel;
    private EditText et_ip;
    private EditText et_port;
    private Button btn_setSystem;
    private Button btn_setScreenSave;
    private Button btn_setLockPassword;
    private Button btn_setIpAndPort;
    private Button btn_lockonoff;
    private Button btn_editInputName;

    private PasswordView mPasswordView;
    private final int[] ids = {
            R.id.btn_00,
            R.id.btn_01,
            R.id.btn_02,
            R.id.btn_03,
            R.id.btn_04,
            R.id.btn_05,
            R.id.btn_06,
            R.id.btn_07,
            R.id.btn_08,
            R.id.btn_09,
            R.id.btn_clear,
            R.id.btn_delete
    };
    private Button[] buttons = new Button[ids.length];
    private Button btn_saveLockPwd;
    private EditText et_lockpwd;
    private EditText et_screenSave;
    private CheckBox cb_startScreenSave;
    private Button btn_screenSave;
    private RangeSeekBar sound_seekbar;
    private TextView tv_sound;
    private TextView tv_date;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_week;
    private EditText et_delay_ip;
    private ImageButton btn_power;
    private TextView tv_warning;
    private ImageButton btn_m1;
    private ImageButton btn_m2;
    private ImageButton btn_m3;
    private ImageButton btn_m4;
    private ImageButton btn_m5;
    private Button abouts_confirm;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataInfo.Thread_alive = true;   //线程开启标记
        Logger.init().hideThreadInfo();
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
        DataInfo.last_input = DataInfo.input1;
        initTime();//加载当前时间

        //查询网络继电器状态
        DelayThread delayThread = new DelayThread();
        delayThread.setDelaydata(StrToHexByte("FF_02_01_00_BB", "_"));
        delayThread.start();

        sound_seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {  //声音滑块数值监听
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                tv_sound.setText(String.valueOf(Float.valueOf(leftValue).intValue()) + "%");
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
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("model_number", DataInfo.model_number);
            edit.commit();

            DataInfo.input1 = input_list.get(0).getAction_name();
            DataInfo.input2 = input_list.get(1).getAction_name();
            DataInfo.input3 = input_list.get(2).getAction_name();
            DataInfo.input4 = input_list.get(3).getAction_name();
            DataInfo.input5 = input_list.get(4).getAction_name();
            DataInfo.input6 = input_list.get(5).getAction_name();

            btn_1.setBackgroundResource(R.drawable.input_button);
            btn_2.setBackgroundResource(R.drawable.input_button);
            btn_3.setBackgroundResource(R.drawable.input_button);
            btn_4.setBackgroundResource(R.drawable.input_button);
            btn_5.setBackgroundResource(R.drawable.input_button);
            btn_6.setBackgroundResource(R.drawable.input_button);

            res_id = getResources().getIdentifier(DataInfo.input1.toLowerCase(), "drawable", getPackageName());
            btn_1.setImageResource(res_id);
            res_id = getResources().getIdentifier(DataInfo.input2.toLowerCase(), "drawable", getPackageName());
            btn_2.setImageResource(res_id);
            res_id = getResources().getIdentifier(DataInfo.input3.toLowerCase(), "drawable", getPackageName());
            btn_3.setImageResource(res_id);
            res_id = getResources().getIdentifier(DataInfo.input4.toLowerCase(), "drawable", getPackageName());
            btn_4.setImageResource(res_id);
            res_id = getResources().getIdentifier(DataInfo.input5.toLowerCase(), "drawable", getPackageName());
            btn_5.setImageResource(res_id);
            res_id = getResources().getIdentifier(DataInfo.input6.toLowerCase(), "drawable", getPackageName());
            btn_6.setImageResource(res_id);
        }
    }

    private void initView() {
        btn_power = findViewById(R.id.btn_power);   //大屏电源
        btn_setting = findViewById(R.id.btn_setting);
        btn_lock = findViewById(R.id.btn_lock);
        btn_abouts = findViewById(R.id.btn_abouts);
        btn_sleep = findViewById(R.id.btn_sleep);
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
            sp.edit().putString("ip", "192.168.1.223").commit();
        }

        if (sp.getString("port", null) != null) {
            DataInfo.server_port = Integer.parseInt(sp.getString("port", "20108"));
        } else {
            DataInfo.server_port = 20108;
            sp.edit().putString("port", "20108").commit();
        }

        if (sp.getString("delay_ip", null) != null) {
            DataInfo.delay_ip = sp.getString("delay_ip", "192.168.1.245");
        } else {
            DataInfo.delay_ip = "192.168.1.245";
            sp.edit().putString("delay_ip", "192.168.1.245").commit();
        }

        if (sp.getString("pwd", null) != null) {
            DataInfo.lock_pwd = sp.getString("pwd", "1234");
        } else {
            DataInfo.lock_pwd = "1234";
            sp.edit().putString("pwd", "1234").commit();
        }

        if (sp.getString("ScreenSaveTime", null) != null) {
            DataInfo.ScreenSaveTime = sp.getString("ScreenSaveTime", "60");
        } else {
            DataInfo.ScreenSaveTime = "60";
            sp.edit().putString("ScreenSaveTime", "60").commit();
        }

        if (sp.getBoolean("ScreenSaveChecked", false)) {
            DataInfo.ScreenSaveChecked = sp.getBoolean("ScreenSaveChecked", false);
        } else {
            DataInfo.ScreenSaveChecked = false;
            sp.edit().putBoolean("ScreenSaveChecked", false).commit();
        }

        if (sp.getString("model_number", null) != null) {
            DataInfo.model_number = sp.getString("model_number", "600");
        } else {
            DataInfo.model_number = "600";
            sp.edit().putString("model_number", "600").commit();
        }

        if (sp.getBoolean("TimingState", false)) { //定时任务设置成功标志位
            DataInfo.TimingState = sp.getBoolean("TimingState", false);
        } else {
            DataInfo.TimingState = false;
            sp.edit().putBoolean("TimingState", false).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
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
                    showWarningDialog_power();  //网络继电器开启关闭界面
                break;
            case R.id.btn_sleep:
                // TODO: 2019/5/17 休眠操作
                Toast.makeText(this, "休眠", Toast.LENGTH_SHORT).show();
                break;
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
            btn_1.setBackgroundResource(R.drawable.input_button_connect);
        } else if (input.equals(DataInfo.input2)) {
            btn_2.setBackgroundResource(R.drawable.input_button_connect);
        } else if (input.equals(DataInfo.input3)) {
            btn_3.setBackgroundResource(R.drawable.input_button_connect);
        } else if (input.equals(DataInfo.input4)) {
            btn_4.setBackgroundResource(R.drawable.input_button_connect);
        } else if (input.equals(DataInfo.input5)) {
            btn_5.setBackgroundResource(R.drawable.input_button_connect);
        } else if (input.equals(DataInfo.input6)) {
            btn_6.setBackgroundResource(R.drawable.input_button_connect);
        }
    }

    private void showWarningDialog_power() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.warning_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initWarningDialog_powerView(dialogView);

        if (DataInfo.PowerState)
            tv_warning.setText("确认关闭大屏电源？");
        else
            tv_warning.setText("确认打开大屏电源？");

        initWarningDialog_powerListener();
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void initWarningDialog_powerView(View view) {
        bt_confirm = view.findViewById(R.id.btn_confirm);
        bt_cancel = view.findViewById(R.id.btn_cancel);
        tv_warning = view.findViewById(R.id.tv_warning);
    }

    private void initWarningDialog_powerListener() {
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                if (DataInfo.PowerState) {
                    //发送继电器1断开指令
                    DelayThread delayThread = new DelayThread();
                    delayThread.setDelaydata(StrToHexByte("FF_03_01_0A_BB", "_"));
                    delayThread.start();
                } else {
                    //发送继电器1吸合指令
                    DelayThread delayThread = new DelayThread();
                    delayThread.setDelaydata(StrToHexByte("FF_03_01_0B_BB", "_"));
                    delayThread.start();
                }
                mDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
            }
        });
    }

    private void showLockSettingDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.lockpwd, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.width = 300;
//        lp.height = 300;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);

        mPasswordView = dialogView.findViewById(R.id.et_password);
        mPasswordView.addOnPasswordChangedListener(new PasswordView.OnPasswordChangedListener() {
            @Override
            public void onPasswordChanged(String password) {
            }

            @Override
            public void onPasswordFinish(String password) {
                if (DataInfo.lock_pwd.equals(password)) {
                    mDialog.dismiss();
                    showSettingDialog();
                } else {
                    mPasswordView.clearPassword();
                    Toast.makeText(HomePageActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initLockDialogListener(dialogView);
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void showSettingDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.setting_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initSettingDialogView(dialogView);
        initSettingDialogListener();
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void initSettingDialogView(View view) {
        btn_setIpAndPort = view.findViewById(R.id.setIpAndPort);
        btn_setLockPassword = view.findViewById(R.id.setLockPassword);
        btn_setScreenSave = view.findViewById(R.id.setScreenSave);
        btn_setSystem = view.findViewById(R.id.setSystem);
        btn_lockonoff = view.findViewById(R.id.lockonoff);
        btn_editInputName = view.findViewById(R.id.editInputName);
        TouchScaleAnim.attachView(btn_setIpAndPort);
        TouchScaleAnim.attachView(btn_setLockPassword);
        TouchScaleAnim.attachView(btn_setScreenSave);
        TouchScaleAnim.attachView(btn_setSystem);
        TouchScaleAnim.attachView(btn_lockonoff);
        TouchScaleAnim.attachView(btn_editInputName);
    }

    private void initSettingDialogListener() {
        btn_setIpAndPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showIpPortDialog();
            }
        });

        btn_setLockPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showSetPasswordDialog();
            }
        });

        btn_setScreenSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showSetScreenSaveDialog();
            }
        });

        btn_setSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                HomePageActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                mDialog.dismiss();
            }
        });

        btn_lockonoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showTimingListDialog();  //定时开关列表界面
            }
        });

        btn_editInputName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                //showEditInputNameDialog();
            }
        });
    }

    public void showTimingListDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.timinglist, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = 750;
        lp.height = 550;
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);

        //ListView数据添加交互显示
        final ListView TimingListView = dialogView.findViewById(R.id.TimingList);
        TimingListView.setDivider(null);
        //初始化ListView数据
        final List<SwipeBean> mData = new ArrayList<>();
        List<TimingLists> lists = LitePal.findAll(TimingLists.class);
        int i = 1;
        for (TimingLists list : lists) {
            mData.add(new SwipeBean(list.getId(), "定时" + i, list.getOpentime(), list.getClosetime(), list.getWeek()));
            i++;
        }
        TimingListView.setAdapter(new CommonAdapter<SwipeBean>(this, mData, R.layout.item_timing) {
            @Override
            public void convert(final ViewHolder viewHolder, SwipeBean swipeBean, final int position) {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                viewHolder.setText(R.id.tv_TimingId, swipeBean.timingId);
                viewHolder.setText(R.id.tv_OpenTime, DataInfo.GetStandardTime(swipeBean.openTime));
                viewHolder.setText(R.id.tv_CloseTime, DataInfo.GetStandardTime(swipeBean.closeTime));
                viewHolder.setText(R.id.tv_TimingWeek, DataInfo.WeekToString(swipeBean.timingWeek));
                viewHolder.setOnClickListener(R.id.itemLL, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TimingListView.getChildAt(position).setSelected(true);
                        Toast.makeText(HomePageActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HomePageActivity.this, "删除:" + position, Toast.LENGTH_SHORT).show();
                        LitePal.delete(TimingLists.class, mDatas.get(position).id); //删除数据库表中数据
                        //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) viewHolder.getConvertView()).quickClose();
                        mDatas.remove(position);
                        notifyDataSetChanged(); //刷新item内容
                        DataInfo.TimingState = false;   //设置标志让线程 发送定时任务
                    }
                });
                viewHolder.setOnClickListener(R.id.btnEdit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //编辑列表中数据
                        mDialog.dismiss();
                        showTimingDialog(mDatas.get(position).openTime, mDatas.get(position).closeTime, mData.get(position).timingWeek, mDatas.get(position).id);
                    }
                });
            }
        });

        //添加按钮
        Button btn_addTiming = dialogView.findViewById(R.id.btn_addTiming);
        btn_addTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showTimingDialog("8:00", "22:00", (char) 0x00, -1);
            }
        });

        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }


    /**
     * @param fromTime 开机时间
     * @param toTime   关机时间
     * @param EditFlag 编辑标志  -1：在数据库添加定时
     *                 other：编辑数据库已有EditFlag位置的定时
     */
    public void showTimingDialog(String fromTime, String toTime, char week, final int EditFlag) {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.time_picker, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = 750;
        lp.height = 550;
        //lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);

        FromToTimePicker fromToTimePicker = dialogView.findViewById(R.id.timePicker);
        fromToTimePicker.setOnResultListener(new FromToTimePicker.OnResultListener() {
            @Override
            public void onConfirm(int fromHour, int fromMinute, int toHour, int toMinute, char week) {
                countTimerView.cancel();
                countTimerView.start();
                //数据库添加或修改数据
                if (EditFlag == -1) {
                    //添加数据到数据库
                    TimingLists lists = new TimingLists();
                    lists.setOpentime(fromHour + ":" + fromMinute);
                    lists.setClosetime(toHour + ":" + toMinute);
                    lists.setWeek(week);
                    lists.save();
                } else {
                    //修改数据
                    ContentValues values = new ContentValues();
                    values.put("opentime", fromHour + ":" + fromMinute);
                    values.put("closetime", toHour + ":" + toMinute);
                    values.put("week", String.valueOf(week));
                    LitePal.update(TimingLists.class, values, EditFlag);
                }
                DataInfo.TimingState = false;//任务更改标志
                mDialog.dismiss();
                showTimingListDialog();
                //Toast.makeText(HomePageActivity.this, "From " + fromHour + ":" + fromMinute + " To " + toHour + ":" + toMinute + " Week " + (byte) week, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
                showTimingListDialog();
                //Toast.makeText(HomePageActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        //fromToTimePicker.setCurrentDate(TimeUtil.getTimeString(), TimeUtil.addTime(TimeUtil.getTimeString(), "8:00").getTime());
        fromToTimePicker.setCurrentDate(fromTime, toTime, week);

        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    public void showIpPortDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.ipportsetdialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initIpPortDialogView(dialogView);
        //将sp中存储的ip port显示
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        et_ip.setText(sp.getString("ip", null));
        et_port.setText(sp.getString("port", null));
        et_delay_ip.setText(sp.getString("delay_ip", null));
        initIpPortDialogListener();
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void initIpPortDialogView(View view) {
        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_port = (EditText) view.findViewById(R.id.et_port);
        et_delay_ip = (EditText) view.findViewById(R.id.et_delay_ip);
        bt_confirm = (Button) view.findViewById(R.id.btn_confirm);
        bt_cancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void initIpPortDialogListener() {
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                if (TextUtils.isEmpty(et_ip.getText())) {
                    Toast.makeText(HomePageActivity.this, "IP地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_port.getText())) {
                    Toast.makeText(HomePageActivity.this, "端口号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_delay_ip.getText())) {
                    Toast.makeText(HomePageActivity.this, "网络继电器IP地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataInfo.server_ip = et_ip.getText().toString().trim();
                DataInfo.server_port = Integer.parseInt(et_port.getText().toString().trim());
                DataInfo.delay_ip = et_delay_ip.getText().toString().trim();
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ip", DataInfo.server_ip);
                edit.putString("port", String.valueOf(DataInfo.server_port));
                edit.putString("delay_ip", DataInfo.delay_ip);
                edit.commit();
                mDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
            }
        });
    }

    private void showSetPasswordDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.setlockpwd, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initSetPasswordDialogView(dialogView);
        //将sp中存储的密码pwd显示
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        et_lockpwd.setText(sp.getString("pwd", null));
        initSetPasswordDialogListener();
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void initSetPasswordDialogView(View view) {
        et_lockpwd = (EditText) view.findViewById(R.id.et_lockpwd);
        btn_saveLockPwd = (Button) view.findViewById(R.id.saveLockPwd);
    }

    private void initSetPasswordDialogListener() {
        btn_saveLockPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimerView.cancel();
                countTimerView.start();
                if (TextUtils.isEmpty(et_lockpwd.getText())) {
                    Toast.makeText(HomePageActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = et_lockpwd.getText().toString().trim();
                if (s.length() != 4) {
                    Toast.makeText(HomePageActivity.this, "密码长度错误，请输入四位密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataInfo.lock_pwd = et_lockpwd.getText().toString().trim();
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("pwd", DataInfo.lock_pwd);
                edit.commit();
                mDialog.dismiss();
            }
        });
    }

    private void showSetScreenSaveDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.setscreensave_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.width = 300;
//        lp.height = 300;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initSetScreenSaveDialogView(dialogView);
        //将sp中存储的密码pwd显示
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        et_screenSave.setText(DataInfo.ScreenSaveTime);
        cb_startScreenSave.setChecked(DataInfo.ScreenSaveChecked);
        initSetScreenSaveDialogListener();
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
    }

    private void initSetScreenSaveDialogView(View view) {
        et_screenSave = (EditText) view.findViewById(R.id.et_ScreenSave);
        cb_startScreenSave = (CheckBox) view.findViewById(R.id.cb_startScreenSave);
        btn_screenSave = (Button) view.findViewById(R.id.btn_ScreenSave);
    }

    private void initSetScreenSaveDialogListener() {
        btn_screenSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_screenSave.getText())) {
                    Toast.makeText(HomePageActivity.this, "屏保时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = et_screenSave.getText().toString().trim();
                boolean checked = cb_startScreenSave.isChecked();
                if ("0".equals(s)) {
                    DataInfo.ScreenSaveChecked = false;
                    checked = false;
                }
                if (checked) {
                    Toast.makeText(HomePageActivity.this, "屏保开启", Toast.LENGTH_SHORT).show();
                    countTimerView.cancel();
                    countTimerView = new CountTimer(Long.parseLong(s) * 1000, 1000, HomePageActivity.this);
                    countTimerView.start();
                } else {
                    Toast.makeText(HomePageActivity.this, "屏保关闭", Toast.LENGTH_SHORT).show();
                }
                DataInfo.ScreenSaveChecked = checked;
                DataInfo.ScreenSaveTime = s;
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ScreenSaveTime", s);
                edit.putBoolean("ScreenSaveChecked", checked);
                edit.commit();
                mDialog.dismiss();
            }
        });
    }

    private void showScreenSaveDialog() {
        //1.创建一个Dialog对象
        ScreenSaverDialog = new Dialog(this);
        //去除标题栏
        ScreenSaverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.screensaver_dialog, null);
        //将自定义布局设置进去
        ScreenSaverDialog.setContentView(dialogView);
        //3.设置指定的宽高
        ScreenSaverDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        ScreenSaverDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ScreenSaverDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ScreenSaverDialog.show();

        //设置点击其它地方消失Dialog
        ScreenSaverDialog.setCancelable(true);
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                ScreenSaverDialog.dismiss();
                return false;
            }
        });
    }

    private void showLockDialog() {

        //1.创建一个Dialog对象
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.lockpwd, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(false);
        mPasswordView = (PasswordView) dialogView.findViewById(R.id.et_password);
        mPasswordView.addOnPasswordChangedListener(new PasswordView.OnPasswordChangedListener() {
            @Override
            public void onPasswordChanged(String password) {
            }

            @Override
            public void onPasswordFinish(String password) {
                if (DataInfo.lock_pwd.equals(password)) {
                    mDialog.dismiss();
                } else {
                    mPasswordView.clearPassword();
                    Toast.makeText(HomePageActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initLockDialogListener(dialogView);
        dialogView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
    }

    private void initLockDialogListener(View view) {
        for (int i = 0; i < ids.length; i++) {
            buttons[i] = (Button) view.findViewById(ids[i]);
            buttons[i].setOnClickListener(listener);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_delete) {
                mPasswordView.delete();
            } else if (v.getId() == R.id.btn_clear) {
                mPasswordView.clearPassword();
            } else {
                Button button = (Button) v;
                String input = button.getText().toString();
                mPasswordView.input(input);
            }
            countTimerView.cancel();
            countTimerView.start();
        }
    };

    private void showAboutsDialog() {
        //1.创建一个Dialog对象
        mDialog = new Dialog(this, R.style.ShareDialog);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.abouts_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        String versionName = "";
        tv_version = dialogView.findViewById(R.id.tv_version);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("VersionName Achieve Error");
            e.printStackTrace();
        }
        tv_version.setText("版本：" + versionName);
        abouts_confirm = dialogView.findViewById(R.id.abouts_confirm);
        abouts_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTimerView.cancel();
                countTimerView.start();
                mDialog.dismiss();
            }
        });

        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countTimerView.cancel();
                countTimerView.start();
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countTimerView.cancel();
                countTimerView.start();
            }
        });
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
                    case DataInfo.DISCONNECTED:
                        img_connect_state.setImageResource(R.drawable.input_red);
                        break;
                    case DataInfo.INPUTCONNECTED:
                        connection_input(msg.getData().getString("input"));
                        break;
                    case DataInfo.SCREENSAVER:  //开启屏保
                        showScreenSaveDialog();
                        break;
                    case DataInfo.POWERCONNECT:
                        btn_power.setImageResource(R.drawable.on);
                        break;
                    case DataInfo.POWERDISCONNECT:
                        btn_power.setImageResource(R.drawable.off);
                        break;
                    case DataInfo.POWEROFFLINE:
                        btn_power.setImageResource(R.drawable.offline);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static void sendTimingList() {
        //设置网络继电器周循环定时任务
        List<TimingLists> all = LitePal.findAll(TimingLists.class);
        byte[] TimingData = new byte[all.size() * 14 + 4];
        if (all.isEmpty()) {
            //定时任务为空
            DelayThread delayThread = new DelayThread();
            delayThread.setDelaydata(StrToHexByte("FF_0A_01_00_BB", "_"));
            delayThread.start();
            return;
        }
        int i = 0;
        TimingData[i++] = (byte) 0xFF;    //开始位
        TimingData[i++] = (byte) 0x0A;    //功能标志位
        TimingData[i++] = (byte) ((all.size() * 14) & 0xff); //指令长度
        for (TimingLists buf : all) {
            String[] open = buf.getOpentime().split(":");
            String[] close = buf.getClosetime().split(":");

            TimingData[i++] = (byte) (Integer.valueOf(open[0]) & 0xff); //开机时间：时
            TimingData[i++] = (byte) (Integer.valueOf(open[1]) & 0xff); //开机时间：分
            TimingData[i++] = (byte) 0x00;                                 //开机时间：秒
            TimingData[i++] = (byte) 0x01;//继电器控制位
            TimingData[i++] = (byte) 0x01;//设置开关继电器的状态
            TimingData[i++] = (byte) (buf.getWeek() & 0xff);//设置循环周期
            TimingData[i++] = (byte) 0x01;//定时功能开关

            TimingData[i++] = (byte) (Integer.valueOf(close[0]) & 0xff); //关机时间：时
            TimingData[i++] = (byte) (Integer.valueOf(close[1]) & 0xff); //关机时间：分
            TimingData[i++] = (byte) 0x00;                                  //关机时间：秒
            TimingData[i++] = (byte) 0x01;//继电器控制位
            TimingData[i++] = (byte) 0x00;//设置开关继电器的状态
            TimingData[i++] = (byte) (buf.getWeek() & 0xff);//设置循环周期
            TimingData[i++] = (byte) 0x01;//定时功能开关
        }
        TimingData[i] = (byte) 0xBB; //结束位

        //发送定时任务
        DelayThread delayThread = new DelayThread();
        delayThread.setDelaydata(TimingData);
        delayThread.start();
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
        countTimerView.cancel();
        countTimerView.start();
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
