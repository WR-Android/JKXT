package wang.com.jkxttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import agreement.Models;
import mThread.UDPThread;

import static agreement.Models.create_database;
import static wang.com.jkxttest.DataInfo.StrToHexByte;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    //public static Handler mHandler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        if (!(new File(DataInfo.DB_PATH + DataInfo.DB_NAME).exists())) {
            Log.e(TAG, "===创建了数据库===");
            create_database();      //初始化数据库
        }
        else{
            Log.e(TAG, "===数据库已存在 没有创建数据库===");
        }
        initView();     //初始化控件
        if (TextUtils.isEmpty(et_ip.getText())) {
            Toast.makeText(this, "IP不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DataInfo.server_ip = et_ip.getText().toString().trim();
        DataInfo.server_port = 20108;

        List<Models> device_list = LitePal.where("action_name = ?", "Device").find(Models.class);
        byte[] data = StrToHexByte(device_list.get(0).getSend_data(), "_");
        UDPThread sendThread = new UDPThread();
        sendThread.setData(data);
        sendThread.start();
        //while (!DataInfo.ConnectionState) {
//        for (Models x : device_list) {
//            sendThread = new UDPThread();
//            byte[] data = StrToHexByte(x.getSend_data(), "_");
//            sendThread.setData(data);
//            sendThread.start();
//        }
        //}
        Toast.makeText(this, "当前连接设备：" + DataInfo.model_number, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                ChangeInput("CV1");
                break;
            case R.id.btn_2:
                ChangeInput("CV2");
                break;
            case R.id.btn_3:
                ChangeInput("VGA");
                break;
            case R.id.btn_4:
                ChangeInput("DVI");
                break;
            case R.id.btn_5:
                ChangeInput("HDMI");
                break;
            case R.id.btn_6:
                ChangeInput("USB");
                break;
            case R.id.btn:
                List<Models> find = LitePal.findAll(Models.class);
                for (Models m : find) {
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

    public void ChangeInput(String input_name) {
        List<Models> modelsList = LitePal.where("model_name = ? and action_name = ?", DataInfo.model_number, input_name).find(Models.class);
        if (!modelsList.isEmpty()) {
            String str = modelsList.get(0).getSend_data();
            tv_send.setText(str);
            byte[] data = StrToHexByte(str, "_");
            UDPThread sendThread = new UDPThread();
            sendThread.setData(data);
            sendThread.start();
        }
        if (DataInfo.ConnectionState)
            tv_receive.setText("连接成功");
        else
            tv_receive.setText("连接失败");
    }

//    public void MyHandler() {
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case DataInfo.RECEIVED_DATA:
//                        String data = msg.getData().getString("data");
//                        List<Models> list = LitePal.where("model = ? and return_data = ?", DataInfo.model_number, data).find(Models.class);
//                        if (!list.isEmpty()) {
//
//                        }
//                        break;
//                    case 2:
//
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//    }

}
