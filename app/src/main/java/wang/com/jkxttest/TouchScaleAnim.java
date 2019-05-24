package wang.com.jkxttest;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2019/5/17.
 */

public class TouchScaleAnim implements View.OnTouchListener{

    /*
    * 点击的动画效果
    * 不做任何逻辑处理，只是模拟交互效果
    * */
    public static void attachView(View view)
    {
        TouchScaleAnim touchScaleAnim=new TouchScaleAnim();
        view.setOnTouchListener(touchScaleAnim);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                ObjectAnimator.ofFloat(view,"scaleX",1.0f,0.7f).setDuration(0).start();
                ObjectAnimator.ofFloat(view,"scaleY",1.0f,0.7f).setDuration(0).start();
            }
            break;
            case MotionEvent.ACTION_UP:
            {
                ObjectAnimator.ofFloat(view,"scaleX",0.7f,1.0f).setDuration(0).start();
                ObjectAnimator.ofFloat(view,"scaleY",0.7f,1.0f).setDuration(0).start();
            }
            break;
            case  MotionEvent.ACTION_CANCEL:
            {
                ObjectAnimator.ofFloat(view,"scaleX",0.7f,1.0f).setDuration(0).start();
                ObjectAnimator.ofFloat(view,"scaleY",0.7f,1.0f).setDuration(0).start();
            }
            break;
        }


        return false;
    }

}
