package com.example.app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povo.Questions;
import com.example.povo.User;
import com.example.servicempl.QuestionServiceImp;

public class MainActivity extends Activity implements OnClickListener {

	private Button start;
	private TextView ins;
	private HashMap<Integer, User> userSingleQuestion;
	private HashMap<Integer, User> userMultiQuestion;
	private String[] resourceFile = new String[] { "dangshi.txt",
			"dangzhang.txt", "jilv_zhuize.txt", "shibada.txt", "xijinping.txt",
			"shibada_duoxuan.txt", "dangzhang_duoxuan.txt" };
	private ArrayList[] queList = new ArrayList[7];
	private QuestionServiceImp qServiceImp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initData();
		getUsersQuestion();
	}

	public void initData() {
		start = (Button) findViewById(R.id.btnStart);
		ins=(TextView) findViewById(R.id.tv_ins);
		start.setOnClickListener(this);
		String text="测试说明：本测试分为两个部分：单选题和多选题，总共50道题。其中单选题" +
				"有30题，每题2分，答对给2分，答错不给分；多选题有20道，每题2分，答对给2分，" +
				"答对但不全给1分，有答错的不给分，60分及格，加油吧！";
		ins.setText(text);
		qServiceImp = new QuestionServiceImp();
		userSingleQuestion = new HashMap<Integer, User>();
		userMultiQuestion = new HashMap<Integer, User>();
		for (int i = 0; i < queList.length; i++) {
			queList[i] = new ArrayList<Questions>();
		}
	}

	public void getUsersQuestion() {
		try {
			// 单选
			for (int i = 0; i < queList.length; i++) {
				InputStream is = getAssets().open(resourceFile[i]);
				if (i < 5)
					queList[i] = (ArrayList<Questions>) qServiceImp
							.getEveryQuestion(is, 1);
				else
					queList[i] = (ArrayList<Questions>) qServiceImp
							.getEveryQuestion(is, 2);
			}
			int single = 1;
			for (int i = 0; i < 5; i++) {
				while (userSingleQuestion.size() < (i + 1) * 6) {
					boolean same = false;
					int rand = (int) (Math.random() * queList[i].size());
					Questions question = (Questions) queList[i].get(rand);
					if (userSingleQuestion.size() > 0) {
						same = false;
						for (int j = 1; j <= userSingleQuestion.size(); j++) {
							if (userSingleQuestion.get(j).getQuestion()
									.equals(question.getQuestion())) {
								same = true;
								break;
							}
						}
						if (same) {
							continue;
						}
					}
					User u = new User();
					u = getUser(question, u, 1);
					u.setId(single);
					single++;
					if (userSingleQuestion.size() == 0 || same == false)
						userSingleQuestion.put(u.getId(), u);
				}
			}
			
			int multi = 1;
			for (int i = 5; i <= 6; i++) {
				int size = 0;
				if (i == 5)
					size = 5;
				if (i == 6)
					size = 20;

				while (userMultiQuestion.size() < size) {
					boolean same = false;
					int rand = (int) (Math.random() * queList[i].size());
					Questions question = (Questions) queList[i].get(rand);
					if (userMultiQuestion.size() > 0) {
						same = false;
						for (int j = 1; j <= userMultiQuestion.size(); j++) {
							if (userMultiQuestion.get(j).getQuestion()
									.equals(question.getQuestion())) {
								same = true;
								break;
							}
						}
						if (same) {
							continue;
						}
					}
					User u = new User();
					u = getUser(question, u, 2);
					u.setId(multi);
					multi++;
					if (userMultiQuestion.size() == 0 || same == false)
						userMultiQuestion.put(u.getId(), u);
				}
			}
			Set<Integer> keySet = userMultiQuestion.keySet();
			for (Integer i : keySet) {
				String text = "题目：" + userMultiQuestion.get(i).getId() + " "
						+ userMultiQuestion.get(i).getQuestion() + "\n"
						+ userMultiQuestion.get(i).getSelect_a() + "\n"
						+ userMultiQuestion.get(i).getSelect_b() + "\n"
						+ userMultiQuestion.get(i).getSelect_c() + "\n"
						+ userMultiQuestion.get(i).getSelect_d() + "\n正确答案："
						+ userMultiQuestion.get(i).getAnswer() + "\n选中状态:"
						+ userMultiQuestion.get(i).getChecked() + "\n类型："
						+ userMultiQuestion.get(i).getType() + "\n正确性："
						+ userMultiQuestion.get(i).getRight() + "\n用户答案："
						+ userMultiQuestion.get(i).getUserAnswer();
				Log.i("多选题", text);
				// Toast.makeText(this, "多选题--"+text, 0).show();
			}

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), 1).show();
		}
	}

	public User getUser(Questions question, User u, int type) {
		u.setQuestion(question.getQuestion());
		u.setSelect_a(question.getSelect_a());
		u.setSelect_b(question.getSelect_b());
		u.setSelect_c(question.getSelect_c());
		u.setSelect_d(question.getSelect_d());
		u.setAnswer(question.getAnswer());
		u.setUserAnswer(null);
		u.setChecked(false);
		u.setRight(false);
		u.setType(type);
		return u;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MainActivity.this, SingleTestActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("single", userSingleQuestion);
		bundle.putSerializable("multi", userMultiQuestion);
		bundle.putInt("pageNo", 1);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
