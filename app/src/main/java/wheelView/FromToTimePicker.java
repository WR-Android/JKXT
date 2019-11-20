package wheelView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import wang.com.jkxttest.R;


public class FromToTimePicker extends LinearLayout {
    public static final String TAG = "FromToTimePicker";
    private WheelView mWheelFromHour;
    private WheelView mWheelFromMinute;
    private WheelView mWheelToHour;
    private WheelView mWheelToMinute;
    private Button mCancelBtn;
    private Button mConfirmBtn;
    private int mFromMinute;
    private int mFromHour;
    private int mToHour;
    private int mToMinute;
    private char week;

    private CheckBox week1;
    private CheckBox week2;
    private CheckBox week3;
    private CheckBox week4;
    private CheckBox week5;
    private CheckBox week6;
    private CheckBox week7;

    private OnResultListener onResultListener;

    private WheelView.OnSelectListener mFromHourListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int hour, String text) {
            mFromHour = hour;
        }

        @Override
        public void selecting(int id, String text) {
            mFromHour = id;
        }
    };

    private WheelView.OnSelectListener mFromMinuteListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int minute, String text) {
            mFromMinute = minute;
        }

        @Override
        public void selecting(int id, String text) {
            mFromMinute = id;
        }
    };


    private WheelView.OnSelectListener mToHourListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int hour, String text) {
            mToHour = hour;
        }

        @Override
        public void selecting(int hour, String text) {
            mToHour = hour;
        }
    };
    private WheelView.OnSelectListener mToMinuteListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int minute, String text) {
            mToMinute = minute;
        }

        @Override
        public void selecting(int minute, String text) {
            mToMinute = minute;
        }
    };

    private Context mContext;

    public FromToTimePicker(Context context) {
        this(context, null);
    }

    public FromToTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWheelViewItemNumber(int number) {
        mWheelFromHour.setItemNumber(number);
        mWheelFromMinute.setItemNumber(number);
        mWheelToHour.setItemNumber(number);
        mWheelToMinute.setItemNumber(number);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContext = getContext();
        LayoutInflater.from(mContext).inflate(R.layout.timingselect, this);
        mWheelFromHour = findViewById(R.id.from_hour);
        mWheelFromMinute = findViewById(R.id.from_minute);
        mWheelToHour = findViewById(R.id.to_hour);
        mWheelToMinute = findViewById(R.id.to_minute);

        week1 = findViewById(R.id.week_1);
        week2 = findViewById(R.id.week_2);
        week3 = findViewById(R.id.week_3);
        week4 = findViewById(R.id.week_4);
        week5 = findViewById(R.id.week_5);
        week6 = findViewById(R.id.week_6);
        week7 = findViewById(R.id.week_7);

        mCancelBtn = findViewById(R.id.btn_cancel);
        mConfirmBtn = findViewById(R.id.btn_confirm);
        mWheelFromHour.setOnSelectListener(mFromHourListener);
        mWheelFromMinute.setOnSelectListener(mFromMinuteListener);
        mWheelToHour.setOnSelectListener(mToHourListener);
        mWheelToMinute.setOnSelectListener(mToMinuteListener);
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                char rWeek = SelectedWeek();
                if (rWeek == 0x00) {
                    Toast.makeText(mContext, "星期设置不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onResultListener != null) {
                    onResultListener.onConfirm(mFromHour, mFromMinute, mToHour, mToMinute, rWeek);
                }
            }

        });
        mCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onResultListener != null) {
                    onResultListener.onCancel();
                }
            }
        });
        setDate();
    }

    private void setWeek() {
        if ((week & 0x01) != (byte) 0x00) {
            week1.setChecked(true);
        }
        if ((week & 0x01 << 1) != (byte) 0x00) {
            week2.setChecked(true);
        }
        if ((week & 0x01 << 2) != (byte) 0x00) {
            week3.setChecked(true);
        }
        if ((week & 0x01 << 3) != (byte) 0x00) {
            week4.setChecked(true);
        }
        if ((week & 0x01 << 4) != (byte) 0x00) {
            week5.setChecked(true);
        }
        if ((week & 0x01 << 5) != (byte) 0x00) {
            week6.setChecked(true);
        }
        if ((week & 0x01 << 6) != (byte) 0x00) {
            week7.setChecked(true);
        }
    }

    private char SelectedWeek() {
        char WeekDay = 0x00;
        if (week1.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01);
        }
        if (week2.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 1);
        }
        if (week3.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 2);
        }
        if (week4.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 3);
        }
        if (week5.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 4);
        }
        if (week6.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 5);
        }
        if (week7.isChecked()) {
            WeekDay = (char) (WeekDay | 0x01 << 6);
        }
        return WeekDay;
    }

    private ArrayList<String> getHourData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            list.add(i + "");
        }
        return list;
    }

    private ArrayList<String> getMinuteData(int max) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i <= max; i++) {
            list.add(i + "");
        }
        return list;
    }

    public interface OnResultListener {
        void onConfirm(int fromHour, int fromMinute, int toHour, int toMinute, char week);

        void onCancel();
    }

    private void setDate() {
        mWheelFromHour.setData(getHourData());
        mWheelFromMinute.setData(getMinuteData(59));
        mWheelToHour.setData(getHourData());
        mWheelToMinute.setData(getMinuteData(59));
    }

    public void setCurrentDate(String from, String to, char weekday) {
        //从外面设置星期进来 0x00代表没有设置星期
        week = weekday;
        setWeek();
        // 从外面设置当前的时间进来
        mFromHour = TimeUtil.getHourFromTime(from);
        mFromMinute = TimeUtil.getMinuteFromTime(from);
        mToHour = TimeUtil.getHourFromTime(to);
        mToMinute = TimeUtil.getMinuteFromTime(to);

//        mWheelFromHour.setItemNumber(5);
//        mWheelFromMinute.setItemNumber(5);
//        mWheelToHour.setItemNumber(7);
//        mWheelToMinute.setItemNumber(9);


//        mWheelFromHour.setCyclic(false);
//        mWheelFromMinute.setCyclic(false);
//        mWheelToHour.setCyclic(false);
//        mWheelToMinute.setCyclic(false);

        mWheelFromHour.setDefault(mFromHour);
        mWheelFromMinute.setDefault(mFromMinute);
        mWheelToHour.setDefault(mToHour);
        mWheelToMinute.setDefault(mToMinute);

    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}