package com.cxt.gps.UI.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.cxt.gps.R;
import com.cxt.gps.UI.MainActivity;
import com.cxt.gps.util.SharePreferenceUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlashActivity extends Activity{

    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        final SharePreferenceUtil preferenceUtil = new SharePreferenceUtil(getApplicationContext(),"first_pref");
        final SharedPreferences sharedPreferences = getSharedPreferences("remember_password", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceUtil.getisFirst()){
                    intent = new Intent(getApplicationContext(),GuideActivity.class);
                }else {
                    String name = sharedPreferences.getString("username","");
                    String password = sharedPreferences.getString("password","");
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){

                        try {
                            SharePreferenceUtil util = new SharePreferenceUtil(getApplicationContext(),"login_logout_time");
                            String dateStr = util.getUserInfo("lastLoginTime");
                            if (TextUtils.isEmpty(dateStr)){
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                            }else {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Log.i("last_logout_time",dateStr);
                                if (new Date().getTime() - sdf.parse(dateStr).getTime() > 15 * 24 * 60 * 60 * 1000) {
                                    intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                                } else {
                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else{
                        intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                    }
                }
                getApplicationContext().startActivity(intent);
                finish();
            }
        },2000);
    }

}
