package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import mThread.DelayThread;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;
import wang.com.jkxttest.R;

import static wang.com.jkxttest.DataInfo.StrToHexByte;
import static wang.com.jkxttest.HomePageActivity.TimerRestart;

/**
 * Created by Administrator on 2019/5/29.
 */

public class WarningDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private Button btn_confirm;
    private Button btn_cancel;
    private TextView tv_warning;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyMiddleDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        View view = inflater.inflate(R.layout.warning_dialog, container);
        initView(view);

        if (DataInfo.PowerState)
            tv_warning.setText("确认关闭大屏电源？");
        else
            tv_warning.setText("确认打开大屏电源？");

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TimerRestart();
                return false;
            }
        });

        return view;
    }

    private void initView(View view) {
        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        tv_warning = view.findViewById(R.id.tv_warning);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }



    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        TimerRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                TimerRestart();
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
                dismiss();
                break;
            case R.id.btn_cancel:
                TimerRestart();
                dismiss();
                break;
        }
    }

}
