package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.RiskControlPresenter;
import com.cxt.gps.view.IRiskControlView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class RiskControlActivity extends SuperActivity implements IRiskControlView,View.OnClickListener{
    private SearchView sv_query;
    private TextView tv_notice;
    private TextView tv_title;
    private ImageView iv_back;
    private ListView lv_list;
    private TextView tv_yq;
    private TextView tv_cc;
    private TextView tv_lx;
    private TextView tv_mg;
    private SmartRefreshLayout refreshLayout;
    private RiskControlPresenter riskControlPresenter = new RiskControlPresenter(this);
    private SharedPreferences sharedPreferences;
    private String account;
    private SimpleAdapter adapter;
    private int index = 3;
    private int flag = 1;
    private boolean judge = false;
    private int count;
    //存放加载更多数据
    List<HashMap<String, String>> loadMorelist;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_risk_control);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        sv_query = findViewById(R.id.sv_query);
        tv_notice = findViewById(R.id.tv_notice);
        lv_list = findViewById(R.id.lv_list);
        refreshLayout = findViewById(R.id.refreshLayout);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_yq = findViewById(R.id.tv_tab1);
        tv_cc = findViewById(R.id.tv_tab2);
        tv_lx = findViewById(R.id.tv_tab3);
        tv_mg = findViewById(R.id.tv_tab4);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.risk_control));
        tv_yq.setText(getResources().getString(R.string.tab_risk_control_yq));
        tv_cc.setText(getResources().getString(R.string.tab_risk_control_cc));
        tv_lx.setText(getResources().getString(R.string.tab_risk_control_lx));
        tv_mg.setText(getResources().getString(R.string.tab_risk_control_mg));
        tv_yq.setBackgroundColor(getResources().getColor(R.color.top_layout));
        riskControlPresenter.initData("REQUEST_RISK_CONTROL_NUM",account);
        riskControlPresenter.loadListData(account,"",1,"overdueAlarm");
    }
    public void initEvents(){
        iv_back.setOnClickListener(this);
        tv_yq.setOnClickListener(this);
        tv_cc.setOnClickListener(this);
        tv_lx.setOnClickListener(this);
        tv_mg.setOnClickListener(this);

        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                riskControlPresenter.queryData("REQUEST_RISK_CONTROL",index,account,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                riskControlPresenter.queryData("REQUEST_RISK_CONTROL",index,account,newText);
                return true;
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                switch (index){
                    case 1:
                        riskControlPresenter.loadListData(account,"",flag,"overdueAlarm");
                        break;
                    case 2:
                        riskControlPresenter.loadListData(account,"",flag,"dismantleAlarm");
                        break;
                    case 3:
                        riskControlPresenter.loadListData(account,"",flag,"offlineAlarm");
                        break;
                    case 4:
                        riskControlPresenter.loadListData(account,"",flag,"mortgageAlarm");
                        break;
                }
                refreshLayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
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
                            riskControlPresenter.loadListData(account,"",flag,"overdueAlarm");
                            break;
                        case 2:
                            riskControlPresenter.loadListData(account,"",flag,"dismantleAlarm");
                            break;
                        case 3:
                            riskControlPresenter.loadListData(account,"",flag,"offlineAlarm");
                            break;
                        case 4:
                            riskControlPresenter.loadListData(account,"",flag,"mortgageAlarm");
                            break;
                    }
                    refreshlayout.finishLoadmore(1000);
                }

            }
        });

    }
    @Override
    public void onClick(View v) {
        resetBackGround(v);
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.tv_tab1:
                index = 3;
                loadMorelist = new ArrayList<>();
                riskControlPresenter.loadListData(account,"",1,"overdueAlarm");
                break;
            case R.id.tv_tab2:
                index = 256;
                loadMorelist = new ArrayList<>();
                riskControlPresenter.loadListData(account,"",1,"dismantleAlarm");
                break;
            case R.id.tv_tab3:
                index = 32;
                loadMorelist = new ArrayList<>();
                riskControlPresenter.loadListData(account,"",1,"offlineAlarm");
                break;
            case R.id.tv_tab4:
                index = 16384;
                loadMorelist = new ArrayList<>();
                riskControlPresenter.loadListData(account,"",1,"mortgageAlarm");
                break;
        }

    }
    public void resetBackGround(View v){
        tv_yq.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_cc.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_lx.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_mg.setBackground(getResources().getDrawable(R.drawable.view_bg));
        v.setBackgroundColor(getResources().getColor(R.color.top_layout));
    }

    @Override
    public void toShowListActivity(final List<HashMap<String, String>> list) {
        try{
            if (list.size() == 1 && (TextUtils.isEmpty(list.get(0).get("count").toString()) || list.get(0).get("count").equals("0"))){
                lv_list.setVisibility(View.GONE);
                tv_notice.setVisibility(View.VISIBLE);
            }else {
                lv_list.setVisibility(View.VISIBLE);
                tv_notice.setVisibility(View.GONE);
                if (!loadMorelist.containsAll(list)){
                    loadMorelist.addAll(list);
                }
                    adapter = new SimpleAdapter(this, list,
                            R.layout.risk_control_list_item,
                            new String[]{"carNum", "time"},
                            new int[]{R.id.tv_carNum, R.id.tv_time});
                    lv_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                        intent.putExtra("carNum",list.get(position).get("carNum"));
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
    public void toActivityShowResult(String result) {

    }

    @Override
    public void toShowDataNumActivity(List<HashMap<String, String>> list) {
        try{
            tv_yq.setText(tv_yq.getText().toString()+"("+list.get(0).get("yq_num")+")");
            tv_cc.setText(tv_cc.getText().toString()+"("+list.get(0).get("cc_num")+")");
            tv_lx.setText(tv_lx.getText().toString()+"("+list.get(0).get("lx_num")+")");
            tv_mg.setText(tv_mg.getText().toString()+"("+list.get(0).get("mg_num")+")");
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
}
