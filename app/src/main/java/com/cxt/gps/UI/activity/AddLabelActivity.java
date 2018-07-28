package com.cxt.gps.UI.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.cxt.gps.R;
import com.cxt.gps.util.ShowUtils;

public class AddLabelActivity extends SuperActivity {
    private TextView tv_title;
    private ImageView iv_back;
    private EditText et_label;
    private ImageView iv_scanner;
    private Button btn_complete;
    private static final int REQUEST_CODE = 111;
    private String carNum,vin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);
        initView();
        init();
        initEvents();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        et_label = findViewById(R.id.et_label);
        iv_scanner = findViewById(R.id.iv_scanner);
        btn_complete = findViewById(R.id.btn_complete);
    }
    private void init(){
        carNum = getIntent().getStringExtra("carNum");
        vin = getIntent().getStringExtra("vin");
        tv_title.setText(getResources().getString(R.string.add_label));
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner();
            }
        });
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WhitelistActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
            }
        });
    }
    public void scanner(){
        if (Build.VERSION.SDK_INT > 22){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
            }else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                startCaptureActivityForResult();
            }
        }else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
            startCaptureActivityForResult();
        }

    }
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            ShowUtils.showCustomToast(getApplicationContext(), "需要相机权限才能使用该功能！");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CaptureActivity.REQ_CODE){
            if (resultCode == RESULT_OK){
                et_label.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }else if (resultCode == RESULT_CANCELED){
                et_label.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }
        }
    }
}
