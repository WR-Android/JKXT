package mThread;

import android.util.Log;

import org.litepal.LitePal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import agreement.Models;
import wang.com.jkxttest.DataInfo;

import static android.content.ContentValues.TAG;
import static wang.com.jkxttest.DataInfo.HexByteToStr;
import static wang.com.jkxttest.DataInfo.agreement_lenth;

public class UDPReceiveThread extends Thread {

    public static DatagramSocket mSocket = null;
    byte[] buf = new byte[1024];

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
                Log.e(TAG, "===mSocket receive waiting===");
                mSocket.receive(receivePacket);
                Log.e(TAG, "===receive: ok===");
                //String info = new String(buf, 0, receivePacket.getLength());
                String info = HexByteToStr(buf, agreement_lenth, "_");
                //接收到数据包后操作
                check_return_data(info);
                Log.e(TAG, "===" + info + "===");
                //mSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "===receive error===" );
                e.printStackTrace();
                //break;
            }
    }

    private synchronized void check_return_data(String data) {
        if (DataInfo.ConnectionState) {
            //List<Models> list = LitePal.where("model_name = ? and return_data = ?", DataInfo.model_number, data).find(Models.class);
//            if(!list.isEmpty()){
//                list.get(0).getAction_name();
//            }
        } else {
            List<Models> list = LitePal.where("return_data = ?", data).find(Models.class);
            if (!list.isEmpty()) {
                if (list.get(0).getAction_name().equals("Device")) {
                    DataInfo.model_number = list.get(0).getModel_name();
                    DataInfo.ConnectionState = true;
                }
            }
        }


    }
}