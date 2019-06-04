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
import android.widget.Toast;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.PasswordView;
import wang.com.jkxttest.R;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;

/**
 * Created by Administrator on 2019/5/29.
 */

public class LockSettingDialog extends DialogFragment{

    public Context mContext;

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
        View view = inflater.inflate(R.layout.lockpwd, container);
        initView(view);
        mPasswordView.addOnPasswordChangedListener(new PasswordView.OnPasswordChangedListener() {
            @Override
            public void onPasswordChanged(String password) {
            }

            @Override
            public void onPasswordFinish(String password) {
                if (DataInfo.lock_pwd.equals(password)) {
                    dismiss();
                    //打开设置主界面
                    showSettingDialog();
                } else {
                    mPasswordView.clearPassword();
                    Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initListener(view);
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
        mPasswordView = view.findViewById(R.id.et_password);
    }

    private void initListener(View view) {
        for (int i = 0; i < ids.length; i++) {
            buttons[i] = view.findViewById(ids[i]);
            buttons[i].setOnClickListener(listener);
        }
        buttons[10].setText("返回");
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_delete) {
                mPasswordView.delete();
            } else if (v.getId() == R.id.btn_clear) {
                dismiss();
            } else {
                Button button = (Button) v;
                String input = button.getText().toString();
                mPasswordView.input(input);
            }
            TimerRestart();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(false); //设置点击其它地方消失Dialog
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        TimerRestart();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        TimerRestart();
    }

    private void showSettingDialog() {
        SettingDialog settingDialog = new SettingDialog();
        settingDialog.show(getFragmentManager(),"Setting");
    }

}
