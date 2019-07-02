package wang.com.jkxttest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class checkActivity extends AppCompatActivity {

    private TextView textMAc;
    private EditText editpwd = null;
    private TextView btnReg;
    private SharedPreferences sp;
    SharedPreferences.Editor editor;
    String check;//mac地址按des加密后的结果

    public final static int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.check);
        textMAc = findViewById(R.id.getMac);
        editpwd = findViewById(R.id.editPwd);
        btnReg = findViewById(R.id.btnReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regCheck();
            }
        });

        //动态获取权限
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            AfterGetPermissions();
        }

    }

    private void AfterGetPermissions() {
        String substr = Md5.md5(AndroidID(), DataInfo.key);
        check = substr.substring(substr.length() - 8, substr.length()).trim();
        sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();
        System.out.println("check1:" + check + ";" + substr);
        System.out.println("checkpass:" + sp.getString("pass", ""));
        if (sp.getString("pass", "").equalsIgnoreCase(check)) {
//    自动跳转到主界面
            Intent intent = new Intent(checkActivity.this, HomePageActivity.class);
            checkActivity.this.startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    AfterGetPermissions();
                }
                break;

            default:
                break;
        }
    }

    private String getMac() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }

        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    private String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

    private String getUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI

                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    public String getAndroidID() {
        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    private String AndroidID() {
        String AndroidID = getIMEI() + getAndroidID();
        String substr = Md5.md5(AndroidID);
        String subAndroidID = substr.substring(substr.length() - 8, substr.length()).trim();
        textMAc.setText(subAndroidID);
        return subAndroidID;
    }

    private void regCheck() {
        String passwordValue = editpwd.getText().toString().trim();
        System.out.println("check:" + check);
        System.out.println("passwordValue:" + passwordValue);
        if (passwordValue.equalsIgnoreCase(check)) {
            editor.putString("pass", passwordValue);
            editor.commit();
            Intent intent = new Intent(checkActivity.this, HomePageActivity.class);
            checkActivity.this.startActivity(intent);
            finish();
        } else {
            Toast.makeText(checkActivity.this, "注册失败，请检查注册码是否正确", Toast.LENGTH_SHORT).show();//反馈给用户登录成功
        }
    }
}

