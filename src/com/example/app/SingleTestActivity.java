package com.example.app;

import java.util.HashMap;

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
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povo.User;
import com.example.servicempl.PreferenceServiceImp;
import com.example.servicempl.QuestionServiceImp;


public class SingleTestActivity extends Activity implements OnGestureListener,OnClickListener{

	private TextView testType,questionName,submit,pre,next,progress,index,go;
	private RadioGroup selectGroup;
	private RadioButton[] selectMenu;
	private FrameLayout frame;
	
	private int[] radioButtonId = new int[] { R.id.rbA, R.id.rbB, R.id.rbC,
			R.id.rbD };
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
		setContentView(R.layout.test_single_activity);
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
		
		selectGroup = (RadioGroup) findViewById(R.id.rgSelect);
		selectMenu = new RadioButton[4];
		for (int i = 0; i < selectMenu.length; i++) {
			selectMenu[i] = (RadioButton) findViewById(radioButtonId[i]);
		}
		detector = new GestureDetector(this);
		
		qServiceImp = new QuestionServiceImp();
		preferenceService=new PreferenceServiceImp();
		
		userSingleQuestion = new HashMap<Integer, User>();
		userMultiQuestion = new HashMap<Integer, User>();
		
		selectGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup rg, int id) {
				String[] select={"A","B","C","D"};
				RadioButton rbtn=(RadioButton) findViewById(id);
				if(rbtn.isChecked()){
					userSingleQuestion.get(pageNo).setChecked(true);
					for (int i = 0; i < selectMenu.length; i++) {
						if(selectMenu[i].getId()==id){
							userSingleQuestion.get(pageNo).setUserAnswer(select[i]);
							if(userSingleQuestion.get(pageNo).getAnswer().trim().equals(userSingleQuestion.get(pageNo).getUserAnswer())){
								userSingleQuestion.get(pageNo).setRight(true);
							}else{
								userSingleQuestion.get(pageNo).setRight(false);
							}
						}
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void getUserData(){
		Intent data=getIntent();
		Bundle bundle=data.getExtras();
		userSingleQuestion=(HashMap<Integer, User>) bundle.getSerializable("single");
		userMultiQuestion=(HashMap<Integer, User>)bundle.getSerializable("multi");
		pageNo=bundle.getInt("pageNo",1);
		if(pageNo==1) preferenceService.setChangeFlag(this, 1);
		if(pageNo>30){
			Intent intent=new Intent(SingleTestActivity.this,MultiTestActivity.class);
			intent=setIntent(1, intent);
			startActivity(intent);
			finish();
		}
		/*testType.setText("单选题 "+pageNo);*/
		String[] answer={"A","B","C","D"};
		questionName.setText(pageNo+"、"+userSingleQuestion.get(pageNo).getQuestion());
		selectMenu[0].setText(userSingleQuestion.get(pageNo).getSelect_a());
		selectMenu[1].setText(userSingleQuestion.get(pageNo).getSelect_b());
		selectMenu[2].setText(userSingleQuestion.get(pageNo).getSelect_c());
		if(userSingleQuestion.get(pageNo).getSelect_d()==null){
			selectMenu[3].setVisibility(View.GONE);
		}else{
			selectMenu[3].setVisibility(View.VISIBLE);
			selectMenu[3].setText(userSingleQuestion.get(pageNo).getSelect_d());
		}
		
		if(userSingleQuestion.get(pageNo).getChecked()){
			for(int i=0;i<answer.length;i++){
				if(answer[i].equals(userSingleQuestion.get(pageNo).getUserAnswer())){
					selectMenu[i].setChecked(true);
					break;
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
			int sum=userSingleQuestion.size()+userMultiQuestion.size();
			int extra=qServiceImp.getFinishAndUnFinshNumber(userSingleQuestion, userMultiQuestion);
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
			break;
		case R.id.tvNext:
			refreshView(pageNo+1,2);
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
						Intent intent=new Intent(SingleTestActivity.this,MainActivity.class);
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
			refreshView(v.getId(),2);
			break;
		}
	}
	
	public void submitDialog(String message,final int flag){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("交卷")
		.setIcon(R.drawable.ic_launcher)
		.setMessage(message)
		.setPositiveButton("确定交卷", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(flag==1) refreshView(-1, 2);
				else Toast.makeText(SingleTestActivity.this, "提交失败！", 0).show();
			}
		}).setNegativeButton("再做一会", null).create().show();
	}
	
	public void changeTestType(int index){
		Intent intent=new Intent(this, MultiTestActivity.class);
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
	
	public void refreshView(int index,int direction){
		Intent intent = null;
		if(index==-1) intent=new Intent(SingleTestActivity.this,SubmitTestActivity.class);
		else if(index<=30) intent=new Intent(SingleTestActivity.this,SingleTestActivity.class);
		else intent=new Intent(SingleTestActivity.this,MultiTestActivity.class);
		intent=setIntent(index, intent);
		startActivity(intent);
		if(direction==1) this.overridePendingTransition(R.anim.left_in,R.anim.right_out);//右
		else this.overridePendingTransition(R.anim.right_in,R.anim.left_out);//左
		finish();
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
		if (e1.getX() - e2.getX() >= MIN_DISTANCE) {
			refreshView(pageNo+1,2);// 向左滑动
			return true;
		} else if (e1.getX() - e2.getY() < -MIN_DISTANCE) {
			if(pageNo>1) refreshView(pageNo-1,1);// 向右滑动
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
