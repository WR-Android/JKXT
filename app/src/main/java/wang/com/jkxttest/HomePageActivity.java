package wang.com.jkxttest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import agreement.Models;
import mThread.CheckConnectThread;
import mThread.CheckInputThread;
import mThread.TimeThread;
import mThread.UDPThread;

import static agreement.Models.create_database;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    public static Handler mHandler;
    private static final String TAG = "HomePageActivity";
    private TextView tv_send;
    private TextView tv_receive;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn;
    private EditText et_ip;
    private TextView tv_time;
    List<Models> input_list;

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
            Logger.e("File error");
            e.printStackTrace();
        }
        initView();     //初始化控件

        DataInfo.server_ip = et_ip.getText().toString().trim();
        DataInfo.server_port = 20108;

        new TimeThread().start();   //开时钟线程
        new CheckConnectThread().start();   //开设备连接线程
        new CheckInputThread().start();

        if (!DataInfo.ConnectionState) {
            //加载默认主界面布局
            DataInfo.model_number = "880";
            update_input_view();
        }
    }

    private void update_input_view() {
        //找到当前设备的端口信息
        input_list = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "input").find(Models.class);
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
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_receive = (TextView) findViewById(R.id.tv_receive);
        et_ip = (EditText) findViewById(R.id.et_ip);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn = (Button) findViewById(R.id.btn);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn.setOnClickListener(this);
        MyHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                ChangeInput(input_list.get(0).getAction_name());
                break;
            case R.id.btn_2:
                ChangeInput(input_list.get(1).getAction_name());
                break;
            case R.id.btn_3:
                ChangeInput(input_list.get(2).getAction_name());
                break;
            case R.id.btn_4:
                ChangeInput(input_list.get(3).getAction_name());
                break;
            case R.id.btn_5:
                ChangeInput(input_list.get(4).getAction_name());
                break;
            case R.id.btn_6:
                ChangeInput(input_list.get(5).getAction_name());
                break;
            case R.id.btn:
                DataInfo.server_ip = et_ip.getText().toString().trim();
                List<Models> find = LitePal.findAll(Models.class);
                if (!find.isEmpty())
                    for (Models m : find) {

                        Logger.d("model id is " + m.getId());
                        Log.d(TAG, "model id is " + m.getId());
                        Log.d(TAG, "model name is " + m.getModel_name());
                        Log.d(TAG, "action name is " + m.getAction_name());
                        Log.d(TAG, "send data is " + m.getSend_data());
                        Log.d(TAG, "return data is " + m.getReturn_data());
                        Log.d(TAG, "===========================");
                    }
                break;
        }
    }

    private void ChangeInput(String input_name) {
        List<Models> modelsList = LitePal.where("model_name = ? and action_name = ?", DataInfo.model_number, input_name).find(Models.class);
        //Logger.e(modelsList.size() + "");
        if (!modelsList.isEmpty()) {
            String str = modelsList.get(0).getSend_data();
            tv_send.setText(str);
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

       if(input.equals(DataInfo.input1)){
           btn_1.setBackgroundResource(R.drawable.input_button_click);
       }else if(input.equals(DataInfo.input2)){
           btn_2.setBackgroundResource(R.drawable.input_button_click);
       }else if(input.equals(DataInfo.input3)){
           btn_3.setBackgroundResource(R.drawable.input_button_click);
       }else if(input.equals(DataInfo.input4)){
           btn_4.setBackgroundResource(R.drawable.input_button_click);
       }else if(input.equals(DataInfo.input5)){
           btn_5.setBackgroundResource(R.drawable.input_button_click);
       }else if(input.equals(DataInfo.input6)){
           btn_6.setBackgroundResource(R.drawable.input_button_click);
       }
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
                        tv_receive.setText("连接设备：" + DataInfo.model_number);
                        update_input_view();    //动态加载端口布局
                        break;
                    case DataInfo.DISCONNECTED:
                        tv_receive.setText("断开连接");
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
