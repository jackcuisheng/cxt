package com.cxt.gps.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    protected int resource;
    protected List<HashMap<String, String>> list;

    public CarListAdapter(Context context, int resource, List<HashMap<String, String>> list){
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if (convertView == null) {

            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_car_manage_item, null);
            holder.license_plate = convertView.findViewById(R.id.license_plate);
            holder.date = convertView.findViewById(R.id.date);
            holder.location = convertView.findViewById(R.id.location);
            holder.risk = convertView.findViewById(R.id.risk);
            holder.location_type = convertView.findViewById(R.id.tv_location_type);
            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }


        holder.license_plate.setText(list.get(position).get("carNum").toString());
        holder.location.setText(list.get(position).get("location").toString());
        holder.date.setText(list.get(position).get("date").toString());
        String type = list.get(position).get("positioningType").toString();
        String locationType = "";
        if (type.equals("010")){
            locationType = "基站";
        }else if(type.equals("100")){
            locationType = "卫星";
        }else if(type.equals("001")){
            locationType = "WIFI";
        }else if(type.equals("000")){
            locationType = "暂无定位";
        }else if(type.equals("110")){
            locationType = "卫星、基站";
        }else if(type.equals("101")){
            locationType = "卫星、wifi";
        }else if(type.equals("011")){
            locationType = "基站、wifi";
        }else if (type.equals("111")){
            locationType = "卫星、基站、wifi";
        }else if(type.isEmpty()){
            locationType = "无设备或故障";
        }
        holder.location_type.setText(locationType);
        String result = list.get(position).get("risk").toString();
        if (result.equals("1")){
            holder.risk.setImageResource(R.mipmap.green_point);
        }else if (result.equals("2")){
            holder.risk.setImageResource(R.mipmap.orange_point);
        }else {
            holder.risk.setImageResource(R.mipmap.red_point);
        }


        return convertView;
    }
    class ViewHolder{
        public TextView license_plate;
        public TextView date;
        public TextView location;
        public ImageView risk;
        public TextView location_type;
    }
}

