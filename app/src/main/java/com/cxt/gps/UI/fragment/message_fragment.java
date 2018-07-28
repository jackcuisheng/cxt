package com.cxt.gps.UI.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.MapActivity;
import com.cxt.gps.presenter.MessageManagePresenter;
import com.cxt.gps.view.IMessageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class message_fragment extends SuperFragment implements IMessageView{
    private TextView tv_title;
    private ImageView iv_back;
    private SearchView sv_query;
    private ListView lv_list;
    private TextView tv_notice;
    private SmartRefreshLayout refreshLayout;
    private MessageManagePresenter presenter = new MessageManagePresenter(this);
    private SharedPreferences sharedPreferences;
    private String account;
    private String count;
    private int countNum;
    private MessageAdapter adapter;
    private int flag = 1;
    private boolean judge = false;
    private List<HashMap<String,String>> showList = new ArrayList<>();
    private List<HashMap<String,String>> loadMorelist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initViews() {
        tv_title = mView.findViewById(R.id.tv_title);
        iv_back = mView.findViewById(R.id.iv_back);
        tv_notice = mView.findViewById(R.id.tv_notice);
        sv_query = mView.findViewById(R.id.sv_query);
        lv_list = mView.findViewById(R.id.lv_list);
        refreshLayout = mView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initEvents() {
        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.query("REQUEST_ALARM_MSG",account,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.query("REQUEST_ALARM_MSG",account,newText);
                return true;
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                init();
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                flag++;
                if (countNum % 10 == 0){
                    judge=true;
                }else {
                    judge=false;
                }
                int pageNum = 0;
                if (judge){
                    pageNum = countNum / 10;
                }else {
                    pageNum = countNum / 10 + 1;
                }
                if (flag <= pageNum) {
                    presenter.loadDataList(account,"alarmMsgs",flag,10);
                    refreshLayout.finishLoadmore();
                }

            }
        });
    }

    @Override
    protected void init() {
        loadMorelist.clear();
        sharedPreferences = getActivity().getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        iv_back.setVisibility(View.INVISIBLE);
        tv_title.setText(getResources().getString(R.string.message_list));
        presenter.loadDataList(account,"alarmMsgs",1,10);
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
                count = list.get(0).get("count");
                countNum = Integer.parseInt(count);
                tv_title.setText(getResources().getString(R.string.message_list)+"("+count+")");
                if (!loadMorelist.containsAll(list)){
                    loadMorelist.addAll(list);
                }
               // showList = list;
                if (adapter == null){
                    adapter = new MessageAdapter(getActivity(),R.layout.message_list_item,loadMorelist);
                    lv_list.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }
               lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Intent intent = new Intent(getContext(), MapActivity.class);
                       intent.putExtra("carNum",loadMorelist.get(position).get("carNum"));
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
    public void toShowQueryListActivity(final List<HashMap<String, String>> list) {
        try{
            if (list.size() == 1 && (TextUtils.isEmpty(list.get(0).get("count").toString()) || list.get(0).get("count").equals("0"))){
                lv_list.setVisibility(View.GONE);
                tv_notice.setVisibility(View.VISIBLE);
            }else {
                lv_list.setVisibility(View.VISIBLE);
                tv_notice.setVisibility(View.GONE);
                adapter = new MessageAdapter(getActivity(),R.layout.message_list_item,list);
                lv_list.setAdapter(adapter);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), MapActivity.class);
                        intent.putExtra("carNum",list.get(position).get("carNum"));
                        startActivity(intent);
                    }
                });

             }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }

    class ViewHolder{
        public ImageView iv_image;
        public TextView tv_name;
        public TextView tv_carNum;
        public TextView tv_time;
        public TextView tv_type;
    }

    class MessageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        protected int resource;
        protected List<HashMap<String, String>> list;


        public MessageAdapter(Context context, int resource, List<HashMap<String, String>> list){
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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(resource, null);
                holder.iv_image = convertView.findViewById(R.id.iv_image);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_carNum = convertView.findViewById(R.id.tv_carNum);
                holder.tv_time = convertView.findViewById(R.id.tv_time);
                holder.tv_type = convertView.findViewById(R.id.tv_type);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }
            String name,carNum,time,msgType;
            if (list.get(position).get("name") == null){
                name = "车主一";
            }else {
                name = list.get(position).get("name").toString();
            }
            holder.tv_name.setText(name);
            if (list.get(position).get("carNum")==null){
                carNum = "";
            }else {
                carNum = list.get(position).get("carNum").toString();
            }
            holder.tv_carNum.setText(carNum);
            if (list.get(position).get("time")==null){
                time = "";
            }else {
                time = list.get(position).get("time").toString();
            }
            holder.tv_time.setText(time);
            if (list.get(position).get("msg").toString()==null){
                msgType = "";
            }else {
                msgType = list.get(position).get("msg").toString();
            }
            holder.tv_type.setText(msgType);
            if(msgType.contains("拆机")){
                holder.iv_image.setImageResource(R.drawable.type_chai);
            }else if(msgType.contains("款")){
                holder.iv_image.setImageResource(R.drawable.type_kuan);
            }else if(msgType.contains("低电")){
                holder.iv_image.setImageResource(R.drawable.type_didian);
            }else if(msgType.contains("离线")){
                holder.iv_image.setImageResource(R.drawable.type_lixian);
            }else if(msgType.contains("抵押") ){
                holder.iv_image.setImageResource(R.drawable.type_diya);
            }else {
                holder.iv_image.setImageResource(R.drawable.type_jing);
            }
            return convertView;
        }

    }
}
