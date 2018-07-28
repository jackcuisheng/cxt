package com.cxt.gps.UI.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.cxt.gps.presenter.CarDefendPresenter;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.view.ICarDefendView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class CarDefendActivity extends SuperActivity implements ICarDefendView{
    private TextView tv_title;
    private ImageView iv_back;
    private ListView lv_list;
    private SmartRefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private CarDefendPresenter carDefendPresenter = new CarDefendPresenter(this);
    private String account;
    private TextView tv_notice;
    private CarDefendAdapter adapter;
    private int flag = 1;
    private boolean judge = false;
    private int count;
    List<HashMap<String, String>> loadMoreList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_car_risk);
        initView();
        init();
        initEvents();
    }

    public void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        lv_list = findViewById(R.id.lv_list);
        tv_notice = findViewById(R.id.tv_notice);
        refreshLayout = findViewById(R.id.refreshLayout);
    }
    public void init(){
        loadMoreList.clear();
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.ck_defend));
        carDefendPresenter.loadListData("REQUEST_DEFENDED_CAR_INFO",account,1,10);
    }
    public void initEvents(){
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
                    carDefendPresenter.loadListData("REQUEST_DEFENDED_CAR_INFO",account,flag,10);
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
            tv_title.setText(getResources().getString(R.string.ck_defend)+"("+count+")");
            if (!loadMoreList.containsAll(list)){
                loadMoreList.addAll(list);
            }
            if (adapter == null){
                adapter = new CarDefendAdapter(this,R.layout.car_defend_list_item,loadMoreList);
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
        showLoadingDialog("加载中...");
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void toActivityShowResult(String result) {
        if(result.equals("1")){
            ShowUtils.showCustomToast(this,"解除设防成功！");
            init();
        }else if(result.equals("0")){
            ShowUtils.showCustomToast(this,"解除设防失败，车牌出错或没有！");
        }else if(result.equals("3")){
            ShowUtils.showCustomToast(this,"解除设防失败，该车未设防！");
        }
    }

    @Override
    public void toActivityShowControlResult(String result) {
        if(result.equals("1")){
            ShowUtils.showCustomToast(this,"布控成功！");
            Intent intent=new Intent(this,CarControlActivity.class);
            startActivity(intent);
        }else if(result.equals("4")){
            ShowUtils.showCustomToast(this,"该车已被布控，请勿重复操作！");
        }else {
            ShowUtils.showCustomToast(this,"请求出错");
        }
    }

    class CarDefendAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        protected int resource;
        protected List<HashMap<String, String>> list;
        private Context mContext;

        public CarDefendAdapter(Context context, int resource, List<HashMap<String, String>> list){
            this.mInflater = LayoutInflater.from(context);
            this.resource = resource;
            this.list=list;
            this.mContext = context;
        }
        @Override
        public int getCount() { return list.size(); }
        @Override
        public Object getItem(int position) { return list.get(position); }
        @Override
        public long getItemId(int arg0) { return arg0; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(resource, null);
                holder.tv_carNum = convertView.findViewById(R.id.tv_carNum);
                holder.tv_speed = convertView.findViewById(R.id.tv_speed);
                holder.tv_time = convertView.findViewById(R.id.tv_time);
                holder.tv_because = convertView.findViewById(R.id.tv_reason);
                holder.btn_cancel = convertView.findViewById(R.id.btn_cancel);
                holder.btn_control = convertView.findViewById(R.id.btn_control);
                convertView.setTag(holder);
            }else {

                holder = (ViewHolder)convertView.getTag();
            }
            holder.tv_carNum.setText((String)list.get(position).get("carNum"));
            holder.tv_speed.setText((String)list.get(position).get("speed"));
            holder.tv_time.setText((String)list.get(position).get("time"));
            holder.tv_because.setText((String)list.get(position).get("because"));
            final String carNum = list.get(position).get("carNum");
            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setTitle("解除设防");
                dialog.setMessage("确定解除设防吗");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            carDefendPresenter.cancelDefend("REQUEST_RELEASE_DEFEND",account,carNum);
                        }catch (Exception e){
                            Timber.d(e.getMessage());
                        }

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

                dialog.show();

            }
        });

        holder.btn_control.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                carDefendPresenter.setControl("REQUEST_SET_CAR_CONTROL",account,carNum);
            }
        });
            return convertView;
        }

    }
    class ViewHolder{
        public TextView tv_carNum;
        public TextView tv_speed;
        public TextView tv_time;
        public TextView tv_because;
        public Button btn_cancel;
        public Button btn_control;
    }
}
