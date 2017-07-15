package com.example.green.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpTools {
	/*
	 * key
	 * 关键字
	 * value
	 * 对应值
	 * */
	public static void setBoolean(Context context,String key,boolean value){
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();//提交保存键值对
	}
	
	/*context
	 * key
	 * 关键字
	 * defValue
	 * 设置的默认值
	 * */
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
		
	}
}
