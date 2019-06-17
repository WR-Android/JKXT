package mFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.HomePageActivity;
import wang.com.jkxttest.R;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.countTimerView;
import static wang.com.jkxttest.HomePageActivity.mHandler;

/**
 * Created by Administrator on 2019/5/29.
 */

public class ScreenSaveDialog extends DialogFragment {

    public Context mContext;
    private Button cover; //休眠覆盖全屏按钮
    private GestureDetector mGestureDetector;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyMiddleDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        View view = inflater.inflate(R.layout.screensaver_dialog, container);

        cover = view.findViewById(R.id.Cover);

        cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                dismiss();
                countTimerView.start(); //休眠结束，开启定时
                mHandler.sendMessage(mHandler.obtainMessage(DataInfo.SetLCDOn));
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {//长按事件
                super.onLongPress(e);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
