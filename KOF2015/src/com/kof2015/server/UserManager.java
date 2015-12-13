package com.kof2015.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.common.UserInfo;
import com.common.UserState;

public class UserManager {
	/**
	 * 维护了当前在线的所有用户。
	 * TODO 对吗？为了确保多线程安全，所有UserState操作都应该由该类完成。
	 */
	private static volatile HashMap<UserInfo, UserState> mapGlobalUsers = new HashMap<UserInfo, UserState>();
	
	/**
	 * TODO 从数据库加载用户状态。
	 * 可能出现：用户名不存在的情况。
	 */
	public static void load() {
		
	}
	
	/**
	 * 检查当前在线用户中，是否存在该用户。
	 * @param info
	 * @return
	 */
	public static boolean contains( UserInfo info ) {
		boolean result;
		synchronized (mapGlobalUsers) {
			result = mapGlobalUsers.containsKey( info );
		}
		return result;
	}
	
	public static void add( UserInfo info ) {
		if( info == null )
			return;
		
		UserState state = new UserState();
		synchronized (mapGlobalUsers) {
			mapGlobalUsers.put( info, state );
		}
	}
	
	public static void remove( UserInfo info ) {
		if( info == null )
			return;
		
		synchronized (mapGlobalUsers) {
			mapGlobalUsers.remove( info );
		}
	}
	
	private static UserState getState( UserInfo info ) {
		UserState state;
		synchronized (mapGlobalUsers) {
			state = mapGlobalUsers.get(info);
		}
		return state;
	}
	
	/**
	 * 如果当前没有对手，则返回null。
	 * TODO 优化效率，或者用更好的匹配方法。
	 * @param info
	 * @return
	 */
	public static UserInfo match( UserInfo info ) {
		UserInfo opponent = null;
		synchronized (mapGlobalUsers) {
			UserState selfState = getState( info );
			if( selfState != null && !selfState.isFighting ) {
				int minGap = Integer.MAX_VALUE;
				Iterator<Entry<UserInfo, UserState>> iterator = mapGlobalUsers.entrySet().iterator();
				while( iterator.hasNext() ) {
					Map.Entry<UserInfo, UserState> entry = (Map.Entry<UserInfo, UserState>) iterator.next();
					UserInfo key = (UserInfo) entry.getKey();
					UserState value = (UserState) entry.getValue();
					
					if( !value.isFighting ) {
						int scoreGap = Math.abs( value.score - selfState.score );
						if( scoreGap < minGap ) {
							opponent = key;
							minGap = scoreGap;
						}
					}
				}
				// 变更查询者、同其对手的状态为正在战斗。
				if( opponent != null ) {
					selfState.isFighting = true;
					getState(opponent).isFighting = true;
				}
			}
		}
		return opponent;
	}
}
