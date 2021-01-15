package com.XuanRan.ChangeIMEI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public static volatile transient Test $test=new Test() {
        @Override
        public Object test() {
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ($test==null){
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "NOT NULL", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT > 23) {

            requestPower();
        }
        TextView tv = findViewById(R.id.textview);
        TelephonyManager tele = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        try {
            tv.setText(tele.getDeviceId()+"\n"+tele.getSubscriberId());
        } catch (Exception e) {
            AlertDialog.Builder Alert=new AlertDialog.Builder(this);
            Alert.setTitle("错误");
            Alert.setMessage("获取IMEI号码失败，可能与设备SDK版本号过高有关系（29），当前SDK版本：" + Build.VERSION.SDK_INT);
            Alert.setPositiveButton("确定", null);
            Alert.show();
        }
    }


    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] ==PackageManager. PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
