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
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.R;

import static android.content.Context.MODE_PRIVATE;
import static wang.com.jkxttest.HomePageActivity.TimerRestart;

/**
 * Created by Administrator on 2019/5/29.
 */

public class SetPasswordDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private Button btn_saveLockPwd;
    private EditText et_lockpwd;

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
        View view = inflater.inflate(R.layout.setlockpwd, container);
        initView(view);

        //将sp中存储的密码pwd显示
        SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
        et_lockpwd.setText(sp.getString("pwd", null));

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
        et_lockpwd = view.findViewById(R.id.et_lockpwd);
        btn_saveLockPwd = view.findViewById(R.id.saveLockPwd);
        btn_saveLockPwd.setOnClickListener(this);
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
            case R.id.saveLockPwd:
                TimerRestart();
                if (TextUtils.isEmpty(et_lockpwd.getText())) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = et_lockpwd.getText().toString().trim();
                if (s.length() != 4) {
                    Toast.makeText(mContext, "密码长度错误，请输入四位密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataInfo.lock_pwd = et_lockpwd.getText().toString().trim();
                SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("pwd", DataInfo.lock_pwd);
                edit.commit();
                dismiss();
                break;
        }
    }

}
