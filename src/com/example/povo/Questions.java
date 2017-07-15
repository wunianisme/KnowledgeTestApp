package com.example.povo;

import java.io.Serializable;

public class Questions implements Serializable {
	private int id;
	private String question;
	private String select_a;
	private String select_b;
	private String select_c;
	private String select_d;
	private String answer;
	private int type;
	
	public Questions(){}
	
	public Questions(int id, String question, String select_a, String select_b,
			String select_c, String select_d, String answer,int type) {
		this.id = id;
		this.question = question;
		this.select_a = select_a;
		this.select_b = select_b;
		this.select_c = select_c;
		this.select_d = select_d;
		this.answer = answer;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getSelect_a() {
		return select_a;
	}
	public void setSelect_a(String select_a) {
		this.select_a = select_a;
	}
	public String getSelect_b() {
		return select_b;
	}
	public void setSelect_b(String select_b) {
		this.select_b = select_b;
	}
	public String getSelect_c() {
		return select_c;
	}
	public void setSelect_c(String select_c) {
		this.select_c = select_c;
	}
	public String getSelect_d() {
		return select_d;
	}
	public void setSelect_d(String select_d) {
		this.select_d = select_d;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
