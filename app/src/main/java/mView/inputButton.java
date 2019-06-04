package mView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wang.com.jkxttest.R;

/**
 * Created by Administrator on 2019/5/28.
 */

public class inputButton extends RelativeLayout {


    private LinearLayout inputLinear;
    private ImageView img_input;
    private TextView tv_input;

    public inputButton(Context context) {
        super(context);
    }

    public inputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.btn_input, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputButtonItem);

        inputLinear = findViewById(R.id.inputLinear);
        img_input = findViewById(R.id.img_input);
        tv_input = findViewById(R.id.tv_input);

        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        String textString = typedArray.getString(R.styleable.InputButtonItem_text);
        int textColor = typedArray.getColor(R.styleable.InputButtonItem_text_color,
                0xFFFFFFFF);
        float textSize = typedArray.getDimension(R.styleable.InputButtonItem_text_size,
                20);
        tv_input.setText(textString);
        tv_input.setTextColor(textColor);
        tv_input.setTextSize(textSize);
//        int imageHeight = (int) typedArray.getDimension(R.styleable.InputButtonItem_image_height, 25);
//        int imageWidth = (int) typedArray.getDimension(R.styleable.InputButtonItem_image_width, 25);
//        LayoutParams layoutParams = new LayoutParams(imageWidth, imageHeight);
//        img_input.setLayoutParams(layoutParams);

        int imageSrc = typedArray.getResourceId(R.styleable.InputButtonItem_image_src, 0);
        int imageBg = typedArray.getResourceId(R.styleable.InputButtonItem_image_bg, 0);
        img_input.setImageResource(imageSrc);
        img_input.setBackgroundResource(imageBg);
        //img_input.setScaleType(ImageView.ScaleType.FIT_CENTER);

        int bg_src = typedArray.getResourceId(R.styleable.InputButtonItem_bg_src, 0);
        inputLinear.setBackgroundResource(bg_src);

        typedArray.recycle();
    }

    /**
     * 设置此控件的文本
     *
     * @param text
     */
    public void setText(String text) {
        tv_input.setText(text);
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        tv_input.setTextColor(textColor);
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        tv_input.setTextSize(textSize);
    }

    /**
     * 设置图片
     *
     * @param resId
     */
    public void setImageResource(int resId) {
        img_input.setImageResource(resId);
    }


    public void setImgPadding(int padding) {
        img_input.setPadding(padding, padding, padding, padding);
    }

    /**
     * 设置图片背景
     */
    public void setBackgroundResource(int resId) {
        inputLinear.setBackgroundResource(resId);
    }

    /**
     * 设置图片的大小
     * 这里面需要使用LayoutParams这个布局参数来设置
     *
     * @param width
     * @param height
     */
    public void setImageSize(int width, int height) {
        LayoutParams layoutParams = new LayoutParams(width, height);
        img_input.setLayoutParams(layoutParams);
    }

    /**
     * 设置背景图片资源
     *
     * @param resId
     */
    public void setLinearBackgroundResource(int resId) {
        inputLinear.setBackgroundResource(resId);
    }

    /**
     * image点击事件的回调
     *
     * @param listener
     */
    public void setOnClickListener(final OnItemClickListener listener) {
        inputLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick();
            }
        });
    }

    /**
     * 点击事件接口
     *
     * @author mView
     */
    public interface OnItemClickListener {
        public void onImageClick();
    }

}
