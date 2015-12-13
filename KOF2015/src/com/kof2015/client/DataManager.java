package com.kof2015.client;

import com.common.UserInfo;

public class DataManager {
	
	private static DataManager manager = new DataManager();
	
	private UserInfo userInfo = null;
	
	private DataManager()
	{}
	
	public static DataManager getManager()
	{
		return manager;
	}
	
	public void setUserInfo(UserInfo currentUserInfo) {
		this.userInfo = currentUserInfo;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
}
