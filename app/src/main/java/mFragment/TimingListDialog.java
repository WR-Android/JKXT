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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import agreement.TimingLists;
import swipeMenu.SwipeBean;
import swipeMenu.SwipeMenuLayout;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.R;

import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.countTimerView;

/**
 * Created by Administrator on 2019/5/30.
 */

public class TimingListDialog extends DialogFragment {

    public Context mContext;
    private ListView timingListView;
    private List<SwipeBean> mData;

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
        View view = inflater.inflate(R.layout.timinglist, container);

        //ListView数据添加交互显示
        timingListView = view.findViewById(R.id.TimingList);
        timingListView.setDivider(null);
        //初始化ListView数据
        mData = new ArrayList<>();
        List<TimingLists> lists = LitePal.findAll(TimingLists.class);
        int i = 1;
        for (TimingLists list : lists) {
            mData.add(new SwipeBean(list.getId(), "定时" + i, list.getOpentime(), list.getClosetime(), list.getWeek()));
            i++;
        }
        timingListView.setAdapter(new CommonAdapter<SwipeBean>(getContext(), mData, R.layout.item_timing) {
            @Override
            public void convert(final ViewHolder viewHolder, SwipeBean swipeBean, final int position) {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                viewHolder.setText(R.id.tv_TimingId, swipeBean.timingId);
                viewHolder.setText(R.id.tv_OpenTime, DataInfo.GetStandardTime(swipeBean.openTime));
                viewHolder.setText(R.id.tv_CloseTime, DataInfo.GetStandardTime(swipeBean.closeTime));
                viewHolder.setText(R.id.tv_TimingWeek, DataInfo.WeekToString(swipeBean.timingWeek));
                viewHolder.setOnClickListener(R.id.itemLL, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimerRestart();
                    }
                });
                viewHolder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mContext, "删除:" + position, Toast.LENGTH_SHORT).show();
                        TimerRestart();
                        LitePal.delete(TimingLists.class, mDatas.get(position).id); //删除数据库表中数据
                        //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) viewHolder.getConvertView()).quickClose();
                        mDatas.remove(position);
                        notifyDataSetChanged(); //刷新item内容
                        DataInfo.TimingState = false;   //设置标志让线程 发送定时任务
                    }
                });
                viewHolder.setOnClickListener(R.id.btnEdit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //编辑列表中数据
                        dismiss();
                        countTimerView.cancel();
                        showTimingDialog(mDatas.get(position).openTime, mDatas.get(position).closeTime, mData.get(position).timingWeek, mDatas.get(position).id);
                    }
                });
            }
        });

        //添加按钮
        Button btn_addTiming = view.findViewById(R.id.btn_addTiming);
        btn_addTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                countTimerView.cancel();
                showTimingDialog("8:00", "22:00", (char) 0x00, -1);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TimerRestart();
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(1000, 800);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        TimerRestart();
    }

    /**
     * @param fromTime 开机时间
     * @param toTime   关机时间
     * @param week     定时星期
     * @param EditFlag 编辑标志  -1：在数据库添加定时
     *                 other：编辑数据库已有EditFlag位置的定时
     */
    private void showTimingDialog(String fromTime, String toTime, char week, final int EditFlag) {
        countTimerView.cancel();
        TimingDialog timingDialog = new TimingDialog();
        timingDialog.setTime(fromTime, toTime, week, EditFlag);
        timingDialog.show(getFragmentManager(), "TimingDialog");
    }

}
