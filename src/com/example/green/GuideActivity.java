package com.example.green;

import java.util.ArrayList;
import java.util.List;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.green.utils.DensittyUtil;
import com.example.green.utils.MyConstants;
import com.example.green.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * 鍚戝鐣岄潰锛屼娇鐢╒iewpager鐣岄潰鐨勫垏鎹�
 * 
 * */
public class GuideActivity extends Activity {
	private ViewPager vp_guides;
	private LinearLayout ll_points;
	private View v_redppint;
	private Button bt_startExp;
	private List<ImageView> guids;
	private Myadpter adpter;
	private int disPoints;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();// 鍒濆鍖栫晫闈�
		initData();// 鍒濆鍖栨暟鎹�
		initEvent();// 鍒濆鍖栫粍浜嬩欢
	}

	private void initEvent() {
		// 鐩戝惉甯冨眬瀹屾垚瑙﹀彂缁撴灉
		v_redppint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					

					@Override
					public void onGlobalLayout() {
						//鍙栨秷娉ㄥ唽鐣岄潰鍙樺寲鍙戠敓鐨勫洖璋冪粨鏋�
						v_redppint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						//璁＄畻璺濈
						disPoints = (ll_points.getChildAt(1).getLeft() - ll_points.getChildAt(0)
								.getLeft());
					}
				});
		// 鎸夐挳娣诲姞鐐瑰嚮浜嬩欢
		bt_startExp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 淇濆瓨璁剧疆鐘舵��
				SpTools.setBoolean(getApplicationContext(),
						MyConstants.ISSETUP, true);
				// 杩涘叆涓荤晫闈�
				Intent main = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(main);
				// 鍏抽棴鏈〉闈�
				finish();
			}
		});
		// vp_guides椤电爜鏀瑰彉浜嬩欢
		vp_guides.setOnPageChangeListener(new OnPageChangeListener() {

			private android.view.ViewGroup.LayoutParams layoutParams;

			@Override
			public void onPageSelected(int position) {
				// 褰撳墠ViewPager鏄剧ず椤甸潰
				// 濡傛灉ViewPager婊戝姩鍒版渶鍚庝竴涓〉闈紝鏄剧ずButton
				if (position == guids.size() - 1) {
					bt_startExp.setVisibility(View.VISIBLE);// 璁剧疆鎸夐挳鏄剧ず
				} else {
					bt_startExp.setVisibility(View.GONE);// 璁剧疆鎸夐挳闅愯棌
				}
			}

			/*
			 * 椤甸潰婊戝姩涓Е鍙戠殑浜嬩欢
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// positionOffset绉诲姩姣斾緥鍊�
				//璁＄畻璺濈澶у皬
				float leftMargin = disPoints * (position + positionOffset);
				
				//璁剧疆鍥涘彾鑽夊乏杈硅窛
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v_redppint.getLayoutParams();
				layoutParams.leftMargin = Math.round(leftMargin);//瀵筬loat鍥涜垗浜斿叆
				//閲嶆柊璁剧疆甯冨眬鍙傛暟 
				v_redppint.setLayoutParams(layoutParams);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void initData() {
		// 鍥剧墖鏁扮粍
		int[] pics = new int[] {R.drawable.jxcia_1,R.drawable.jxcia_2,};

		// 瀹氫箟ImageView浣跨敤瀹瑰櫒
		guids = new ArrayList<ImageView>();

		// 鍒濆鍖栧鍣ㄦ暟鎹�
		for (int i = 0; i < pics.length; i++) {
			ImageView iv_temp = new ImageView(getApplicationContext());
			iv_temp.setBackgroundResource(pics[i]);

			// 娣诲姞鏁版嵁
			guids.add(iv_temp);

			
			// .娣诲姞鐐瑰鍣↙inear鍒濆鐏拌壊鍥涘彾鑽�
			View v_point = new View(getApplicationContext());
			v_point.setBackgroundResource(R.drawable.jxcia_np);
			int dip = 25;
			// 璁剧疆鐐瑰弬鏁�
			LayoutParams params = new LayoutParams(DensittyUtil.dip2px(getApplicationContext(), dip), DensittyUtil.dip2px(getApplicationContext(), dip));
			// 鐐逛笌鐐归棿鐨勭┖闅�
			// 绗竴涓笉闇�瑕佹寚瀹�
			if (i != 0)
				params.leftMargin = 10;// px
			v_point.setLayoutParams(params);

			// 娣诲姞鍥涘彾鑽夊埌绾挎�у竷灞�
			ll_points.addView(v_point);
		}

		// 鍒涘缓viewPager鐨勯�傞厤鍣�
		adpter = new Myadpter();

		// 璁剧疆閫傞厤鍣�
		vp_guides.setAdapter(adpter);
	}

	private class Myadpter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return guids.size();// 杩斿洖鏁版嵁鐨勪釜鏁�
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1; // 杩囨护鍜岀紦瀛�
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);// 绉婚櫎
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 鑾峰彇View
			View childe = guids.get(position);
			// 娣诲姞View
			container.addView(childe);
			// 杩斿洖View
			return childe;
		}

	}

	private void initView() {
		setContentView(R.layout.activity_guide);
		vp_guides = (ViewPager) findViewById(R.id.vp_guide_pages);
		ll_points = (LinearLayout) findViewById(R.id.ll_guide_points);
		v_redppint = findViewById(R.id.v_guide_redpoint);
		bt_startExp = (Button) findViewById(R.id.bt_guide_startExp);

	}
}