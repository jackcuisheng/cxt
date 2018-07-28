package com.cxt.gps.UI.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.navigate.util.mapUtil.ToastUtil;
import com.cxt.gps.util.Code;

public class ForgetPasswordActivity extends SuperActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_code;
    private ImageView iv_code;
    private Button btn_next;
    private Bitmap bitmap_code;
    private String num;
    private String code;
    private String name;
    public static final String PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0,5-9])|(17[0-9]))\\d{8}$";
    private boolean flag = false;
    private boolean isTrue = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        init();
        initEvents();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        et_code = findViewById(R.id.et_code);
        et_name = findViewById(R.id.et_account);
        et_phone = findViewById(R.id.et_phone);
        iv_code = findViewById(R.id.iv_code);
        btn_next = findViewById(R.id.btn_next);
    }
    private void init(){
        tv_title.setText(getResources().getString(R.string.find_password));
        bitmap_code = Code.getInstance().createBitmap();
        iv_code.setImageBitmap(bitmap_code);
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap_code = Code.getInstance().createBitmap();
                iv_code.setImageBitmap(bitmap_code);
            }
        });
//        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus){
//                    if (et_phone.getText().toString().matches(PHONE_PATTERN)){
//                        flag = true;
//                    }else {
//                        flag = false;
//                        showCustomToast(getApplicationContext(),"手机号格式不对！");
//                    }
//                }
//            }
//        });
//        et_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus){
//                    if (et_code.getText().toString().toString().equals(Code.getInstance().getCode())){
//                        isTrue = true;
//                    }else {
//                        isTrue = false;
//                        showCustomToast(getApplicationContext(),"验证码输入错误！");
//                    }
//                }
//            }
//        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    et_phone.clearFocus();
                    et_code.clearFocus();
                    num = et_phone.getText().toString().trim();
                    name = et_name.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)){
                        if (TextUtils.isEmpty(num)) {
                            showCustomToast(getApplicationContext(), "输入的手机号不能为空");
                        } else {
                            if (num.length() == 11 && num.matches(PHONE_PATTERN)) {
                                code = et_code.getText().toString().trim();
                                if (TextUtils.isEmpty(code)) {
                                    ToastUtil.show(getApplicationContext(), "验证码为空！");
                                } else {
                                    if (code.equalsIgnoreCase(Code.getInstance().getCode())) {
                                        Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                                        Bundle data = new Bundle();
                                        data.putString("username", name);
                                        data.putString("phone", num);
                                        intent.putExtras(data);
                                        startActivity(intent);
                                    } else {
                                        showCustomToast(getApplicationContext(), "验证码错误！");
                                    }
                                }

                            } else {
                                showCustomToast(getApplicationContext(), "输入的手机号格式有误");
                            }
                        }
                    }else {
                        showCustomToast(getApplicationContext(),"请输入你的账号！");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
