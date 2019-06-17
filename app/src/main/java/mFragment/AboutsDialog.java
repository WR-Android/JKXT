package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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

import com.orhanobut.logger.Logger;

import me.jessyan.autosize.internal.CancelAdapt;
import wang.com.jkxttest.R;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;

/**
 * Created by Administrator on 2019/5/29.
 */

public class AboutsDialog extends DialogFragment implements View.OnClickListener{

    public Context mContext;

    private TextView tv_version;
    private Button abouts_confirm;

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
        View view = inflater.inflate(R.layout.abouts_dialog, container);
        initView(view);
        String versionName = "";

        try {
            versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("VersionName Achieve Error");
            e.printStackTrace();
        }
        tv_version.setText("版本：" + versionName);

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
        tv_version = view.findViewById(R.id.tv_version);
        abouts_confirm = view.findViewById(R.id.abouts_confirm);
        abouts_confirm.setOnClickListener(this);
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
            case R.id.abouts_confirm:
                TimerRestart();
                dismiss();
                break;
        }
    }

}
