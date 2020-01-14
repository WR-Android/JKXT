package mFragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.litepal.LitePal;

import agreement.TimingLists;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.R;
import wheelView.FromToTimePicker;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.countTimerView;

/**
 * Created by Administrator on 2019/5/30.
 */

public class TimingDialog extends DialogFragment {

    public Context mContext;
    private String fromTime;
    private String toTime;
    private char week;
    private int EditFlag;

    public void setTime(String fromTime, String toTime, char week, final int EditFlag) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.week = week;
        this.EditFlag = EditFlag;
    }

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
        View view = inflater.inflate(R.layout.time_picker, container);
        FromToTimePicker fromToTimePicker = view.findViewById(R.id.timePicker);
        fromToTimePicker.setOnResultListener(new FromToTimePicker.OnResultListener() {
            @Override
            public void onConfirm(int fromHour, int fromMinute, int toHour, int toMinute, char week) {
                //数据库添加或修改数据
                if (EditFlag == -1) {
                    //添加数据到数据库
                    TimingLists lists = new TimingLists();
                    lists.setOpentime(fromHour + ":" + fromMinute);
                    lists.setClosetime(toHour + ":" + toMinute);
                    lists.setWeek(week);
                    lists.save();
                } else {
                    //修改数据
                    ContentValues values = new ContentValues();
                    values.put("opentime", fromHour + ":" + fromMinute);
                    values.put("closetime", toHour + ":" + toMinute);
                    values.put("week", String.valueOf(week));
                    LitePal.update(TimingLists.class, values, EditFlag);
                }
                DataInfo.TimingState = false;//任务更改标志
                dismiss();
                showTimingListDialog();
            }

            @Override
            public void onCancel() {
                dismiss();
                showTimingListDialog();
            }
        });
        fromToTimePicker.setCurrentDate(fromTime, toTime, week);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        countTimerView.cancel();
        getDialog().getWindow().setLayout(1000, 800);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        countTimerView.start();
    }

    private void showTimingListDialog() {
        TimingListDialog timingListDialog = new TimingListDialog();
        timingListDialog.show(getFragmentManager(),"TimingList");
    }

}
