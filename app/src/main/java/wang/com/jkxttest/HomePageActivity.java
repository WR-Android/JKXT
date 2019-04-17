package wang.com.jkxttest;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import agreement.Models;
import mThread.CheckConnectThread;
import mThread.CheckInputThread;
import mThread.TimeThread;
import mThread.UDPThread;

import static agreement.CreateDataBase.create_database;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    public static Handler mHandler;
    private static final String TAG = "HomePageActivity";
    private TextView tv_receive;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private TextView tv_time;
    private ImageView img_connect_state;
    private Button btn_setting;
    private Button btn_lock;
    private Button btn_abouts;
    private Dialog mDialog;
    private Button bt_confirm;
    private Button bt_cancel;
    private EditText et_ip;
    private EditText et_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init().hideThreadInfo();
        setContentView(R.layout.activity_homepage);
        try {
            if (!(new File(DataInfo.DB_PATH + DataInfo.DB_NAME).exists())) {    //判断数据库文件是否已存在
                Log.e(TAG, "===创建数据库===");
                create_database();      //初始化数据库
            } else {
                Log.e(TAG, "===数据库已存在 没有创建数据库===");
            }
        } catch (Exception e) {
            Logger.e("DB File Error");
            e.printStackTrace();
        }
        initView();     //初始化控件
        initSP();   //初始化提取保存的ip和port

        new TimeThread().start();   //开时钟线程
        new CheckConnectThread().start();   //开设备连接线程
        new CheckInputThread().start();

        //加载默认主界面布局
        DataInfo.model_number = "880";
        update_input_view();
        DataInfo.last_input = DataInfo.input1;

    }

    private void update_input_view() {
        //找到当前设备的端口信息
        List<Models> input_list = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "input").find(Models.class);
        if (!input_list.isEmpty()) {
            DataInfo.input1 = input_list.get(0).getAction_name();
            DataInfo.input2 = input_list.get(1).getAction_name();
            DataInfo.input3 = input_list.get(2).getAction_name();
            DataInfo.input4 = input_list.get(3).getAction_name();
            DataInfo.input5 = input_list.get(4).getAction_name();
            DataInfo.input6 = input_list.get(5).getAction_name();
            Logger.e("刷新按钮控件");
            btn_1.setText(DataInfo.input1);
            btn_2.setText(DataInfo.input2);
            btn_3.setText(DataInfo.input3);
            btn_4.setText(DataInfo.input4);
            btn_5.setText(DataInfo.input5);
            btn_6.setText(DataInfo.input6);
        }
    }

    private void initView() {
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_lock = (Button) findViewById(R.id.btn_lock);
        btn_abouts = (Button) findViewById(R.id.btn_abouts);
        img_connect_state = (ImageView) findViewById(R.id.img_connect_state);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_receive = (TextView) findViewById(R.id.tv_receive);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);

        btn_setting.setOnClickListener(this);
        btn_lock.setOnClickListener(this);
        btn_abouts.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        MyHandler();
    }

    private void initSP() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getString("ip", null) != null) {
            DataInfo.server_ip = sp.getString("ip", "192.168.1.223");
        } else {
            DataInfo.server_ip = "192.168.1.223";
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("ip", "192.168.1.223");
        }

        if (sp.getString("port", null) != null) {
            DataInfo.server_port = Integer.parseInt(sp.getString("port", "20108"));
        } else {
            DataInfo.server_port = 20108;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("port", "20108");
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
                showIpPortDialog();
                break;
            case R.id.btn_lock:
                showLockDialog();
                break;
            case R.id.btn_abouts:
                showAboutsDialog();
                break;
        }
    }

    private void showLockDialog() {
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.setting_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.width = 300;
//        lp.height = 300;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
//        HomePageActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    private void showAboutsDialog() {
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.abouts_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.width = 300;
//        lp.height = 300;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);

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
            btn_1.setBackgroundResource(R.drawable.input_button_click);
        } else if (input.equals(DataInfo.input2)) {
            btn_2.setBackgroundResource(R.drawable.input_button_click);
        } else if (input.equals(DataInfo.input3)) {
            btn_3.setBackgroundResource(R.drawable.input_button_click);
        } else if (input.equals(DataInfo.input4)) {
            btn_4.setBackgroundResource(R.drawable.input_button_click);
        } else if (input.equals(DataInfo.input5)) {
            btn_5.setBackgroundResource(R.drawable.input_button_click);
        } else if (input.equals(DataInfo.input6)) {
            btn_6.setBackgroundResource(R.drawable.input_button_click);
        }
    }

    public void showIpPortDialog() {
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.ipportsetdialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.width = 300;
//        lp.height = 300;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        mDialog.getWindow().setAttributes(lp);
        //设置点击其它地方消失Dialog
        mDialog.setCancelable(true);
        initIpPortDialogView(dialogView);
        //将sp中存储的ip port显示
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        et_ip.setText(sp.getString("ip", null));
        et_port.setText(sp.getString("port", null));

        initIpPortDialogListener();
    }

    private void initIpPortDialogView(View view) {
        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_port = (EditText) view.findViewById(R.id.et_port);
        bt_confirm = (Button) view.findViewById(R.id.btn_confirm);
        bt_cancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void initIpPortDialogListener() {

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_ip.getText())) {
                    Toast.makeText(HomePageActivity.this, "IP地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_port.getText())) {
                    Toast.makeText(HomePageActivity.this, "端口号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataInfo.server_ip = et_ip.getText().toString().trim();
                DataInfo.server_port = Integer.parseInt(et_port.getText().toString().trim());
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ip", DataInfo.server_ip);
                edit.putString("port", String.valueOf(DataInfo.server_port));
                edit.commit();
                mDialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
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
                        tv_time.setText(msg.getData().getString("time"));
                        break;
                    case DataInfo.NEWCONNECT:   //新连接设备
                        tv_receive.setText("连接设备:" + DataInfo.model_number);
                        img_connect_state.setImageResource(R.drawable.green);
                        update_input_view();    //动态加载端口布局
                        break;
                    case DataInfo.DISCONNECTED:
                        tv_receive.setText("连接已断开");
                        img_connect_state.setImageResource(R.drawable.red);
                        break;
                    case DataInfo.INPUTCONNECTED:
                        connection_input(msg.getData().getString("input"));
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
