package com.cxt.gps.UI.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.CarManagePresenter;
import com.cxt.gps.util.CarListAdapter;
import com.cxt.gps.view.ICarManageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class CarManageActivity extends SuperActivity implements ICarManageView,CompoundButton.OnCheckedChangeListener{
    private SearchView sv_query;
    private SmartRefreshLayout refreshLayout;
    private ListView lv_list;
    public  CarListAdapter adapter;
    private ImageView iv_back;
    private TextView tv_title;
    //加载更多

    private int flag = 1;
    private boolean judge = false;
    private int count;
    private TextView tv_notice;
    private CarManagePresenter carManagePresenter = new CarManagePresenter(this);
    private SharedPreferences sharePreference;
    private int pageNum = 1;
    private String account;
    //布控、设防
    private CheckBox cb_all;
    private CheckBox cb_control;
    private CheckBox cb_defend;
    private int index = 1;
    //存放加载更多数据
    List<HashMap<String, String>> loadMorelist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_car_manage);
        initViews();
        init();
        initEvents();


    }
    protected void initViews() {
        sv_query = findViewById(R.id.sv_query);
        refreshLayout = findViewById(R.id.refreshLayout);
        lv_list = findViewById(R.id.lv_list);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_notice = findViewById(R.id.tv_notice);
        cb_all = findViewById(R.id.cb_all);
        cb_control = findViewById(R.id.cb_control);
        cb_defend = findViewById(R.id.cb_defend);
    }
    protected void initEvents() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                try {
                    switch (index){
                        case 1:
                            init();
                            break;
                        case 2:
                            carManagePresenter.getRiskCarData("REQUEST_CAR_BE_CONTROLED",account,1,10);
                            break;
                        case 3:
                            carManagePresenter.getRiskCarData("REQUEST_DEFENDED_CAR_INFO",account,1,10);
                            break;
                    }
                    refreshlayout.finishRefresh(1000/*,false*/);
                }catch (Exception e){
                    Timber.d(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                try {
                        flag++;
                    if (count % 10 == 0){
                        judge=true;
                    }else {
                        judge=false;
                    }
                    int page_num = 0;
                    if (judge){
                        page_num = count / 10;
                    }else {
                        page_num = count / 10 + 1;
                    }
                    if (flag <= page_num) {
                        switch (index){
                            case 1:
                                carManagePresenter.getInitData("",account,flag);
                                break;
                            case 2:
                                carManagePresenter.getRiskCarData("REQUEST_CAR_BE_CONTROLED",account,flag,10);
                                break;
                            case 3:
                                carManagePresenter.getRiskCarData("REQUEST_DEFENDED_CAR_INFO",account,flag,10);
                                break;
                        }
                    }
                    refreshlayout.finishLoadmore(1000);
                }catch (Exception e){
                    Timber.d(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                carManagePresenter.getInitData(query,account,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carManagePresenter.getInitData(newText,account,1);
                return true;
            }
        });
        cb_control.setOnCheckedChangeListener(this);
        cb_all.setOnCheckedChangeListener(this);
        cb_defend.setOnCheckedChangeListener(this);
    }

    protected void init() {
        loadMorelist.clear();
        sharePreference = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharePreference.getString("account","");
        tv_title.setText(getResources().getString(R.string.car_manage));
        carManagePresenter.getInitData("",account,pageNum);
        cb_all.setChecked(true);

    }

    @Override
    public void toShowCarsActivity(final List<HashMap<String, String>> list) {
        try {
            if (list.size() == 1 && (TextUtils.isEmpty(list.get(0).get("count").toString()) || list.get(0).get("count").equals("0"))){
                lv_list.setVisibility(View.GONE);
                tv_notice.setVisibility(View.VISIBLE);
            }else {
                lv_list.setVisibility(View.VISIBLE);
                tv_notice.setVisibility(View.GONE);
                count = Integer.parseInt(list.get(0).get("count"));
                tv_title.setText(getResources().getString(R.string.car_manage)+"("+count+")");
                if (!loadMorelist.containsAll(list)){
                    loadMorelist.addAll(list);
                }
                if (adapter == null){
                    adapter = new CarListAdapter(this, R.layout.activity_car_manage_item, loadMorelist);
                    lv_list.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        String positionType = "";
                        if (loadMorelist.get(position).get("positioningType") == null){
                            positionType = "";
                        }else {
                            positionType = loadMorelist.get(position).get("positioningType");
                        }
                        if (positionType.isEmpty()){
                            new AlertDialog.Builder(CarManageActivity.this).setMessage("\t暂无设备绑定，需要立即绑定设备么？").setTitle("温馨提示")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Bundle data = new Bundle();
                                            data.putString("carNum",loadMorelist.get(position).get("carNum").toString());
                                            Intent intent = new Intent(CarManageActivity.this, DeviceListActivity.class);
                                            intent.putExtras(data);
                                            startActivity(intent);
                                        }
                                    }).create().show();
                        }else {
                            String carNum = loadMorelist.get(position).get("carNum").toString();
                            Bundle data = new Bundle();
                            data.putString("carNum", carNum);
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    }
                });

            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void toShowRiskCar(final List<HashMap<String, String>> list) {
        try {
            if (list.size() == 1 && (TextUtils.isEmpty(list.get(0).get("count").toString()) || list.get(0).get("count").equals("0"))){
                lv_list.setVisibility(View.GONE);
                tv_notice.setVisibility(View.VISIBLE);
            }else {
                lv_list.setVisibility(View.VISIBLE);
                tv_notice.setVisibility(View.GONE);
                adapter = new CarListAdapter(this, R.layout.activity_car_manage_item, list);
                lv_list.setAdapter(adapter);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String carNum = list.get(position).get("carNum").toString();
                        Bundle data = new Bundle();
                        data.putString("carNum", carNum);
                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                });
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
        showLoadingDialog("加载中...");
    }
    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            switch (buttonView.getId()) {
                case R.id.cb_all:
                    //重置加载更多页码数
                    flag = 1;
                    cb_defend.setChecked(false);
                    cb_control.setChecked(false);
                    carManagePresenter.getInitData("",account,pageNum);
                    index = 1;
                    break;
                case R.id.cb_control:
                    //重置加载更多页码数
                    flag = 1;
                    cb_defend.setChecked(false);
                    cb_all.setChecked(false);
                    carManagePresenter.getRiskCarData("REQUEST_CAR_BE_CONTROLED",account,1,10);
                    index = 2;
                    break;
                case R.id.cb_defend:
                    //重置加载更多页码数
                    flag = 1;
                    cb_all.setChecked(false);
                    cb_control.setChecked(false);
                    carManagePresenter.getRiskCarData("REQUEST_DEFENDED_CAR_INFO",account,1,10);
                    index = 3;
                    break;
            }
        }
    }
}
