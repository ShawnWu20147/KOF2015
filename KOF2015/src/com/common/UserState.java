package com.common;

/**
 * ��¼�û�״̬��Ϣ���ࡣ
 * ���԰������û����֣��û������Ŀ�Ƭ��Ϣ�ȡ�
 * @author cellzero
 *
 */
public class UserState {
	
	/**
	 * �û���ǰ�Ļ��֣�ƥ��ʱʹ�á�
	 */
	public int score;
	
	/**
	 * ��־��ǰ�û����Ƿ���ͬ�����˱�����
	 */
	public boolean isFighting;
	
	public UserState() {
		score = (int) (Math.random() * 10000);
		isFighting = false;
	}
}
