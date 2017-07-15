package com.example.app;

import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povo.User;
import com.example.servicempl.PreferenceServiceImp;
import com.example.servicempl.QuestionServiceImp;

public class MultiTestActivity extends Activity implements OnGestureListener,OnClickListener,android.widget.CompoundButton.OnCheckedChangeListener{

	private TextView testType, questionName,submit,pre,next,progress,index,go;
	private CheckBox[] selectMenu;
	private FrameLayout frame;
	private int[] checkId = new int[] { R.id.cbA, R.id.cbB, R.id.cbC,
			R.id.cbD };
	private static final int MIN_DISTANCE = 100;
	private GestureDetector detector;
	private QuestionServiceImp qServiceImp;
	private PreferenceServiceImp preferenceService;
	private HashMap<Integer, User> userSingleQuestion;
	private HashMap<Integer, User> userMultiQuestion;
	private int pageNo=1,rightNum,errorNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_multi_activity);
		initData();
		getUserData();
	}
	
	@SuppressWarnings("deprecation")
	public void initData() {
		testType = (TextView) findViewById(R.id.tvType);
		go=(TextView) findViewById(R.id.tvGoto);
		submit=(TextView) findViewById(R.id.tvSubmit);
		pre=(TextView) findViewById(R.id.tvPre);
		next=(TextView) findViewById(R.id.tvNext);
		progress=(TextView) findViewById(R.id.tvProgress);
		questionName = (TextView) findViewById(R.id.tvQuestion);
		index=(TextView) findViewById(R.id.tvIndex);
		frame=(FrameLayout) findViewById(R.id.frameTest);
		
		testType.setOnClickListener(this);
		submit.setOnClickListener(this);
		pre.setOnClickListener(this);
		next.setOnClickListener(this);
		index.setOnClickListener(this);
		go.setOnClickListener(this);
		
		selectMenu = new CheckBox[4];
		for (int i = 0; i < selectMenu.length; i++) {
			selectMenu[i] = (CheckBox) findViewById(checkId[i]);
			selectMenu[i].setOnCheckedChangeListener(this);
		}
		detector = new GestureDetector(this);
		qServiceImp = new QuestionServiceImp();
		preferenceService=new PreferenceServiceImp();
		userSingleQuestion = new HashMap<Integer, User>();
		userMultiQuestion = new HashMap<Integer, User>();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		String[] select={"A","B","C","D"};
		String userAnswer="";
		int unchecked=0;
		for (int i = 0; i < selectMenu.length; i++) {
			if(selectMenu[i].isChecked()){
				userAnswer+=select[i];
				userMultiQuestion.get(pageNo).setChecked(true);
			}else{
				unchecked++;
			}
		}
		if(unchecked==4) userMultiQuestion.get(pageNo).setChecked(false);
		userMultiQuestion.get(pageNo).setUserAnswer(userAnswer);
		for (int i = 0; i < selectMenu.length; i++) {
			if(userMultiQuestion.get(pageNo).getAnswer().trim().equals(userAnswer)){
				userMultiQuestion.get(pageNo).setRight(true);
			}else{
				userMultiQuestion.get(pageNo).setRight(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getUserData(){
		Intent data=getIntent();
		Bundle bundle=data.getExtras();
		userSingleQuestion=(HashMap<Integer, User>) bundle.getSerializable("single");
		userMultiQuestion=(HashMap<Integer, User>)bundle.getSerializable("multi");
		pageNo=bundle.getInt("pageNo",1);
		String[] answer={"A","B","C","D"};
		int index=pageNo+userSingleQuestion.size();
		questionName.setText(index+"、"+userMultiQuestion.get(pageNo).getQuestion());
		selectMenu[0].setText(userMultiQuestion.get(pageNo).getSelect_a());
		selectMenu[1].setText(userMultiQuestion.get(pageNo).getSelect_b());
		selectMenu[2].setText(userMultiQuestion.get(pageNo).getSelect_c());
		if(userMultiQuestion.get(pageNo).getSelect_d()==null){
			selectMenu[3].setVisibility(View.GONE);
		}else{
			selectMenu[3].setVisibility(View.VISIBLE);
			selectMenu[3].setText(userMultiQuestion.get(pageNo).getSelect_d());
		}
		if(userMultiQuestion.get(pageNo).getChecked()){
			String[] split=userMultiQuestion.get(pageNo).getUserAnswer().split("");
			for(int i=1;i<split.length;i++){
				for (int j = 0; j <answer.length; j++) {
					if((split[i].equals(answer[j]))){
						selectMenu[j].setChecked(true);
						break;
					}
				}
			}
		}
		int extra=qServiceImp.getFinishAndUnFinshNumber(userSingleQuestion, userMultiQuestion);
		int sum=userMultiQuestion.size()+userSingleQuestion.size();
		progress.setText((sum-extra)+"/"+sum);
		createFrameLayout(sum);
	}
	
	
	public void createFrameLayout(int sum){
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		TextView title=new TextView(this);
		title.setText("选择题号");
		title.setGravity(Gravity.CENTER);
		title.setTextSize(24);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 5, 0, 5);
		title.setLayoutParams(params);
		layout.addView(title);
		for (int i = 1; i <=sum/5; i++) {
			LinearLayout mlayout=new LinearLayout(this);
			mlayout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 1; j <= 5; j++) {
				TextView text=new TextView(this);
				int id=(i-1)*5+j;
				int images;
				int index = 1;
				HashMap<Integer,User> questions;
				if(id<=30){
					questions=userSingleQuestion;
					index=id;
				} 
				else {
					questions=userMultiQuestion;
					index=id-userSingleQuestion.size();
				}
				if(questions.get(index).getChecked()) images=R.drawable.icon_finish;
				else images=R.drawable.icon_empty;
				text.setCompoundDrawablesWithIntrinsicBounds(0, images, 0, 0);
				text.setText(""+id);
				text.setGravity(Gravity.CENTER);
				text.setLayoutParams(new LayoutParams(130, 180));
				text.setId(id);
				text.setTextSize(20);
				text.setOnClickListener(this);
				mlayout.setGravity(Gravity.CENTER);
				mlayout.addView(text);
			}
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			layout.addView(mlayout);
		}
		ScrollView scrollView=new ScrollView(this);
		scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1100));
		scrollView.addView(layout);
		frame.addView(scrollView);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvSubmit:
			int extra=qServiceImp.getFinishAndUnFinshNumber(userSingleQuestion, userMultiQuestion);
			int sum=userSingleQuestion.size()+userMultiQuestion.size();
			if(extra==0){
				submitDialog("亲，您确定要交卷吗？",1);
			}else if(extra==sum){
				submitDialog("请不要交白卷",-1);
			}else{
				submitDialog("亲，您还有"+extra+"道题没做，确定要交卷吗？",1);
			}
			break;
		case R.id.tvType:
			changeTestType(1);
			break;
		case R.id.tvPre:
			if(pageNo>1) refreshView(pageNo-1,1);
			else refreshView(30,1);
			break;
			
		case R.id.tvNext:
			if(pageNo<20)refreshView(pageNo+1,2);
			else Toast.makeText(this, "这已经是最后一页了！", 0).show();
			break;
		case R.id.tvIndex:
			if(frame.getVisibility()==View.GONE){
				AlertDialog.Builder dialog=new Builder(this);
				dialog.setTitle("退出测试")
				.setIcon(R.drawable.ic_launcher)
				.setMessage("您确定要退出当前测试吗？")
				.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent=new Intent(MultiTestActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
				}).setNegativeButton("继续作答", null).create().show();
			}else{
				frame.setVisibility(View.GONE);
				testType.setVisibility(View.VISIBLE);
				findViewById(R.id.tvSelected).setVisibility(View.VISIBLE);
			}
			break;
			
		case R.id.tvGoto:
			frame.setVisibility(View.VISIBLE);
			testType.setVisibility(View.INVISIBLE);
			findViewById(R.id.tvSelected).setVisibility(View.INVISIBLE);
			break;
		default:
			frame.setVisibility(View.GONE );
			if(v.getId()>userSingleQuestion.size())refreshView(v.getId(),2);
			else refreshView(v.getId(),-1);
			break;
		}
	}
	
	public void changeTestType(int index){
		Intent intent=new Intent(this, SingleTestActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("single", userSingleQuestion);
		bundle.putSerializable("multi", userMultiQuestion);
		bundle.putInt("pageNo", preferenceService.getChangeFlag(this));
		preferenceService.setChangeFlag(this, pageNo);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	public Intent setIntent(int index, Intent intent) {
		Bundle bundle=new Bundle();
		bundle.putSerializable("single", userSingleQuestion);
		bundle.putSerializable("multi", userMultiQuestion);
		if(index!=-1) bundle.putInt("pageNo", index);
		if(index>userSingleQuestion.size()) bundle.putInt("pageNo", index-userSingleQuestion.size());
		intent.putExtras(bundle);
		return intent;
	}
	
	public void submitDialog(String message,final int flag){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("交卷")
		.setIcon(R.drawable.ic_launcher)
		.setMessage(message)
		.setPositiveButton("确定交卷", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				if(flag==1) refreshView(-1, 2);
				else Toast.makeText(MultiTestActivity.this, "提交失败！", 0).show();
			}
		}).setNegativeButton("再做一会", null).create().show();
	}
	
	public void refreshView(int index,int direction){
		Intent intent = null;
		Bundle bundle=new Bundle();
		if(index==-1) intent=new Intent(MultiTestActivity.this,SubmitTestActivity.class);
		if(index!=-1&&direction>0){
			intent=new Intent(MultiTestActivity.this,MultiTestActivity.class);
		}else if(index==30&&direction==1||direction<0){
			intent=new Intent(MultiTestActivity.this,SingleTestActivity.class);
		}
		intent=setIntent(index, intent);
		startActivity(intent);
		if(direction==1||direction==-1) this.overridePendingTransition(R.anim.left_in,R.anim.right_out);//右
		else  this.overridePendingTransition(R.anim.right_in,R.anim.left_out);//左
		finish();
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
		if (e1.getX() - e2.getX() >= MIN_DISTANCE) {
			if(pageNo<20)refreshView(pageNo+1,2);// 向左滑动
			else Toast.makeText(this, "这已经是最后一页了！", 0).show();
			return true;
		} else if (e1.getX() - e2.getY() < -MIN_DISTANCE) {
			if(pageNo>1) refreshView(pageNo-1,1);// 向右滑动
			else refreshView(30,1);
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
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			final AlertDialog.Builder dialog=qServiceImp.exitDialog(this);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					((DialogInterface) dialog).dismiss();
					finish();
				}
			}).setNegativeButton("取消", null).create().show();
		}
		return true;
	}
}
