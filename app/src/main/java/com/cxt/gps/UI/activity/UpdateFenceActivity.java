package com.cxt.gps.UI.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.UpdateFencePresenter;
import com.cxt.gps.view.IUpdateFenceView;

import java.util.Map;

import timber.log.Timber;


public class UpdateFenceActivity extends SuperActivity implements IUpdateFenceView{
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_carNum;
    private EditText et_name;
    private EditText et_radius;
    private RadioGroup rg_group;
    private RadioButton rb_in;
    private RadioButton rb_out;
    private RadioButton rb_stop;
    private Button btn_save;
    private String condition;
    private SharedPreferences sharedPreferences;
    private String account;
    //进度条
    private ProgressDialog progDialog = null;// 搜索时进度条
    private UpdateFencePresenter presenter = new UpdateFencePresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_edit);
        initView();
        init();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_in:
                        condition = "1";
                        break;
                    case R.id.rb_out:
                        condition = "2";
                        break;
                    case R.id.rb_stop:
                        condition = "3";
                        break;
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_radius.clearFocus();
                et_name.clearFocus();
                String fenceId = getIntent().getStringExtra("fenceId");
                String radius =  et_radius.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                int conditionType = Integer.parseInt(condition);
                if (et_name.getText().toString().isEmpty() || et_radius.getText().toString().isEmpty()){
                    showCustomToast(getApplicationContext(),"围栏名和半径不能为空！");
                }else {
                    presenter.editFence("REQUEST_CHANGE_FENCE", account, fenceId,radius, name, conditionType);
                }
            }
        });
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_carNum = findViewById(R.id.tv_carNum);
        et_name = findViewById(R.id.et_fenceName);
        et_radius = findViewById(R.id.et_radius);
        rg_group = findViewById(R.id.rg_group);
        rb_in = findViewById(R.id.rb_in);
        rb_out = findViewById(R.id.rb_out);
        rb_stop = findViewById(R.id.rb_stop);
        btn_save = findViewById(R.id.btn_save);
    }
    private void init(){
        try{
            sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
            account = sharedPreferences.getString("account","");
            tv_carNum.setText(getIntent().getStringExtra("carNum"));
            tv_title.setText(getResources().getString(R.string.edit_fence));
            et_name.setHint(getIntent().getStringExtra("fenceName"));
            et_radius.setHint(getIntent().getStringExtra("radius"));

            condition = getIntent().getStringExtra("condition");
            if (condition.equals("1")){
                rb_in.setChecked(true);
            }
            if (condition.equals("2")){
                rb_out.setChecked(true);
            }
            if (condition.equals("3")){
                rb_stop.setChecked(true);
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }


    }

    @Override
    public void showEditResult(Map<String, String> map) {
        try{
            if (map.get("state").equals("1")){
                showCustomToast(getApplicationContext(),"修改围栏成功！");
                Intent intent = new Intent(this,FenceListActivity.class);
                intent.putExtra("carNum",getIntent().getStringExtra("carNum"));
                startActivity(intent);
                finish();
            }else if(map.get("state").equals("0")){
                showCustomToast(getApplicationContext(),"修改围栏失败！");
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void showLoading() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("请求中...");
        progDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progDialog != null){
            progDialog.dismiss();
        }
    }
}
