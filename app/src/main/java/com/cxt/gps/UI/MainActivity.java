package com.cxt.gps.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.SuperActivity;
import com.cxt.gps.UI.fragment.home_fragment;
import com.cxt.gps.UI.fragment.message_fragment;
import com.cxt.gps.UI.fragment.search_fragment;
import com.cxt.gps.UI.fragment.user_fragment;
import com.cxt.gps.util.SharePreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends SuperActivity implements View.OnClickListener{
    private LinearLayout ll_home;
    private LinearLayout ll_search;
    private LinearLayout ll_message;
    private LinearLayout ll_user;
    private ImageView iv_home;
    private ImageView iv_search;
    private ImageView iv_message;
    private ImageView iv_user;

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //定义时间
    private long exitTime = 0;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initViews();
        initEvents();
        init();
    }
    protected void initViews() {
        ll_home = findViewById(R.id.ll_home);
        ll_search = findViewById(R.id.ll_search);
        ll_message = findViewById(R.id.ll_message);
        ll_user = findViewById(R.id.ll_user);
        iv_home = findViewById(R.id.iv_home);
        iv_search = findViewById(R.id.iv_search);
        iv_message = findViewById(R.id.iv_message);
        iv_user = findViewById(R.id.iv_user);
        mViewPager = findViewById(R.id.vp_container);
    }

    protected void initEvents() {
        ll_home.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    protected void init() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new home_fragment());
        mFragments.add(new search_fragment());
        mFragments.add(new message_fragment());
        mFragments.add(new user_fragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImage();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        resetImage();
        switch (v.getId()){
            case R.id.ll_home:
                selectTab(0);
                break;
            case R.id.ll_search:
                selectTab(1);
                break;
            case R.id.ll_message:
                selectTab(2);
                break;
            case R.id.ll_user:
                selectTab(3);
                break;
            default:
                break;

        }
    }
    public void resetImage(){
        iv_home.setImageResource(R.mipmap.home);
        iv_search.setImageResource(R.mipmap.search);
        iv_message.setImageResource(R.mipmap.message);
        iv_user.setImageResource(R.mipmap.user);
    }
    public void selectTab(int index){
        switch (index){
            case 0:
                iv_home.setImageResource(R.mipmap.home_press);
                break;
            case 1:
                iv_search.setImageResource(R.mipmap.search_press);
                break;
            case 2:
                iv_message.setImageResource(R.mipmap.message_press);
                break;
            case 3:
                iv_user.setImageResource(R.mipmap.user_press);
                break;
            default:
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(index);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            SharePreferenceUtil util = new SharePreferenceUtil(context,"login_logout_time");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            util.setUserInfo("lastLoginTime",sdf.format(new Date()));
            Intent intent = new Intent();
            intent.setAction(SuperActivity.SYSTEM_EXIT);
            sendBroadcast(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
