package com.common;

import java.util.HashSet;

public class Constants {
	public static final int MAX_ANGER=1000;
	public static final int BASE_ANGER_ATTACK=300;
	public static final int DEFENCE_ANGER_INCREASE=150;
	public static final int SKILL_ANGER_INCREASE=200;
	public static final int KILL_BONUS_ANGER=200;
	
	
	
	public static final double FRONT_HP_BONUS=0.2;
	public static final int BACK_ATTACK_BOUNS=50;
	
	public static final double NORMAL_ATTACK_MODIFY=2;
	public static final double NORMAL_DEFENCE_MODIFY=1;
	
	public static final double POWER_ATTACK_MODIFY=1.5;
	public static final double POWER_DEFENCE_MODIFY=2;
	
	
	
	
	public static final int NORMAL_HIT_MIN=25;
	public static final int NORMAL_HIT_MAX=75;
	
	public static final int NORMAL_BLOCK_MIN=25;
	public static final int NORMAL_BLOCK_MAX=75;
	

	public static final int POWER_HIT_MIN=25;
	public static final int POWER_HIT_MAX=75;
	
	public static final int POWER_BLOCK_MIN=25;
	public static final int POWER_BLOCK_MAX=75;
	public static final HashSet<Integer> ATTACK_BUFF_TYPE_INC=new HashSet<Integer>();
	public static final HashSet<Integer> ATTACK_BUFF_TYPE_DEC=new HashSet<Integer>();
	public static final HashSet<Integer> DEFENCE_BUFF_TYPE_INC=new HashSet<Integer>();
	public static final HashSet<Integer> DEFENCE_BUFF_TYPE_DEC=new HashSet<Integer>();
	
	public static final HashSet<Integer> HIT_BUFF_TYPE_INC=new HashSet<Integer>();
	public static final HashSet<Integer> HIT_BUFF_TYPE_DEC=new HashSet<Integer>();
	
	
	public static final HashSet<Integer> BLOCK_BUFF_TYPE_INC=new HashSet<Integer>();
	public static final HashSet<Integer> BLOCK_BUFF_TYPE_DEC=new HashSet<Integer>();
	
	public static final HashSet<String> ORCH_NAME=new HashSet<String>();
	
	
	static{
		ATTACK_BUFF_TYPE_INC.add(2);ATTACK_BUFF_TYPE_INC.add(8);ATTACK_BUFF_TYPE_INC.add(18);ATTACK_BUFF_TYPE_INC.add(19);	
		ATTACK_BUFF_TYPE_DEC.add(4);ATTACK_BUFF_TYPE_DEC.add(7);
		
		DEFENCE_BUFF_TYPE_INC.add(8);
		DEFENCE_BUFF_TYPE_DEC.add(13);
		
		HIT_BUFF_TYPE_INC.add(0);HIT_BUFF_TYPE_INC.add(5);HIT_BUFF_TYPE_INC.add(15);HIT_BUFF_TYPE_INC.add(17);
		HIT_BUFF_TYPE_DEC.add(14);
		
		BLOCK_BUFF_TYPE_INC.add(1);BLOCK_BUFF_TYPE_INC.add(3);BLOCK_BUFF_TYPE_INC.add(16);BLOCK_BUFF_TYPE_INC.add(20);
		BLOCK_BUFF_TYPE_DEC.add(15);
		
		ORCH_NAME.add("龙二");
		ORCH_NAME.add("七枷社");
		ORCH_NAME.add("夏尔美");
		ORCH_NAME.add("克里斯");
		ORCH_NAME.add("疯丽安娜");
		ORCH_NAME.add("疯八神庵");
		ORCH_NAME.add("大蛇");
		
		
		
	}
	
	
	
	
}
