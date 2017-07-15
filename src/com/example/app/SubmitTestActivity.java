package com.example.app;

import java.util.HashMap;

import com.example.povo.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitTestActivity extends Activity implements OnClickListener{

	private TextView score,again,errorVisit,cancel;
	private HashMap<Integer, User> userSingleQuestion;
	private HashMap<Integer, User> userMultiQuestion;
	private HashMap<Integer,User> errorQuestion;
	private int errorNum;
	private int rightNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.submit_activity);
		initData();
		getUserTestData();
	}
	
	public void initData(){
		score=(TextView) findViewById(R.id.tvScore);
		again=(TextView) findViewById(R.id.tvAgain);
		errorVisit=(TextView) findViewById(R.id.tvError);
		cancel=(TextView) findViewById(R.id.tvCancel);
		again.setOnClickListener(this);
		errorVisit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		errorQuestion=new HashMap<Integer, User>();
		userSingleQuestion=new HashMap<Integer, User>();
		userMultiQuestion=new HashMap<Integer, User>();
	}
	
	public void getUserTestData(){
		Intent data=getIntent();
		Bundle bundle=data.getExtras();
		userSingleQuestion=(HashMap<Integer, User>) bundle.getSerializable("single");
		userMultiQuestion=(HashMap<Integer, User>)bundle.getSerializable("multi");
		int rightSingleAnswer=0;
		int rightMultiAnswer=0;
		int halfRightMultiAnswer=0;
		int errorIndex=1;
		
		for (int i = 1; i <=userSingleQuestion.size(); i++) {
			if(userSingleQuestion.get(i).getRight()){
				rightSingleAnswer++;
			}else{
				errorQuestion.put(errorIndex++, userSingleQuestion.get(i));
			}
		}
		for (int i = 1; i <= userMultiQuestion.size(); i++) {
			if(userMultiQuestion.get(i).getRight()){
				rightMultiAnswer++;
			}else{
				errorQuestion.put(errorIndex++, userMultiQuestion.get(i));
				String answer=userMultiQuestion.get(i).getAnswer();
				String userAnswer=userMultiQuestion.get(i).getUserAnswer();
				if (userAnswer!=null&&!userAnswer.equals("")&&answer.contains(userAnswer)) {
						halfRightMultiAnswer++;
				}
			}
		}
		
		int sumSize=userMultiQuestion.size()+userSingleQuestion.size();
		int sumRightAnswer=rightMultiAnswer+rightSingleAnswer;
		int sumErrorAnswer=sumSize-sumRightAnswer-halfRightMultiAnswer;
		int sumScore=sumRightAnswer*2+halfRightMultiAnswer;
		errorNum=sumErrorAnswer+halfRightMultiAnswer;
		rightNum=sumRightAnswer;
		String scoreText="";
		((TextView)findViewById(R.id.tvRight)).setText("��ȷ��"+sumRightAnswer);
		((TextView)findViewById(R.id.tvErrors)).setText("����"+sumErrorAnswer);
		((TextView)findViewById(R.id.tvHalf)).setText("©ѡ��"+halfRightMultiAnswer);
		if(sumScore<60){
			scoreText+="����Ŷ���´�һ������";
		}else if(sumScore<80){
			scoreText+="���������������ͣ�";
		}else if(sumScore<90){
			scoreText+="���Ĳ���������";
		}else{
			scoreText+="�������������������ɣ�";
		}
		((TextView)findViewById(R.id.tvCongraduate)).setText(scoreText);
		score.setText(sumScore+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvAgain:
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tvError:
			Intent intent2=new Intent(this,ErrorTestActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("error", errorQuestion);
			bundle.putSerializable("single",userSingleQuestion);
			bundle.putSerializable("multi",userMultiQuestion);
			bundle.putInt("errorNum", errorNum);
			bundle.putInt("rightNum", rightNum);
//			bundle.putInt("pageNo", 1);
			intent2.putExtras(bundle);
			startActivity(intent2);
			finish();
			break;
		case R.id.tvCancel:
			finish();
			break;
		}
	}
}
