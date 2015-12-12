package com.common;

public class UserInfo {

	public static String generateNickname()
	{
		final String[] prefix = {
				"������", "������ص�", "ͻ����ʵ�", "��ù��", "��ˬ��", "ǿ���", "��Խ��",
				"���ߵ�", "�Һ��", "���ʵ�", "�󷽵�", "�����", "������", "���ŵ�", "�óԵ�",
				"���������", "������", "̰С���˵�", "���ߵ�", "���ĵ�", "���յ�", "���ɵ�"
		};
		final String[] postfix = {
				"���ɾ�", "�����ú���", "��������",
				"����", "����", "����",
				"������", "�޲���", "��������",
				"����", "������", "������",
				"�鹬�ŵ���", "׵ȭ��", "��Ԫի",
				"��", "��֪����", "����ǧ��",
				"��ҷ�", "�¹���", "�̱���",
				"��٤��", "�Ķ���", "����˹",
				"ɽ������", "����", "����",
				"������", "����", "ʸ������",
				"��ʦ"
		};
		
		String nickname;
		int r1 = (int)(Math.random() * prefix.length);
		int r2 = (int)(Math.random() * postfix.length);
		nickname = prefix[ r1 ] + postfix[ r2 ];
		
		return nickname;
	}
	
	public String nickname;
	
	public UserInfo() {
		nickname = UserInfo.generateNickname();
	}
	
	public UserInfo(String nickname) {
		this.nickname = nickname;
	}
	
	public String getIdentifier() {
		return nickname;
	}
}
