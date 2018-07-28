package com.cxt.gps.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public static String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
	public static String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
	public static String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
	public static String SHARED_KEY_ALARM = "shared_key_alarm";

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);//MODE_MULTI_PROCESS这模式service也可以同步
		editor = sp.edit();
	}
	// 已读alarm信息最新ID
	public int getAlarminfo() {
		return sp.getInt(SHARED_KEY_ALARM, 0);
	}

	public void setAlarminfo(int paramInt) {
		editor.putInt(SHARED_KEY_ALARM, paramInt);
		editor.commit();
	}
//	// 域名
//	public String getDomain() {
//		return sp.getString("domain", com.example.administrator.cxtapp.util.Constants.SERVER_DOMAIN);
//	}
//
//	public void setDomain(String domain) {
//		editor.putString("domain", domain);
//		editor.commit();
//	}
//	// 数据库存
//	public String getDB() {
//		return sp.getString("db", com.example.administrator.cxtapp.util.Constants.SERVER_Name);
//	}

	public void setDB(String db) {
		editor.putString("db", db);
		editor.commit();
	}
	public boolean getIsRemember(){
		return sp.getBoolean("isRemember",false);
	}
	public void setRemember(boolean isRemember){
		editor.putBoolean("isRemember",isRemember);
		editor.commit();
	}
	// 登录状态记录
	public void setIsStart(boolean isStart) {
		editor.putBoolean("isStart", isStart);
		editor.commit();
	}

	public boolean getIsStart() {
		return sp.getBoolean("isStart", false);
	}


	public void setIsFirst(boolean isFirst) {
		editor.putBoolean("isFirst", isFirst);
		editor.commit();
	}

	public boolean getisFirst() {
		return sp.getBoolean("isFirst", true);
	}
	public void setUserInfo(String str_name, String str_value) {

		editor.putString(str_name, str_value);
		editor.commit();
	}

	public String getUserInfo(String str_name) {

		return sp.getString(str_name, "");

	}
	public void removeUserInfo(String str_name){
		editor.remove(str_name);
		editor.commit();
	}
	public void removeUserInfo(){
		editor.clear();
		editor.commit();
	}

	public void setSettingMsgNotification(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgNotification() {
		return sp.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, false);
	}

	public void setSettingMsgSound(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgSound() {

		return sp.getBoolean(SHARED_KEY_SETTING_SOUND, false);
	}

	public void setSettingMsgVibrate(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgVibrate() {
		return sp.getBoolean(SHARED_KEY_SETTING_VIBRATE, false);
	}
}
