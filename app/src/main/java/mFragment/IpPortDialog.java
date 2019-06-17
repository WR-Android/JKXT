package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.R;

import static android.content.Context.MODE_PRIVATE;
import static wang.com.jkxttest.HomePageActivity.TimerRestart;

/**
 * Created by Administrator on 2019/5/29.
 */

public class IpPortDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private Button btn_confirm;
    private Button btn_cancel;
    private EditText et_delay_ip;
    private EditText et_ip;
    private EditText et_port;

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

        // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.ipportsetdialog, container);
        initView(view);

        //将sp中存储的ip port显示
        SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
        et_ip.setText(sp.getString("ip", null));
        et_port.setText(sp.getString("port", null));
        et_delay_ip.setText(sp.getString("delay_ip", null));

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
        et_ip = view.findViewById(R.id.et_ip);
        et_port = view.findViewById(R.id.et_port);
        et_delay_ip = view.findViewById(R.id.et_delay_ip);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        et_ip.setOnClickListener(this);
        et_port.setOnClickListener(this);
        et_delay_ip.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TimerRestart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        et_ip.addTextChangedListener(textWatcher);
        et_delay_ip.addTextChangedListener(textWatcher);
        et_port.addTextChangedListener(textWatcher);

        et_ip.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                TimerRestart();
                return false;
            }
        });
        et_delay_ip.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                TimerRestart();
                return false;
            }
        });
        et_port.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                TimerRestart();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        TimerRestart();
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(et_ip.getText())) {
                    Toast.makeText(mContext, "IP地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_port.getText())) {
                    Toast.makeText(mContext, "端口号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_delay_ip.getText())) {
                    Toast.makeText(mContext, "网络继电器IP地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataInfo.server_ip = et_ip.getText().toString().trim();
                DataInfo.server_port = Integer.parseInt(et_port.getText().toString().trim());
                DataInfo.delay_ip = et_delay_ip.getText().toString().trim();
                SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ip", DataInfo.server_ip);
                edit.putString("port", String.valueOf(DataInfo.server_port));
                edit.putString("delay_ip", DataInfo.delay_ip);
                edit.commit();
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(true); //设置点击其它地方消失Dialog
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
}
