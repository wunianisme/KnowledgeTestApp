package com.example.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.widget.LinearLayout;

import com.example.povo.Questions;
import com.example.povo.User;

public interface IQuestionService {

	public List<Questions> getEveryQuestion(InputStream is,int type);
	
	public int getFinishAndUnFinshNumber(HashMap<Integer,User> userSingleQuestion,HashMap<Integer,User> userMultiQuestion);
	
	public LinearLayout createView(Context c,int size);
	
	public Builder exitDialog(Context c);
	

}
