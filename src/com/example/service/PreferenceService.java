package com.example.service;

import android.content.Context;

public interface PreferenceService {

	public int getChangeFlag(Context c);
	
	public void setChangeFlag(Context c,int flag);
}
