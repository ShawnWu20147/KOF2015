package com.common.message;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1187496040574472188L;
	
	/**
	 * 登录请求。
	 */
	public static final int TYPE_LOGIN = 1;
	/**
	 * 登录时使用，没有这个用户。
	 */
	public static final int TYPE_LOGIN_NONUSER = 2;
	/**
	 * 登录（注册？）时使用，已经存在相同用户名的用户。
	 */
	public static final int TYPE_LOGIN_EXIST = 3;
	/**
	 * 登录时使用，登录成功。
	 */
	public static final int TYPE_LOGIN_SUCCESS = 4;
	
	public int iType;
	public String strMsg;
	
	public static Message generateLoginMessage( String strMsg ) {
		Message msg = new Message();
		msg.iType = TYPE_LOGIN;
		msg.strMsg = strMsg;
		
		return msg;
	}
	
	public static Message generateLoginFeedback( int iType ) {
		Message msg = new Message();
		msg.iType = iType;
		
		return msg;
	}
}
