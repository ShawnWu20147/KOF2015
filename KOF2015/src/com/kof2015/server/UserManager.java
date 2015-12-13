package com.kof2015.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.common.UserInfo;
import com.common.UserState;

public class UserManager {
	/**
	 * ά���˵�ǰ���ߵ������û���
	 * TODO ����Ϊ��ȷ�����̰߳�ȫ������UserState������Ӧ���ɸ�����ɡ�
	 */
	private static volatile HashMap<UserInfo, UserState> mapGlobalUsers = new HashMap<UserInfo, UserState>();
	
	/**
	 * TODO �����ݿ�����û�״̬��
	 * ���ܳ��֣��û��������ڵ������
	 */
	public static void load() {
		
	}
	
	/**
	 * ��鵱ǰ�����û��У��Ƿ���ڸ��û���
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
	 * �����ǰû�ж��֣��򷵻�null��
	 * TODO �Ż�Ч�ʣ������ø��õ�ƥ�䷽����
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
				// �����ѯ�ߡ�ͬ����ֵ�״̬Ϊ����ս����
				if( opponent != null ) {
					selfState.isFighting = true;
					getState(opponent).isFighting = true;
				}
			}
		}
		return opponent;
	}
}
