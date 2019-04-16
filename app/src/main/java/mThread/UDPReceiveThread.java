package mThread;

import android.os.Bundle;
import android.os.Message;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.List;

import agreement.Models;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;

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
            mSocket.receive(receivePacket);  //接收到数据包 阻塞式
            LastReceiveDate = new Date(System.currentTimeMillis()); //更新最后一次接收到数据包的时间
            String info = HexByteToStr(buf, DataInfo.agreement_len, "_");
            Logger.e("Receive Data: " + info);
            check_return_data(info); //处理接收到的数据
            //mSocket.close();
        } catch (Exception e) {
            Logger.e("receive error");
            e.printStackTrace();
            //break;
        }
    }

    private synchronized void check_return_data(String data) {
        Message msg = HomePageActivity.mHandler.obtainMessage();
        if (DataInfo.ConnectionState) {
            //找到当前接收数据的动作类型
            List<Models> list = LitePal.where("model_name = ? and return_data = ?", DataInfo.model_number, data).find(Models.class);
            Logger.e(list.get(0).getAction_name() + list.get(0).getAction_type());
            if (!list.isEmpty()) {
                //Logger.e(list.get(0).getAction_type());
                switch (list.get(0).getAction_type()) {
                    case "CheckConnect":
                        LastCheckConnectDate = new Date(System.currentTimeMillis());    //更新最新检查连接时间
                        break;
//                    case "input":
                        //收到切换端口操作，发送check_signal协议
//                        List<Models> check_list = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "check_signal").find(Models.class);
//                        if (!check_list.isEmpty()) {
//                            UDPThread sendThread = new UDPThread();
//                            sendThread.setData(StrToHexByte(check_list.get(0).getSend_data(), "_"));
//                            sendThread.start();
//                        }
//                        break;
                    case "check_signal":
                        Logger.e(list.get(0).getAction_name());
                        //接收到的是画面状态查询返回值 更新UI按钮画面连接状态标志
                        msg.what = DataInfo.INPUTCONNECTED;
                        Bundle bundle = new Bundle();
                        bundle.putString("input", list.get(0).getAction_name());
                        msg.setData(bundle);
                        HomePageActivity.mHandler.sendMessage(msg);
                        break;
                    case "mode":
                        //接收到模式转换返回值 更新模式按钮颜色
                        break;
                    default:
                        break;
                }
            }
        } else {
            //找到返回值对应的设备型号
            List<Models> list = LitePal.where("action_name = ? and return_data = ?", "Device", data).find(Models.class);
            if (!list.isEmpty()) {
                DataInfo.model_number = list.get(0).getModel_name(); //设置当前连接的设备名称
                DataInfo.ConnectionState = true;  //连接成功
                LastCheckConnectDate = new Date(System.currentTimeMillis());
                msg.what = DataInfo.NEWCONNECT;
                HomePageActivity.mHandler.sendMessage(msg);
            }
        }


    }

}