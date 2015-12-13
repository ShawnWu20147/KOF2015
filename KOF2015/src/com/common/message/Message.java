package com.common.message;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1187496040574472188L;
	
	/**
	 * ��¼����
	 */
	public static final int TYPE_LOGIN = 1;
	/**
	 * ��¼ʱʹ�ã�û������û���
	 */
	public static final int TYPE_LOGIN_NONUSER = 2;
	/**
	 * ��¼��ע�᣿��ʱʹ�ã��Ѿ�������ͬ�û������û���
	 */
	public static final int TYPE_LOGIN_EXIST = 3;
	/**
	 * ��¼ʱʹ�ã���¼�ɹ���
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
