package com.example.green;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.green.utils.MyConstants;
import com.example.green.utils.SpTools;

public class SplashActivity extends Activity {
	
	private ImageView iv_splash;
	private AnimationSet as;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		startAnimation();
		initEvent();
	}

	private void initEvent() {
		as.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(SpTools.getBoolean(getApplicationContext(),MyConstants.ISSETUP, false)){
					//已经登录过软件，直接进入注册界面
					Intent main = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(main);
				}else{
					//false 第一次登录软件，进入向导界面
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);//启动界面
				}
				finish();
			}
		});
	}

	private void startAnimation() {
		as = new AnimationSet(false);
		
		//定义渐变动画
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		//动画时间
		aa.setDuration(2000);
		aa.setFillAfter(true);
		//添加动画
		as.addAnimation(aa);
		
		//定义缩放动画
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(1500);
		sa.setFillAfter(true);
		as.addAnimation(sa);
		
		iv_splash.startAnimation(as);
	}

	private void initView() {
		setContentView(R.layout.activity_splash);
		iv_splash = (ImageView) findViewById(R.id.iv_splash_bg);
	}

}
