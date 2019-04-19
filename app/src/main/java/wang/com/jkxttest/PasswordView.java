package wang.com.jkxttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2019/4/18.
 */

public class PasswordView extends View {
    Paint mPaint;
    int color;
    int backgroundColor;
    int strokeColor;
    int strokeWidth;

    String password = "";
    int passwordLength;

    OnPasswordChangedListener changedListener;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordView);
        color = array.getColor(R.styleable.PasswordView_pv_color, Color.BLACK);
        backgroundColor = array.getColor(R.styleable.PasswordView_pv_background_color, Color.WHITE);
        strokeColor = array.getColor(R.styleable.PasswordView_pv_stroke_color, Color.BLACK);
        strokeWidth = array.getDimensionPixelSize(R.styleable.PasswordView_pv_stroke_width, 1);
        passwordLength = array.getInt(R.styleable.PasswordView_pv_password_length, 4);
        array.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void addOnPasswordChangedListener(OnPasswordChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public void input(String txt) {
        if (password.length() >= passwordLength)
            return;

        password += txt;
        postInvalidate();

        if (changedListener == null)
            return;

        changedListener.onPasswordChanged(password);
        if (password.length() >= passwordLength)
            changedListener.onPasswordFinish(password);
    }

    public void delete() {
        if (password.length() <= 0)
            return;

        password = password.substring(0, password.length() - 1);
        postInvalidate();
        if (changedListener != null)
            changedListener.onPasswordChanged(password);
    }

    public void clearPassword() {
        password = "";
        postInvalidate();
        if (changedListener != null)
            changedListener.onPasswordChanged(password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int widthSize = getMeasuredWidth();
        int heightSize = getMeasuredHeight();
        //把宽度分成passwordLength等份
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize / passwordLength, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int stepWidth = width / passwordLength;

        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, width, height, mPaint);

        mPaint.setColor(strokeColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, width - strokeWidth, height - strokeWidth, mPaint);
        for (int i = 1; i < passwordLength; i++) {
            canvas.drawLine(stepWidth * i, 0, stepWidth * i, height, mPaint);
        }

        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        int radius = stepWidth / 8;
        int len = password.length() > passwordLength ? passwordLength : password.length();
        for (int i = 0; i < len; i++) {
            canvas.drawCircle(stepWidth / 2 + stepWidth * i, height / 2, radius, mPaint);
        }
    }

    public interface OnPasswordChangedListener {
        void onPasswordChanged(String password);

        void onPasswordFinish(String password);
    }
}