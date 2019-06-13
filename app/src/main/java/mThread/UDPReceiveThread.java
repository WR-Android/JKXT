package mThread;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.List;

import agreement.Models;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

import static wang.com.jkxttest.CrashHandler.TAG;
import static wang.com.jkxttest.DataInfo.HexByteToStr;

public class UDPReceiveThread extends Thread {

    public static DatagramSocket mSocket = null;
    byte[] buf = new byte[1024];

    public static Date LastCheckConnectDate = new Date(System.currentTimeMillis());
    public static Date LastReceiveDate = new Date(System.currentTimeMillis());
    public static Date CurrentTime = new Date(System.currentTimeMillis());

    @Override
    public void run() {
        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
        try {
            mSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //while (true)
        try {
            mSocket.setSoTimeout(1000);
            mSocket.receive(receivePacket);  //接收到数据包 阻塞式
            LastReceiveDate = new Date(System.currentTimeMillis()); //更新最后一次接收到数据包的时间
            String info = HexByteToStr(buf, DataInfo.agreement_len, "_");
            //Logger.e("Receive Data: " + info);
            check_return_data(info); //处理接收到的数据
            //mSocket.close();
        } catch (Exception e) {
            Log.e(TAG, "Receive Error");
            //e.printStackTrace();
            //break;
        }
    }

    private synchronized void check_return_data(String data) {
        Message msg = HomePageActivity.mHandler.obtainMessage();
        if (DataInfo.ConnectionState) {
            //找到当前接收数据的动作类型
            List<Models> list = LitePal.where("model_name = ? and return_data = ?", DataInfo.model_number, data).find(Models.class);
            //Logger.e(list.get(0).getAction_name() + "  " + list.get(0).getAction_type());
            if (!list.isEmpty()) {
                switch (list.get(0).getAction_type()) {
                    case "CheckConnect":
                        LastCheckConnectDate = new Date(System.currentTimeMillis());    //更新最新检查连接时间
                        break;
                    case "check_signal":    //接收到的是画面状态查询返回值 更新UI按钮画面连接状态标志
                        if (DataInfo.last_input != list.get(0).getAction_name()) {
                            DataInfo.last_input = list.get(0).getAction_name();
                            msg.what = DataInfo.INPUTCONNECTED;
                            Bundle bundle = new Bundle();
                            bundle.putString("input", list.get(0).getAction_name());
                            msg.setData(bundle);
                            HomePageActivity.mHandler.sendMessage(msg);
                        }
                        break;
                    case "mode":
                        //TODO 接收到模式转换返回值 更新模式按钮颜色
                        break;
                    default:
                        break;
                }
            }
        } else {
            //找到返回值对应的设备型号
            List<Models> list = LitePal.where("action_name = ? and return_data = ?", "Device", data).find(Models.class);
            if (!list.isEmpty()) {
                String model_name = list.get(0).getModel_name();
                if(!model_name.equals(DataInfo.model_number)){
                    DataInfo.NewModel = true;
                    //TODO 更新主页面端口名称
                }
                DataInfo.model_number = model_name; //设置当前连接的设备名称
                DataInfo.ConnectionState = true;  //连接成功
                LastCheckConnectDate = new Date(System.currentTimeMillis());
                msg.what = DataInfo.NEWCONNECT;
                HomePageActivity.mHandler.sendMessage(msg);
            }
        }


    }

}