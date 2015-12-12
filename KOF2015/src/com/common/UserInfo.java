package com.common;

public class UserInfo {

	public static String generateNickname()
	{
		final String[] prefix = {
				"昂戳的", "不懂规矩的", "突破天际的", "倒霉的", "暗爽的", "强拆的", "穿越的",
				"至高的", "忠厚的", "开朗的", "大方的", "成熟的", "善良的", "开放的", "好吃的",
				"婆婆妈妈的", "抑郁的", "贪小便宜的", "害羞的", "热心的", "阴险的", "幼稚的"
		};
		final String[] postfix = {
				"草稚京", "二阶堂红丸", "大门五郎",
				"特瑞", "安迪", "东丈",
				"坂崎良", "罗伯特", "坂崎由莉",
				"莉安娜", "拉尔夫", "克拉克",
				"麻宫雅典娜", "椎拳崇", "镇元斋",
				"金", "不知火舞", "神乐千鹤",
				"金家藩", "陈国汗", "蔡保健",
				"七伽社", "夏尔米", "克里斯",
				"山崎龙二", "玛丽", "比利",
				"八神庵", "大蛇", "矢吹真吾",
				"大师"
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
