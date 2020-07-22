package org.jeecg.modules.ability.util;

public class Recognise_Result {
	private  String userId;
	private double socre;
	private String type;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getSocre() {
		return socre;
	}
	public void setSocre(double socre) {
		this.socre = socre;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
