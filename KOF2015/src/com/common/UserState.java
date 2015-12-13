package com.common;

/**
 * 记录用户状态信息的类。
 * 可以包括：用户积分，用户所属的卡片信息等。
 * @author cellzero
 *
 */
public class UserState {
	
	/**
	 * 用户当前的积分，匹配时使用。
	 */
	public int score;
	
	/**
	 * 标志当前用户，是否在同其他人比赛。
	 */
	public boolean isFighting;
	
	public UserState() {
		score = (int) (Math.random() * 10000);
		isFighting = false;
	}
}
