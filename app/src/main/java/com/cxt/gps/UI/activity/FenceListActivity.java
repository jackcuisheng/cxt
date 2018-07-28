package com.cxt.gps.UI.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.cxt.gps.R;
import com.cxt.gps.presenter.FenceListPresenter;
import com.cxt.gps.util.PositionUtil;
import com.cxt.gps.view.IFenceListView;
import com.iflytek.cloud.thirdparty.V;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class FenceListActivity extends SuperActivity implements IFenceListView{
    private TextView tv_title;
    private TextView tv_carNum;
    private ImageView iv_back;
    private ListView lv_list;
    private TextView tv_notice;
    private SharedPreferences sharedPreferences;
    private String account,carNum;
    private FenceListPresenter presenter = new FenceListPresenter(this);
    private FenceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_list);
        initView();
        init();
        initEvents();

    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_carNum.setText(carNum);
        tv_title.setText(getResources().getString(R.string.fence_list));
        presenter.loadFenceList("REQUEST_ALL_FENCE",account,carNum,"","");
    }
    private void initView() {
        tv_carNum = findViewById(R.id.tv_carNum);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        lv_list = findViewById(R.id.lv_list);
        tv_notice = findViewById(R.id.tv_notice);

    }

    @Override
    public void toShowFenceListActivity(List<HashMap<String, String>> list) {
        try {
            lv_list.setVisibility(View.VISIBLE);
            tv_notice.setVisibility(View.GONE);
            adapter = new FenceListAdapter(this,R.layout.activity_fence_list_item,list);
            lv_list.setAdapter(adapter);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {
        tv_notice.setVisibility(View.VISIBLE);
        lv_list.setVisibility(View.GONE);
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
    public void showEditResult(Map<String, String> map) {
        try{
            if (map.get("state").equals("1")){
                showCustomToast(getApplicationContext(),"删除围栏成功！");
                presenter.loadFenceList("REQUEST_ALL_FENCE",account,carNum,"","");
            }else if(map.get("state").equals("0")){
                showCustomToast(getApplicationContext(),map.get("msg"));
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    class ViewHolder{
        public TextView tv_fence_name;
        public TextView tv_radio;
        public TextView address;
        public TextView edit;
        public TextView remove;
        public ImageView item_fence;
    }
   class FenceListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        protected int resource;
        protected List<HashMap<String, String>> list;

        public FenceListAdapter(Context context, int resource, List<HashMap<String, String>> list){
            this.mInflater = LayoutInflater.from(context);
            this.resource = resource;
            this.list=list;
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

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(resource, null);
                holder.tv_fence_name = convertView.findViewById(R.id.tv_fence_name);
                holder.tv_radio = convertView.findViewById(R.id.tv_radio);
                holder.address = convertView.findViewById(R.id.address);
                holder.edit = convertView.findViewById(R.id.edit);
                holder.remove = convertView.findViewById(R.id.remove);
                holder.item_fence = convertView.findViewById(R.id.item_fence);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder) convertView.getTag();
            }
            final String fenceID = list.get(position).get("fenceId").toString();
            final String name = list.get(position).get("fenceName");
            final String radius = list.get(position).get("fenceRadius");
            final String condition = list.get(position).get("triggerCondition");
            String address = list.get(position).get("location");
            holder.tv_fence_name.setText(name);
            holder.tv_radio.setText(radius);
            holder.address.setText(address);
            final ViewHolder finalHolder = holder;
            holder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                  Intent intent = new Intent(FenceListActivity.this,UpdateFenceActivity.class);
                  intent.putExtra("carNum",carNum);
                  intent.putExtra("fenceId",fenceID);
                  intent.putExtra("fenceName",name);
                  intent.putExtra("condition",condition);
                  intent.putExtra("radius",radius);
                  startActivity(intent);
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    presenter.removeFence("REQUEST_DELETE_FENCE_BY_ID",account,fenceID);
                }
            });
            holder.item_fence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FenceListItemActivity.class);
                    intent.putExtra("carNum",carNum);
                    intent.putExtra("latitude", list.get(position).get("centerLat"));
                    intent.putExtra("longitude", list.get(position).get("centerLng"));
                    intent.putExtra("radius", list.get(position).get("fenceRadius"));
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

}
