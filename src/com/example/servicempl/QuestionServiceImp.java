package com.example.servicempl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;

import com.example.app.R;
import com.example.povo.Questions;
import com.example.povo.User;
import com.example.service.IQuestionService;

public class QuestionServiceImp implements IQuestionService {

	int index=1;
	@Override
	public List<Questions> getEveryQuestion(InputStream is,int type){
		List<Questions> qustions=new ArrayList<Questions>();
		Questions question=new Questions();
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br=new BufferedReader(isr);
		String line="";
		try {
			int pos=0;
			while((line=br.readLine())!=null){
				
				String str="";
				if(line.trim().startsWith("A")){
					question.setSelect_a(line);
				}
				else if(line.trim().startsWith("B")){
					question.setSelect_b(line);
				}
				else if(line.trim().startsWith("C")){
					question.setSelect_c(line);
				}
				else if(line.trim().startsWith("D")){
					question.setSelect_d(line);
					qustions.add(question);
				}else{
					if(question.getSelect_d()==null&&pos!=0) qustions.add(question);
					question=new Questions();
					if(line.trim().contains("--")){
						question.setId(index++);
						String[] splitStr=line.split("--");
						question.setQuestion(splitStr[0]);
						question.setAnswer(splitStr[1]);
						question.setType(type);
					}
					pos++;
				}
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return qustions;
	}
	
	@Override
	public int getFinishAndUnFinshNumber(HashMap<Integer, User> userSingleQuestion,
			HashMap<Integer, User> userMultiQuestion) {
		Set<Integer> keySet=userSingleQuestion.keySet();
		Set<Integer> multikeySet=userMultiQuestion.keySet();
		int singleFinish=0;
		for(Integer i:keySet){
			if(userSingleQuestion.get(i).getChecked()){
				singleFinish++;
			}
		}
		int multiFinish=0;
		for(Integer i:multikeySet){
			if(userMultiQuestion.get(i).getChecked()){
				multiFinish++;
			}
		}
		int sumNumber=userSingleQuestion.size()+userMultiQuestion.size();
		int extra=sumNumber-multiFinish-singleFinish;
		return extra;
	}

	public LinearLayout createView(Context c,int size) {
		return null;
	}

	public Builder exitDialog(Context c) {
	
		AlertDialog.Builder dialog=new Builder(c);
		dialog.setIcon(R.drawable.ic_launcher)
		.setTitle("退出登录")
		.setMessage("点击确定退出程序");
		
		return dialog;
	}

}
