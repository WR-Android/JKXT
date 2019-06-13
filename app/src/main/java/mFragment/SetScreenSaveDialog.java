package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mThread.DelayThread;
import wang.com.jkxttest.CountTimer;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;
import wang.com.jkxttest.R;

import static android.content.Context.MODE_PRIVATE;
import static wang.com.jkxttest.DataInfo.StrToHexByte;
import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.countTimerView;

/**
 * Created by Administrator on 2019/5/29.
 */

public class SetScreenSaveDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private EditText et_screenSave;
    private CheckBox cb_startScreenSave;
    private Button btn_screenSave;

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
        View view = inflater.inflate(R.layout.setscreensave_dialog, container);
        // TODO: 2019/5/29 checkbox 框 样式
        initView(view);
        //显示屏保时间
        et_screenSave.setText(DataInfo.ScreenSaveTime);
        cb_startScreenSave.setChecked(DataInfo.ScreenSaveChecked);
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
        et_screenSave = view.findViewById(R.id.et_ScreenSave);
        cb_startScreenSave =  view.findViewById(R.id.cb_startScreenSave);
        btn_screenSave =  view.findViewById(R.id.btn_ScreenSave);
        btn_screenSave.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        TimerRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ScreenSave:
                if (TextUtils.isEmpty(et_screenSave.getText())) {
                    Toast.makeText(mContext, "休眠时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = et_screenSave.getText().toString().trim();
                boolean checked = cb_startScreenSave.isChecked();
                if ("0".equals(s)) {
                    DataInfo.ScreenSaveChecked = false;
                    checked = false;
                }
                if (checked) {
                    Toast.makeText(mContext, "自动休眠开启", Toast.LENGTH_SHORT).show();
                    countTimerView.cancel();
                    countTimerView = new CountTimer(Long.parseLong(s) * 1000, 1000, mContext);
                    countTimerView.start();
                } else {
                    Toast.makeText(mContext, "自动休眠关闭", Toast.LENGTH_SHORT).show();
                }
                DataInfo.ScreenSaveChecked = checked;
                DataInfo.ScreenSaveTime = s;
                SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ScreenSaveTime", s);
                edit.putBoolean("ScreenSaveChecked", checked);
                edit.commit();
                dismiss();
                break;
        }
    }

}
