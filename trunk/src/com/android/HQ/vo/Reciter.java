package com.android.HQ.vo;

public class Reciter {

	private String url;
	private String name;
	
	public Reciter(String n, String u){
		this.url = u;	
		this.name = n;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
