package com.example.servicempl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.service.PreferenceService;

public class PreferenceServiceImp implements PreferenceService {

	@Override
	public int getChangeFlag(Context c) {
		SharedPreferences preferences=c.getSharedPreferences("ChangeFlag", Context.MODE_PRIVATE);
		return preferences.getInt("flag", 1);
	}

	@Override
	public void setChangeFlag(Context c, int flag) {
		SharedPreferences preferences=c.getSharedPreferences("ChangeFlag", Context.MODE_PRIVATE);
		Editor e=preferences.edit();
		e.putInt("flag", flag);
		e.commit();
	}
}
