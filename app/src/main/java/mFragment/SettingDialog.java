package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import wang.com.jkxttest.R;
import wang.com.jkxttest.TouchScaleAnim;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.countTimerView;

/**
 * Created by Administrator on 2019/5/30.
 */

public class SettingDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private Button btn_setSystem;
    private Button btn_setScreenSave;
    private Button btn_setLockPassword;
    private Button btn_setIpAndPort;
    private Button btn_lockonoff;
    private Button btn_editInputName;

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
        View view = inflater.inflate(R.layout.setting_dialog, container);
        initView(view);

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
        btn_setIpAndPort = view.findViewById(R.id.setIpAndPort);
        btn_setLockPassword = view.findViewById(R.id.setLockPassword);
        btn_setScreenSave = view.findViewById(R.id.setScreenSave);
        btn_setSystem = view.findViewById(R.id.setSystem);
        btn_lockonoff = view.findViewById(R.id.lockonoff);
        btn_editInputName = view.findViewById(R.id.editInputName);
        TouchScaleAnim.attachView(btn_setIpAndPort);
        TouchScaleAnim.attachView(btn_setLockPassword);
        TouchScaleAnim.attachView(btn_setScreenSave);
        TouchScaleAnim.attachView(btn_setSystem);
        TouchScaleAnim.attachView(btn_lockonoff);
        TouchScaleAnim.attachView(btn_editInputName);
        btn_setIpAndPort.setOnClickListener(this);
        btn_setLockPassword.setOnClickListener(this);
        btn_setScreenSave.setOnClickListener(this);
        btn_setSystem.setOnClickListener(this);
        btn_lockonoff.setOnClickListener(this);
        btn_editInputName.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        TimerRestart();
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setIpAndPort:
                TimerRestart();
                dismiss();
                showIpPortDialog();
                break;
            case R.id.setLockPassword:
                TimerRestart();
                dismiss();
                showSetPasswordDialog();
                break;
            case R.id.setScreenSave:
                TimerRestart();
                dismiss();
                showSetScreenSaveDialog();
                break;
            case R.id.setSystem:
                dismiss();
                countTimerView.cancel();
                getActivity().startActivity(new Intent(Settings.ACTION_SETTINGS));//打开系统设置
                break;
            case R.id.lockonoff:
                TimerRestart();
                dismiss();
                showTimingListDialog();  //定时开关列表界面
                break;
            case R.id.editInputName:
                TimerRestart();
                dismiss();
                showEditInputNameDialog();
                break;
        }
    }

    private void showIpPortDialog() {
        IpPortDialog ipPortDialog = new IpPortDialog();
        ipPortDialog.show(getFragmentManager(), "IpPort");
    }

    private void showSetPasswordDialog() {
        SetPasswordDialog setPasswordDialog = new SetPasswordDialog();
        setPasswordDialog.show(getFragmentManager(), "SetPassword");
    }

    private void showSetScreenSaveDialog() {
        SetScreenSaveDialog setScreenSaveDialog = new SetScreenSaveDialog();
        setScreenSaveDialog.show(getFragmentManager(), "SetScreenSave");
    }

    private void showTimingListDialog() {
        TimingListDialog timingListDialog = new TimingListDialog();
        timingListDialog.show(getFragmentManager(),"TimingList");
    }

    // TODO: 2019/5/28 端口按钮自定义名称编辑
    private void showEditInputNameDialog() {
        EditInputNameDialog editInputNameDialog = new EditInputNameDialog();
        editInputNameDialog.show(getFragmentManager(), "EditInputName");
    }

}
