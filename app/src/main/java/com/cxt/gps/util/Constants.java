package com.cxt.gps.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final String GET_BASE = "https://gps.91cxt.com:10443";
    public static final String GET_MESSAGE = "https://gps.91cxt.com:10443/SMSVerification/send_code.php?";
    public static final String GET_DATA_URL = "https://gps.91cxt.com:10443/GPSClass/cxt/com/gps/get_data.php";

    //登录
    public static String getLoginRequest(String msgType,String username,String password){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("account",username);
        body.put("password",password);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //找回密码
    public static String getFindPasswrodRequest(String msgType,String username,String code,String password){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        header.put("account","");
        body.put("userName",username);
        body.put("code",code);
        body.put("newPassword",password);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //车辆管理搜索
    public static String getCarRequest(String msgType,String account,int pageNum,String query){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("pageNum",pageNum);
        body.put("likeCar",query);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //修改密码
    public static String getUpdatePasswordRequest(String msgType, String account, String oldPassword, String newPassword){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("oldPassword",oldPassword);
        body.put("newPassword",newPassword);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }

    //风险控制搜索
    public static String getRiskCarQueryRequest(String msgType,int requestType,String account,int pageNum,String query){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("pageNum",pageNum);
        body.put("likeCar",query);
        body.put("requestType",requestType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }

    public static String getRiskCarRequest(String msgType,String account,int pageNum,int num){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("pageNum",pageNum);
        body.put("num",num);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //寻车界面搜索
    public static String getQueryRequest(String msgType,String account,String carNum){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //解除设防
    //风险控制
    public static String getRiskListDataRequest(String account,String requestType,int pageNum){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType","REQUEST_ALARM_MSG");
        header.put("token","");
        body.put("pageNum",pageNum);
        body.put("num",20);
        body.put("requestType",requestType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }

    //获取风险控制消息数目
    public static String getRiskNumRequest(String msgType,String account){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //报警消息接口
    public static String getAlarmMessageRequest(String account,String requestType,int pageNum,int num){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType","REQUEST_ALARM_MSG");
        header.put("token","");
        body.put("pageNum",pageNum);
        body.put("num",num);
        body.put("requestType",requestType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //报警消息搜索
    public static String getQueryAlarmMessageRequest(String msgType,String account,String query){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("pageNum",1);
        body.put("num",10);
        body.put("likeCar",query);
        body.put("requestType","alarmMsgs");
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //设备安装绑定车辆
    public static String getBindCarRequest(String msgType,String account,String vin,String carNum){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("vinNum",vin);
        body.put("carNum",carNum);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //上传图片
    public static String getUploadImageRequest(String msgType,String account,String vin,int num){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("vinNum",vin);
        body.put("num",num);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //检查设备是否正常
    public static String getCheckDeviceRequest(String msgType,String account,String deviceID){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("deviceID",deviceID);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //绑定设备请求
    public static String getBIndDeviceRequest(String msgType,String account,String vin,String carNum,String deviceID){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("deviceID",deviceID);
        body.put("vinNum",vin);
        body.put("carNum",carNum);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //绑定设备列表请求
    public static String getBIndDeviceListRequest(String msgType,String account,String carNum){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //wifi请求
    public static String getWifiDataRequest(String msgType,String account,String carNum){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        String startTime = sdf.format(calendar.getTime());
        String endTime = sdf.format(new Date());
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("startTime",startTime);
        body.put("endTime",endTime);
        body.put("drviceID","");
        body.put("dayChoice","");
        body.put("spacing","");
        body.put("stopTime","");
        body.put("positioningType","2");
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //常驻点列表请求
    public static String getResidentPointRequest(String msgType,String account,String carNum,String timeType,String locationType){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("weekORmonthORseason",timeType);
        body.put("positioningType",locationType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //获取热力图请求
    public static String getHotPointRequest(String msgType,String account,String carNum,String sTime,String eTime,String timeType,String locationType){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("startTime",sTime);
        body.put("endTime",eTime);
        body.put("weekORmonthORseason",timeType);
        body.put("positioningType",locationType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //获取历史轨迹请求
    public static String getTrackRequest(String msgType, String account, String carNum, String sTime, String eTime, String deviceID, String locationType){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("startTime",sTime);
        body.put("endTime",eTime);
        body.put("drviceID", deviceID);
        body.put("dayChoice", "");
        body.put("spacing", "");
        body.put("stopTime", "");
        body.put("positioningType", locationType);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //保存围栏请求
    public static String getSaveFenceRequest(String msgType,String account,String carNum,String fenceName,String address,double radius,String fenceType,double latitude,double longitude,int condition){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("location",address);
        body.put("fenceName",fenceName);
        body.put("fenceRadius",radius);
        body.put("fenceType",fenceType);
        body.put("centerLng",longitude);
        body.put("centerLat",latitude);
        body.put("leftTopLng","");
        body.put("fenceText","");
        body.put("leftTopLat","");
        body.put("rightBottomLng","");
        body.put("rightBottomLat","");
        body.put("triggerCondition",condition);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //获取围栏列表请求
    public static String getFenceListRequest(String msgType, String account, String carNum, String searchValue, String pageNum){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("searchValue",searchValue);
        body.put("pageNum",pageNum);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //修改围栏请求
    public static String getEditFenceRequest(String msgType, String account, String fenceId, String radius, String fenceName, int condition){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("fenceId",fenceId);
        body.put("fenceRadius",radius);
        body.put("fenceName",fenceName);
        body.put("triggerCondition",condition);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //删除围栏
    public static String getRemoveFenceRequest(String msgType, String account, String fenceId){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("fenceid",fenceId);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }
    //设防请求
    public static String getSetDefendRequest(String msgType, String account, String carNum,String reason){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String checkCode = "";
        header.put("account",account);
        header.put("command_sn","");
        header.put("method","post");
        header.put("msgType",msgType);
        header.put("token","");
        body.put("carNum",carNum);
        body.put("setDefendReason",reason);
        JSONObject object = new JSONObject();
        object.put("header",header);
        object.put("body",body);
        object.put("checkCode",checkCode);
        return JSON.toJSONString(object);
    }

}
