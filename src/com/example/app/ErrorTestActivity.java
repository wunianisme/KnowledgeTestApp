package com.example.app;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.povo.User;

public class ErrorTestActivity extends Activity implements OnGestureListener,
		OnClickListener {

	private TextView title, question, s_A, s_B, s_C, s_D, progress, ans,
			userAns,returns,error,right,again;
	private ViewFlipper flipper;
	private HashMap<Integer, User> errorQuestion;
	private static final int MIN_DISTANCE = 100;
	private GestureDetector detector;
	private View flipperView;
	private Animation leftIn, leftOut, rightIn, rightOut;
	private int pageNo,errorNum,rightNum;
	private Intent data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.error_activity);
		initData();
		try{
			for (int i = 1; i<=errorQuestion.size(); i++) {
				pageNo=i;
				flipper.addView(getErrorView(pageNo));
			}
		}catch(Exception e){
			Toast.makeText(this, e.getMessage(), 1).show();
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void initData() {
		title = (TextView) findViewById(R.id.tvTitle);
		flipper = (ViewFlipper) findViewById(R.id.vf);
		returns=(TextView) findViewById(R.id.tvReturn);
		returns.setOnClickListener(this);
		leftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
		leftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);
		rightIn = AnimationUtils.loadAnimation(this, R.anim.right_in);
		rightOut = AnimationUtils.loadAnimation(this, R.anim.right_out);
		title.setText("错题查看");
		detector=new GestureDetector(this);
		data = getIntent();
		Bundle bundle = data.getExtras();
		errorQuestion = (HashMap<Integer,User>) bundle.getSerializable("error");
		errorNum=bundle.getInt("errorNum",0);
		rightNum=bundle.getInt("rightNum",0);
	}

	@SuppressWarnings("unchecked")
	public View getErrorView(int pageNo) {
		LayoutInflater inflater =getLayoutInflater();
		flipperView = inflater.inflate(R.layout.error_viewflipper, null);
		question = (TextView) flipperView.findViewById(R.id.tvQuestion);
		s_A = (TextView) flipperView.findViewById(R.id.tvA);
		s_B = (TextView) flipperView.findViewById(R.id.tvB);
		s_C = (TextView) flipperView.findViewById(R.id.tvC);
		s_D = (TextView) flipperView.findViewById(R.id.tvD);
		ans = (TextView) flipperView.findViewById(R.id.tvAnswer);
		progress=(TextView) flipperView.findViewById(R.id.tvProgress);
		userAns = (TextView) flipperView.findViewById(R.id.tvUserAnswer);
		right=(TextView) flipperView.findViewById(R.id.tvRight);
		error=(TextView) flipperView.findViewById(R.id.tvError);
		again=(TextView) flipperView.findViewById(R.id.tvAgain);
		again.setOnClickListener(this);
		String type="";
		if(errorQuestion.get(pageNo).getType()==1) type="【单选题】";
		else type="【多选题】";
		question.setText(type+""+pageNo + "、" + errorQuestion.get(pageNo).getQuestion());
		s_A.setText(errorQuestion.get(pageNo).getSelect_a());
		s_B.setText(errorQuestion.get(pageNo).getSelect_b());
		s_C.setText(errorQuestion.get(pageNo).getSelect_c());

		if (errorQuestion.get(pageNo).getSelect_d() == null) {
			s_D.setVisibility(View.GONE);
		} else {
			s_D.setVisibility(View.VISIBLE);
			s_D.setText(errorQuestion.get(pageNo).getSelect_d());
		}

		ans.setText("正确答案：" + errorQuestion.get(pageNo).getAnswer());
		if(errorQuestion.get(pageNo).getUserAnswer()!=null&&!errorQuestion.get(pageNo).getUserAnswer().equals("")) userAns.setText("您的答案：" + errorQuestion.get(pageNo).getUserAnswer());
		else userAns.setText("您没有选择答案");
		String ans=errorQuestion.get(pageNo).getAnswer();
		String uAns=errorQuestion.get(pageNo).getUserAnswer();
		int tp=errorQuestion.get(pageNo).getType();
		if(tp==2&&uAns!=null&&!uAns.equals("")&&ans.contains(uAns)){
			userAns.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_halfright, 0, 0, 0);
		}
		progress.setText(pageNo + "/" + errorQuestion.size());
		right.setText("对："+rightNum);
		error.setText("错："+errorNum);
		return flipperView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvAgain:
			Intent intent =new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.tvReturn:
			Intent intent2=new Intent(this,SubmitTestActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("single", data.getExtras().getSerializable("single"));
			bundle.putSerializable("multi", data.getExtras().getSerializable("multi"));
			intent2.putExtras(bundle);
			startActivity(intent2);
			finish();
			break;
		}
	}

	@SuppressLint("NewApi") @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
		if (e1.getX() - e2.getX() >= MIN_DISTANCE) {
			// refreshView(pageNo+1,2);// 向左滑动
			flipper.setInAnimation(rightIn);
			flipper.setOutAnimation(leftOut);
			flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getY() < -MIN_DISTANCE) {
			// if(pageNo>1) refreshView(pageNo-1,1);// 向右滑动
			flipper.setInAnimation(leftIn);
			flipper.setOutAnimation(rightOut);
			flipper.showPrevious();
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
