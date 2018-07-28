package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.MainActivity;
import com.cxt.gps.presenter.DeviceListPresenter;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.view.IDeviceListView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class DeviceListActivity extends SuperActivity implements IDeviceListView{
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_vin;
    private TextView tv_carNum;
    private ListView lv_list;
    private Button bt_home;
    private Button bt_add;
    private Button bt_add_label;
    private SharedPreferences sharedPreferences;
    private DeviceListPresenter presenter = new DeviceListPresenter(this);
    private String account;
    private DeviceListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_vin = findViewById(R.id.tv_vin);
        tv_carNum = findViewById(R.id.tv_carNum);
        lv_list = findViewById(R.id.lv_list);
        bt_home = findViewById(R.id.bt_home);
        bt_add = findViewById(R.id.bt_add);
        bt_add_label = findViewById(R.id.bt_add_label);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.device_list));
        presenter.loadInItData("REQUEST_BIND_DEVICE_INFO",account,getIntent().getStringExtra("carNum"));
        tv_carNum.setText(getIntent().getStringExtra("carNum"));
        tv_vin.setText(getIntent().getStringExtra("vin"));
    }
    public void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddDeviceActivity.class);
                intent.putExtra("carNum",getIntent().getStringExtra("carNum"));
                intent.putExtra("vin",getIntent().getStringExtra("vin"));
                startActivity(intent);
            }
        });
        bt_add_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddLabelActivity.class);
                intent.putExtra("carNum",getIntent().getStringExtra("carNum"));
                intent.putExtra("vin",getIntent().getStringExtra("vin"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void toMainActivity(List<HashMap<String, String>> list) {
        try{
            if (list.size()==1 && list.get(0).get("deviceID").isEmpty()){
                ShowUtils.showCustomToast(this,"暂无设备安装！");
            }else {
                adapter = new DeviceListAdapter(getApplicationContext(),R.layout.device_list_item,list);
                lv_list.setAdapter(adapter);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

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
    public void toShowDeviceStatus(Map<String, String> map) {

    }
    class ViewHolder{
        public ImageView iv_set;
        public TextView tv_deviceID;
        public TextView tv_type;
        public TextView tv_state;

    }
    public class DeviceListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        protected int resource;
        protected List<HashMap<String, String>> list;


        public DeviceListAdapter(Context context, int resource, List<HashMap<String, String>> list){
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
                holder.tv_deviceID = convertView.findViewById(R.id.tv_deviceID);
                holder.tv_type = convertView.findViewById(R.id.tv_type);
                holder.tv_state = convertView.findViewById(R.id.tv_state);
                holder.iv_set= convertView.findViewById(R.id.iv_setting);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            try {
                holder.tv_deviceID.setText(list.get(position).get("deviceID"));
                String type = list.get(position).get("type");
                if (type.equals("0")){
                    holder.tv_type.setText("有线");
                }else if (type.equals("1")){
                    holder.tv_type.setText("无线");
                }else {
                    holder.tv_type.setText(list.get(position).get("type"));
                }
                holder.tv_state.setText(list.get(position).get("state"));
            }catch (Exception e){
                Timber.d(e.getMessage());
            }

            return convertView;
        }
    }
}
