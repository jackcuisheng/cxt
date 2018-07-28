package com.cxt.gps.util;

import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxt.gps.entity.DeviceData;
import com.cxt.gps.entity.GpsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ParseJsonUtil {
    public static List<HashMap<String,String>> getCarData(String jsonStr){
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        try {
            jsonStr = JSON.parseObject(jsonStr).getString("body");
            JSONArray jsonArray = null;
            // 初始化list数组对象
            jsonArray = JSON.parseArray(jsonStr);
            Map<String, String> map;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化map数组对象
                map= new HashMap<>();
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("date", jsonObject.getString("lastLocateTime"));
                map.put("location", jsonObject.getString("carLocation"));
                map.put("risk", jsonObject.getString("valueOfRisk"));
                map.put("carLocation",jsonObject.getString("carLocation"));
                map.put("defendState",jsonObject.getString("defendState"));
                map.put("controlState",jsonObject.getString("controlState"));
                map.put("positioningType",jsonObject.getString("positioningType"));
                if (jsonObject.get("count")==null){
                    map.put("count","0");
                }else {
                    map.put("count", jsonObject.getString("count"));
                }
                list.add((HashMap) map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public static List<HashMap<String,String>> getRiskCarData(String jsonStr){
        // 初始化list数组对象
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try{
            jsonStr = JSON.parseObject(jsonStr).getString("body");
               String ControlCount;
            if (JSON.parseObject(jsonStr).get("msgNum")==null){
                ControlCount = "";
            }else {
                ControlCount = JSON.parseObject(jsonStr).get("msgNum").toString();
            }
            jsonStr = JSON.parseObject(jsonStr).getString("locationBody");
            JSONArray jsonArray = null;
            jsonArray = JSON.parseArray(jsonStr);
            HashMap<String, String> map;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化map数组对象
                map= new HashMap<String, String>();
                map.put("count",ControlCount);
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("positioningType", jsonObject.getString("positioningType"));
                map.put("date", jsonObject.getString("lastLocateTime"));
                map.put("risk", jsonObject.getString("valueOfRisk"));
                map.put("location",jsonObject.getString("carLocation"));
                map.put("speed", jsonObject.getString("carSpeed"));
                map.put("time", jsonObject.getString("setDefendDate"));
                map.put("because", jsonObject.getString("setDefendReason"));
                list.add(map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public static List<HashMap<String,String>> getControlData(String jsonStr){
        // 初始化list数组对象
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try{
            jsonStr = JSON.parseObject(jsonStr).getString("body");
            String ControlCount;
            if (JSON.parseObject(jsonStr).get("msgNum")==null){
                ControlCount = "";
            }else {
                ControlCount = JSON.parseObject(jsonStr).get("msgNum").toString();
            }
            jsonStr = JSON.parseObject(jsonStr).getString("locationBody");
            JSONArray jsonArray = null;
            jsonArray = JSON.parseArray(jsonStr);
            HashMap<String, String> map;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化map数组对象
                map= new HashMap<String, String>();
                map.put("count",ControlCount);
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("name",jsonObject.getString("masterName"));
                list.add(map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public static Map<String,String> getLoginResult(String json){
        Map<String, String> map = null;
        try {
            JSONObject obj = JSON.parseObject(json);
            json = obj.getString("body");
            String account = JSON.parseObject(JSON.toJSONString(obj.get("header"))).get("account").toString();
            JSONArray jsonArray = JSON.parseArray(json);
            String result = JSON.parseObject(JSON.toJSONString(jsonArray.get(0))).get("state").toString();
            map = new HashMap<String, String>();
            map.put("result", result);
            map.put("account", account);

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
    //解析最新定位数据
    public static GpsData getLastLocation(String json){
        GpsData data = new GpsData();
        try {
            JSONObject obj = JSON.parseObject(json);
            json = obj.getString("body");
            data.setVin(JSON.parseObject(json).getString("vinNum"));
            json = JSON.parseObject(json).getString("lastLocation");
            obj = JSON.parseObject(json);
            data.setLatitude(Double.parseDouble(obj.get("latitude").toString()));
            data.setLongitude(Double.parseDouble(obj.get("longitude").toString()));
            data.setSetDenfendStatus(obj.get("setDenfendStatus").toString());
            data.setControlStatus(obj.get("controlStatus").toString());
            data.setLocateMthod(obj.get("locateMthod").toString());
            data.setLocateTime(obj.get("locateTime").toString());
            data.setCurrentLocation(obj.get("currentLocation").toString());
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
       return data;
    }
    //解析设备信息
    public static List<DeviceData> getDeviceData(String json){
        List<DeviceData> list = new ArrayList<>();
        DeviceData data = null;
        try {
            JSONObject obj = JSON.parseObject(json);
            json = obj.getString("body");
            json = JSON.parseObject(json).getString("deviceData");
            JSONArray array = JSONArray.parseArray(json);
            for (int i = 0;i < array.size();i++){
                data = new DeviceData();
                data.setAlarmState(array.getJSONObject(i).getString("alarmState"));
                data.setCarDirection(array.getJSONObject(i).getString("carDirection"));
                data.setCarLat(Double.parseDouble(array.getJSONObject(i).getString("carLat")));
                data.setCarLng(Double.parseDouble(array.getJSONObject(i).getString("carLng")));
                data.setCarSpeed(array.getJSONObject(i).getString("carSpeed"));
                data.setCarState(array.getJSONObject(i).getString("carState"));
                data.setCurrentLocation(array.getJSONObject(i).getString("currentLocation"));
                data.setDeviceBatteryLevel(array.getJSONObject(i).getString("deviceBatteryLevel"));
                data.setLocateTime(array.getJSONObject(i).getString("locateTime"));
                data.setPositioningType(array.getJSONObject(i).getString("positioningType"));
                data.setDeviceID(array.getJSONObject(i).getString("deviceID"));
                list.add(data);
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //获取所有车牌
    public static List<HashMap<String, String>> getCarList(String json){
        List<HashMap<String,String>> list = new ArrayList<>();
        try {
            json = JSON.parseObject(json).getString("body");
            JSONArray jsonArray = null;
            // 初始化list数组对象
            jsonArray = JSON.parseArray(json);
            Map<String,String> map = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                map = new HashMap<>();
                JSONObject obj = jsonArray.getJSONObject(i);
                map.put("carNum",obj.getString("carNum"));
                list.add((HashMap<String, String>) map);
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    //解除设防
    public static String getResult(String json){
        String result = "";
        try {
            json = JSON.parseObject(json).getString("body");
            result = JSON.parseObject(json).get("result").toString();
        }catch (Exception e){
            Timber.d(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //解析风险控制数据
    public static List<HashMap<String,String>> getRiskControl(String response){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            String count = JSON.parseObject(response).getString("msgNum");
            response = JSON.parseObject(response).getString("locationBody");
            JSONArray array = JSON.parseArray(response);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    // 初始化map数组对象
                    map = new HashMap<String, String>();
                    map.put("count",count);
                    map.put("carNum", jsonObject.getString("carNum"));
                    map.put("time", jsonObject.getString("msgTime"));
                    map.put("type", jsonObject.getString("msgType"));
                    list.add(map);
                }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析风险控制查询数据
    public static List<HashMap<String,String>> getRiskControlQuery(String response){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            String count = JSON.parseObject(response).getString("count");
            response = JSON.parseObject(response).getString("Databody");
            JSONArray array = JSON.parseArray(response);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                // 初始化map数组对象
                map = new HashMap<String, String>();
                map.put("count",count);
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("time", jsonObject.getString("msgTime"));
                map.put("type", jsonObject.getString("msgType"));
                list.add(map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析风险控制消息数目
    public static List<HashMap<String,String>> getRiskControlNum(String response){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        try{
            response = JSON.parseObject(response).getString("body");
            map.put("yq_num",JSON.parseObject(response).getString("overdueNum"));
            map.put("cc_num",JSON.parseObject(response).getString("dismantleNum"));
            map.put("lx_num",JSON.parseObject(response).getString("offLineNum"));
            map.put("mg_num",JSON.parseObject(response).getString("mortgageAlarmNum"));
            list.add(map);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析消息返回结果
    public static List<HashMap<String,String>> getAlarmMessageList(String response){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            String count = JSON.parseObject(response).getString("msgNum");
            response = JSON.parseObject(response).getString("locationBody");
            JSONArray array = JSON.parseArray(response);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                // 初始化map数组对象
                map = new HashMap<String, String>();
                map.put("count",count);
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("time", jsonObject.getString("msgTime"));
                map.put("type", jsonObject.getString("msgType"));
                map.put("msg",jsonObject.getString("msg"));
                list.add(map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析风险控制
    public static List<HashMap<String,String>> getRiskControlList(String response){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            String count = JSON.parseObject(response).getString("count");
            response = JSON.parseObject(response).getString("Databody");
            JSONArray array = JSON.parseArray(response);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                // 初始化map数组对象
                map = new HashMap<String, String>();
                map.put("count",count);
                map.put("carNum", jsonObject.getString("carNum"));
                map.put("time", jsonObject.getString("msgTime"));
                map.put("type", jsonObject.getString("msgType"));
                list.add(map);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析设备状态返回数据
    public static Map<String,String> getDeviceState(String response){
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONObject object = JSON.parseObject(response);
            map = new HashMap<>();
            map.put("lastLocateTime",object.getString("lastLocateTime"));
            map.put("lastLocation",object.getString("lastLocation"));
            map.put("latitude",object.getString("latitude"));
            map.put("longitude",object.getString("longitude"));
            map.put("deviceStatus",object.getString("deviceStatus"));

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return map;
    }
    //解析设备绑定结果
    public static Map<String,String> getBindDeviceResult(String response){
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONObject object = JSON.parseObject(response);
            map = new HashMap<>();
            map.put("result",object.getString("result"));
            map.put("error",object.getString("error"));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return map;
    }
    //获取设备列表数据
    public static List<HashMap<String,String>> getDeviceList(String response){
        JSONArray jsonArray = null;
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            // 初始化list数组对象
            response = JSON.parseObject(response).getString("body");
            jsonArray = JSON.parseArray(response);
            HashMap<String, String> map;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化map数组对象
                map = new HashMap<String, String>();
                map.put("deviceID",jsonObject.getString("deviceID"));
                map.put("type",jsonObject.getString("deviceType"));
                map.put("state",jsonObject.getString("deviceStatus"));
                map.put("dataSendTimeInteval",jsonObject.getString("dataSendTimeInteval"));
                list.add(map);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return list;
    }
    //获取WIFI定位数据
    public static List<HashMap<String,String>> getWifiData(String response){
        List<HashMap<String, String>> list = new ArrayList<>();
        try {
            response = JSON.parseObject(response).getString("body");
            response = JSON.parseObject(response).getString("device_sn");
            JSONArray deviceArray = JSONArray.parseArray(response);
            for (int j = 0; j <deviceArray.size();j++ ) {
                response = deviceArray.getJSONObject(j).getString("lastLocation");
                JSONArray array = JSONArray.parseArray(response);
                if (array == null) {
                    return null;
                } else {
                    HashMap<String, String> map = null;
                    for (int i = 0; i < array.size(); i++) {
                        map = new HashMap<>();
                        map.put("latitude", array.getJSONObject(i).getString("latitude"));
                        map.put("longitude", array.getJSONObject(i).getString("longitude"));
                        map.put("location", array.getJSONObject(i).getString("location"));
                        map.put("dateTime", array.getJSONObject(i).getString("dateTime"));
                        list.add(map);
                    }
                }
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析车辆详情数据
    public static Map<String,String> getCarDetailData(String response){
        response = JSON.parseObject(response).getString("body");
        JSONArray array = JSON.parseArray(response);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(array.get(0)));
        Map<String,String> map = new HashMap<>();
        map.put("name", jsonObject.getString("masterName"));
        map.put("phone", jsonObject.getString("masterTel"));
        map.put("money", jsonObject.getString("loanAmount"));
        map.put("months", jsonObject.getString("loanPayPeriods"));
        map.put("repay", jsonObject.getString("periodRepay"));
        map.put("repay_months",jsonObject.getString("repayedPeriods"));
        map.put("alarmState",jsonObject.getString("alarmState"));
        map.put("location",jsonObject.getString("carLocation"));
        JSONObject object = JSON.parseObject(JSON.toJSONString(jsonObject.get("carImage")));
        map.put("image1",object.getString("image1"));
        map.put("image2",object.getString("image2"));
        map.put("image3",object.getString("image3"));
        return map;
    }
    //获取常驻点数据
    public static List<HashMap<String,String>> getResidentPointData(String response){
        List<HashMap<String, String>> list = new ArrayList<>();
        try {
            response = JSON.parseObject(response).getString("body");
            response = JSON.parseObject(response).getString("CZDbody");
            JSONArray array = JSON.parseArray(response);
            HashMap<String,String> map = null;
            for (int i = 0;i < array.size();i++){
                map = new HashMap<>();
                map.put("count",array.getJSONObject(i).getString("count"));
                map.put("latitude",array.getJSONObject(i).getString("latitude"));
                map.put("longitude",array.getJSONObject(i).getString("longitude"));
                map.put("lastLocation",array.getJSONObject(i).getString("lastLocation"));
                list.add(map);
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //获取历史轨迹定位数据
    public static List<HashMap<String,String>> getTrackData(String response){
        List<HashMap<String, String>> list = new ArrayList<>();
        try {
            response = JSON.parseObject(response).getString("body");
            response = JSON.parseObject(response).getString("device_sn");
            JSONArray deviceArray = JSONArray.parseArray(response);
            for (int j = 0; j <deviceArray.size();j++ ) {
                response = deviceArray.getJSONObject(j).getString("lastLocation");
                JSONArray array = JSONArray.parseArray(response);
                if (array == null) {
                    return null;
                } else {
                    HashMap<String, String> map = null;
                    for (int i = 0; i < array.size(); i++) {
                        map = new HashMap<>();
                        map.put("latitude", array.getJSONObject(i).getString("latitude"));
                        map.put("longitude", array.getJSONObject(i).getString("longitude"));
                        map.put("location", array.getJSONObject(i).getString("location"));
                        map.put("dateTime", array.getJSONObject(i).getString("dateTime"));
                        map.put("direction",array.getJSONObject(i).getString("direction"));
                        map.put("speed",array.getJSONObject(i).getString("speed"));
                        list.add(map);
                    }
                }
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return list;
    }
    //解析围栏保存数据
    public static Map<String,String> getSaveFenceData(String response){
        Map<String,String>map = null;
        try{
            map = new HashMap<>();
            response = JSON.parseObject(response).getString("body");
            map.put("state",JSON.parseObject(response).getString("state"));
            map.put("msg",JSON.parseObject(response).getString("msg"));
            map.put("fenceId",JSON.parseObject(response).getString("fenceId"));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

        return map;
    }
    //获取围栏列表数据
    public static List<HashMap<String,String>> getFenceListData(String response){
        List<HashMap<String,String>>list = new ArrayList<>();
        Map<String,String>map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONArray array = JSON.parseArray(response);
            if (array.size() == 1 && array.getJSONObject(0).getString("fenceId").isEmpty()){
                return null;
            }else {
                for (int i = 0; i < array.size(); i++) {
                    map = new HashMap<>();
                    map.put("fenceId", array.getJSONObject(i).getString("fenceId"));
                    map.put("location", array.getJSONObject(i).getString("location"));
                    map.put("fenceName", array.getJSONObject(i).getString("fenceName"));
                    map.put("fenceRadius", array.getJSONObject(i).getString("fenceRadius"));
                    map.put("fenceText", array.getJSONObject(i).getString("fenceText"));
                    map.put("fenceType", array.getJSONObject(i).getString("fenceType"));
                    map.put("centerLng", array.getJSONObject(i).getString("centerLng"));
                    map.put("centerLat", array.getJSONObject(i).getString("centerLat"));
                    map.put("triggerCondition", array.getJSONObject(i).getString("triggerCondition"));
                    list.add((HashMap<String, String>) map);

                }
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
            return null;
        }
        return list;
    }
    //修改围栏结果
    public static Map<String,String> getEditFenceResult(String response){
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONObject object = JSON.parseObject(response);
            map = new HashMap<>();
            map.put("state",object.getString("state"));
            map.put("msg",object.getString("msg"));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return map;
    }
    //设防结果
    public static Map<String,String> getSetDefendResult(String response){
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONObject object = JSON.parseObject(response);
            map = new HashMap<>();
            map.put("result",object.getString("result"));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return map;
    }
    //找回密码结果
    public static Map<String,String> getFindPasswordResult(String response){
        HashMap<String,String> map = null;
        try{
            response = JSON.parseObject(response).getString("body");
            JSONObject object = JSON.parseObject(response);
            map = new HashMap<>();
            map.put("state",object.getString("state"));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
        return map;
    }
}
