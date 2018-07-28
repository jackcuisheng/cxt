package com.cxt.gps.UI.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.CarControlPresenter;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.view.ICarControlView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarControlActivity extends SuperActivity implements ICarControlView{
    private TextView tv_title;
    private ImageView iv_back;
    private ListView lv_list;
    private SmartRefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private CarControlPresenter carControlPresenter = new CarControlPresenter(this);
    private String account;
    private TextView tv_notice;
    private CarControlAdapter adapter;
    private int flag = 1;
    private boolean judge = false;
    private int count;
    private Context mContext;
    List<HashMap<String, String>> loadMoreList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_risk);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        lv_list = findViewById(R.id.lv_list);
        tv_notice = findViewById(R.id.tv_notice);
        refreshLayout = findViewById(R.id.refreshLayout);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        mContext = this;
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.ck_control));
        carControlPresenter.loadListData("REQUEST_CAR_BE_CONTROLED",account,1,10);
    }
    public void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                init();
                refreshlayout.autoRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                flag++;
                if (count % 10 == 0) {
                    judge = true;
                } else {
                    judge = false;
                }
                int page_num = 0;
                if (judge) {
                    page_num = count / 10;
                } else {
                    page_num = count / 10 + 1;
                }
                if (flag <= page_num) {
                    carControlPresenter.loadListData("REQUEST_CAR_BE_CONTROLED", account, flag, 10);
                    refreshlayout.autoLoadmore(1000);
                }
            }
        });
    }

    @Override
    public void toActivityShowRisk(List<HashMap<String, String>> list) {
        if (list.size() == 1 && (TextUtils.isEmpty(list.get(0).get("count").toString()) || list.get(0).get("count").equals("0"))){
            lv_list.setVisibility(View.GONE);
            tv_notice.setVisibility(View.VISIBLE);
        }else {
            lv_list.setVisibility(View.VISIBLE);
            tv_notice.setVisibility(View.GONE);
            count = Integer.parseInt(list.get(0).get("count"));
            tv_title.setText(getResources().getString(R.string.ck_control)+"("+count+")");
            if (!loadMoreList.containsAll(list)){
                loadMoreList.addAll(list);
            }
            if (adapter == null){
                adapter = new CarControlAdapter(getApplicationContext(),R.layout.car_control_list_item,list);
                lv_list.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void showLoading() {
        showLoadingDialog("正在加载...");
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void toActivityShowResult(String result) {
        if(result.equals("1")){
            ShowUtils.showCustomToast(this,"解除布控成功！");
            init();
        }else if(result.equals("0")){
            ShowUtils.showCustomToast(this,"解除布控失败,车牌没有或出错！");
        }else if (result.equals("0")){
            ShowUtils.showCustomToast(this,"解除布控失败,该车未布控！");
        }
    }
    class ViewHolder{
        public TextView tv_carNum;
        public TextView tv_name;
        public Button btn_cancel;
        public Button btn_search;
    }


   class CarControlAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        protected int resource;
        protected List<HashMap<String, String>> list;


        public CarControlAdapter(Context context, int resource, List<HashMap<String, String>> list){
            this.mInflater = LayoutInflater.from(context);
            this.resource = resource;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder=null;
            if (convertView == null) {

                holder = new ViewHolder();


                convertView = mInflater.inflate(resource, null);

                holder.tv_carNum = convertView.findViewById(R.id.tv_carNum);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.btn_cancel = convertView.findViewById(R.id.btn_cancel);
                holder.btn_search = convertView.findViewById(R.id.btn_search);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            final String carNum;
            if ( list.get(position).get("carNum") == null ){
                 carNum = "";
            }else {
                 carNum = list.get(position).get("carNum").toString();
            }
            holder.tv_carNum.setText(carNum);
            String name ;
            if (list.get(position).get("name") == null){
               name = "车主一";
            }else {
               name = list.get(position).get("name").toString();
            }
            holder.tv_name.setText(name);

            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("解除布控")
                            .setMessage("确定解除布控吗")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    carControlPresenter.cancelControl("REQUEST_RELEASE_CAR_CONTROL",account,carNum);
                                }
                            }).create().show();

                }
            });

            holder.btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putString("carNum",carNum);
                    Intent intent=new Intent(getApplicationContext(),MapActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            });


            return convertView;
        }

    }
}
