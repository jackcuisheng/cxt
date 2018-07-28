package com.cxt.gps.UI.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.cxt.gps.R;
import com.cxt.gps.presenter.AddDevicePresenter;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.view.IAddDeviceView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import timber.log.Timber;

public class AddDeviceActivity extends SuperActivity implements IAddDeviceView{
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_vin;
    private TextView tv_carNum;
    private EditText et_deviceID;
    private ImageView iv_scanner;
    private TextView tv_time;
    private TextView tv_status;
    private Button bt_next;
    private TextView tv_show_state;
    private ImageView iv_show_state_img;
    private SharedPreferences sharedPreferences;
    private String account;
    private AddDevicePresenter presenter = new AddDevicePresenter(this);
    private static final int REQUEST_CODE = 111;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    boolean flag = false;
    private LinearLayout ll_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        tv_carNum = findViewById(R.id.tv_carNum);
        iv_back = findViewById(R.id.iv_back);
        tv_vin = findViewById(R.id.tv_vin);
        et_deviceID = findViewById(R.id.et_deviceID);
        iv_scanner = findViewById(R.id.iv_scanner);
        tv_time = findViewById(R.id.tv_time);
        tv_status = findViewById(R.id.tv_status);
        tv_show_state = findViewById(R.id.tv_show_state);
        iv_show_state_img = findViewById(R.id.iv_show_state_image);
        bt_next = findViewById(R.id.bt_next);
        ll_all = findViewById(R.id.all);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.add_device));
        tv_vin.setText(getIntent().getStringExtra("vin"));
        tv_carNum.setText(getIntent().getStringExtra("carNum"));
    }
    public void initEvents(){
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
        et_deviceID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String deviceID = et_deviceID.getText().toString();
                    presenter.checkDeviceState("REQUEST_DEVICE_INFO",account,deviceID);
                }
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_deviceID.clearFocus();
                if (flag){
                    presenter.bindDevice("REQUEST_UPLOAD_BIND_DEVICE_INFO",account,
                            getIntent().getStringExtra("vin"),getIntent().getStringExtra("carNum"),et_deviceID.getText().toString());
                }
            }
        });
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_deviceID.clearFocus();
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
    public void toMainActivity(Map<String,String> map) {
        try{
            if (map.get("result").equals("0")){
                ShowUtils.showCustomToast(this,map.get("error"));
            }else if (map.get("result").equals("0")){
                ShowUtils.showCustomToast(this,"绑定成功！");
            }
            Intent intent = new Intent(this,DeviceListActivity.class);
            intent.putExtra("vin",getIntent().getStringExtra("vin"));
            intent.putExtra("carNum",getIntent().getStringExtra("carNum"));
            startActivity(intent);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void toShowDeviceStatus(Map<String, String> map) {
        try{
            String lastTime = map.get("lastLocateTime");
            if (lastTime.isEmpty()){
                tv_show_state.setText("设备不存在！");
                iv_show_state_img.setImageResource(R.drawable.error);
            }else {
                Date time = sdf.parse(lastTime);
                Date now = new Date();
                if (now.getTime() - time.getTime() < 2 * 24 * 60 * 60 * 1000) {
                    tv_time.setText(lastTime);
                    tv_time.setTextColor(Color.GREEN);
                    tv_status.setText("设备正常");
                    tv_status.setTextColor(Color.GREEN);
                    tv_show_state.setText("设备启用正常");
                    iv_show_state_img.setImageResource(R.drawable.ok);
                    bt_next.setBackgroundColor(getResources().getColor(R.color.top_layout));
                    flag = true;
                } else {
                    tv_time.setText(lastTime);
                    tv_time.setTextColor(Color.RED);
                    tv_status.setText("设备故障");
                    tv_show_state.setText("设备启用异常");
                    iv_show_state_img.setImageResource(R.drawable.error);
                }
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CaptureActivity.REQ_CODE){
            if (resultCode == RESULT_OK){
                et_deviceID.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }else if (resultCode == RESULT_CANCELED){
                et_deviceID.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }
        }
    }
}
