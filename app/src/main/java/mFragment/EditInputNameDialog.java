package mFragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

import agreement.Models;
import mView.inputButton;
import wang.com.jkxttest.DataInfo;
import wang.com.jkxttest.R;

import static android.content.Context.MODE_PRIVATE;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
import static wang.com.jkxttest.DataInfo.HexByteToStr;
import static wang.com.jkxttest.DataInfo.StrToHexByte;
import static wang.com.jkxttest.HomePageActivity.TimerRestart;
import static wang.com.jkxttest.HomePageActivity.mHandler;

/**
 * Created by Administrator on 2019/5/29.
 */

public class EditInputNameDialog extends DialogFragment implements View.OnClickListener {

    public Context mContext;

    private EditText et_inputName;
    private Button btn_save;
    private Button btn_complete;
    private Button btn_default;

    int btn_Selected = 0;
    inputButton btn_1;
    inputButton btn_2;
    inputButton btn_3;
    inputButton btn_4;
    inputButton btn_5;
    inputButton btn_6;
    private EditText et_No1;
    private EditText et_No2;
    private EditText et_No3;
    private EditText et_No4;
    private EditText et_No5;
    private EditText et_No6;
    private List<Models> InputNumList;
    private String chars;

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
        View view = inflater.inflate(R.layout.editinput_dialog, container);
        initView(view);
        setInputView(); //初始化inputButton控件文字图案
        et_inputName.setText(DataInfo.input1_Name);
        btn_Selected = 1;   // 默认编辑第一个input
        refreshButton();
        initSoftInputListener();    //点击非输入框区域时，自动收起键盘
        et_inputName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerRestart();
            }
        });
        et_inputName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                TimerRestart();
                return false;
            }
        });

        et_inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TimerRestart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    /**
     * 点击非输入框区域时，自动收起键盘
     */
    private void initSoftInputListener() {
        getDialog().getWindow().getDecorView()
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        InputMethodManager manager = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (getDialog().getCurrentFocus() != null
                                    && getDialog().getCurrentFocus().getWindowToken() != null) {
                                manager.hideSoftInputFromWindow(
                                        getDialog().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        return false;
                    }
                });
    }

    private void initView(View view) {
        //可编辑文本框
        et_inputName = view.findViewById(R.id.et_inputName);
        btn_save = view.findViewById(R.id.btn_save); //保存按钮
        btn_default = view.findViewById(R.id.btn_default); //恢复默认按钮
        btn_complete = view.findViewById(R.id.btn_complete);//完成按钮
        btn_1 = view.findViewById(R.id.btn_1);
        btn_2 = view.findViewById(R.id.btn_2);
        btn_3 = view.findViewById(R.id.btn_3);
        btn_4 = view.findViewById(R.id.btn_4);
        btn_5 = view.findViewById(R.id.btn_5);
        btn_6 = view.findViewById(R.id.btn_6);

        et_No1 = view.findViewById(R.id.et_No1);
        et_No2 = view.findViewById(R.id.et_No2);
        et_No3 = view.findViewById(R.id.et_No3);
        et_No4 = view.findViewById(R.id.et_No4);
        et_No5 = view.findViewById(R.id.et_No5);
        et_No6 = view.findViewById(R.id.et_No6);

        InputNumList = LitePal.where("model_name = ? and action_type = ?", DataInfo.model_number, "input").find(Models.class);
        if (!InputNumList.isEmpty()) {
            chars = "0123456789ABCDEF";
            byte[] bytes;

            bytes = StrToHexByte(InputNumList.get(0).getSend_data(), "_");
            et_No1.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

            bytes = StrToHexByte(InputNumList.get(1).getSend_data(), "_");
            et_No2.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

            bytes = StrToHexByte(InputNumList.get(2).getSend_data(), "_");
            et_No3.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

            bytes = StrToHexByte(InputNumList.get(3).getSend_data(), "_");
            et_No4.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

            bytes = StrToHexByte(InputNumList.get(4).getSend_data(), "_");
            et_No5.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

            bytes = StrToHexByte(InputNumList.get(5).getSend_data(), "_");
            et_No6.setText("" + chars.charAt(Integer.valueOf(bytes[4] >> 4 & 0x0F)) + chars.charAt(Integer.valueOf(bytes[4] & 0x0F)));

        }

        btn_save.setOnClickListener(this);
        btn_default.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
    }

    private void setInputView() {
        int res_id;
        btn_1.setText(DataInfo.input1_Name);
        res_id = getResources().getIdentifier(DataInfo.input1.toLowerCase(), "drawable", mContext.getPackageName());
        btn_1.setImageResource(res_id);


        btn_2.setText(DataInfo.input2_Name);
        res_id = getResources().getIdentifier(DataInfo.input2.toLowerCase(), "drawable", mContext.getPackageName());
        btn_2.setImageResource(res_id);

        btn_3.setText(DataInfo.input3_Name);
        res_id = getResources().getIdentifier(DataInfo.input3.toLowerCase(), "drawable", mContext.getPackageName());
        btn_3.setImageResource(res_id);

        btn_4.setText(DataInfo.input4_Name);
        res_id = getResources().getIdentifier(DataInfo.input4.toLowerCase(), "drawable", mContext.getPackageName());
        btn_4.setImageResource(res_id);

        btn_5.setText(DataInfo.input5_Name);
        res_id = getResources().getIdentifier(DataInfo.input5.toLowerCase(), "drawable", mContext.getPackageName());
        btn_5.setImageResource(res_id);

        btn_6.setText(DataInfo.input6_Name);
        res_id = getResources().getIdentifier(DataInfo.input6.toLowerCase(), "drawable", mContext.getPackageName());
        btn_6.setImageResource(res_id);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(false);
        /**
         * 将对话框外部（未被遮挡的部分）的背景设置为透明（为收起对话框需要）
         */
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = BRIGHTNESS_OVERRIDE_OFF;
        window.setAttributes(windowParams);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        TimerRestart();
        Message msg = mHandler.obtainMessage();
        msg.what = DataInfo.COMPLETEEDIT;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        TimerRestart();
        switch (v.getId()) {
            case R.id.btn_save:
                String str = et_inputName.getText().toString().trim();
                if (str.isEmpty()) {
                    Toast.makeText(mContext, "自定义名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                changeName(str);
                break;
            case R.id.btn_default:
                defaultName();
                break;
            case R.id.btn_complete:
                // TODO: 2019-08-07 获取编号文本框内容，修改数据库
                changeSQL();
                dismiss();
                break;
            case R.id.btn_1:
                et_inputName.setText(DataInfo.input1_Name);
                btn_Selected = 1;
                refreshButton();
                break;
            case R.id.btn_2:
                et_inputName.setText(DataInfo.input2_Name);
                btn_Selected = 2;
                refreshButton();
                break;
            case R.id.btn_3:
                et_inputName.setText(DataInfo.input3_Name);
                btn_Selected = 3;
                refreshButton();
                break;
            case R.id.btn_4:
                et_inputName.setText(DataInfo.input4_Name);
                btn_Selected = 4;
                refreshButton();
                break;
            case R.id.btn_5:
                et_inputName.setText(DataInfo.input5_Name);
                btn_Selected = 5;
                refreshButton();
                break;
            case R.id.btn_6:
                et_inputName.setText(DataInfo.input6_Name);
                btn_Selected = 6;
                refreshButton();
                break;
        }
    }

    private void changeSQL() {
        Models models;
        String Str;
        byte[] bit,bytes;
        byte b;

        Str = et_No1.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(0).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(0).getId());

        Str = et_No2.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(1).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(1).getId());

        Str = et_No3.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(2).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(2).getId());

        Str = et_No4.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(3).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(3).getId());

        Str = et_No5.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(4).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(4).getId());

        Str = et_No6.getText().toString().trim();
        bit = Str.getBytes();
        b = (byte) (chars.indexOf(bit[0]) << 4 | chars.indexOf(bit[1]));
        bytes = StrToHexByte(InputNumList.get(5).getSend_data(), "_");
        bytes[4] = b;
        bytes[5] = (byte) (bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]);
        models = new Models();
        models.setSend_data(HexByteToStr(bytes, DataInfo.agreement_len, "_"));
        models.update(InputNumList.get(5).getId());
    }

    private void defaultName() {
        switch (btn_Selected) {
            case 1:
                et_inputName.setText(DataInfo.input1);
                break;
            case 2:
                et_inputName.setText(DataInfo.input2);
                break;
            case 3:
                et_inputName.setText(DataInfo.input3);
                break;
            case 4:
                et_inputName.setText(DataInfo.input4);
                break;
            case 5:
                et_inputName.setText(DataInfo.input5);
                break;
            case 6:
                et_inputName.setText(DataInfo.input6);
                break;
            default:
                et_inputName.setText("");
                break;
        }
    }

    private void changeName(String s) {

        SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
        switch (btn_Selected) {
            case 1:
                btn_1.setText(s);
                DataInfo.input1_Name = s;
                sp.edit().putString("input1_Name", s).apply();
                break;
            case 2:
                btn_2.setText(s);
                DataInfo.input2_Name = s;
                sp.edit().putString("input2_Name", s).apply();
                break;
            case 3:
                btn_3.setText(s);
                DataInfo.input3_Name = s;
                sp.edit().putString("input3_Name", s).apply();
                break;
            case 4:
                btn_4.setText(s);
                DataInfo.input4_Name = s;
                sp.edit().putString("input4_Name", s).apply();
                break;
            case 5:
                btn_5.setText(s);
                DataInfo.input5_Name = s;
                sp.edit().putString("input5_Name", s).apply();
                break;
            case 6:
                btn_6.setText(s);
                DataInfo.input6_Name = s;
                sp.edit().putString("input6_Name", s).apply();
                break;
        }
    }

    private void refreshButton() {
        btn_1.setLinearBackgroundResource(R.drawable.input);
        btn_2.setLinearBackgroundResource(R.drawable.input);
        btn_3.setLinearBackgroundResource(R.drawable.input);
        btn_4.setLinearBackgroundResource(R.drawable.input);
        btn_5.setLinearBackgroundResource(R.drawable.input);
        btn_6.setLinearBackgroundResource(R.drawable.input);
        switch (btn_Selected) {
            case 1:
                btn_1.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
            case 2:
                btn_2.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
            case 3:
                btn_3.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
            case 4:
                btn_4.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
            case 5:
                btn_5.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
            case 6:
                btn_6.setLinearBackgroundResource(R.drawable.input_pressed);
                break;
        }
    }

}
