package com.common;

import java.io.Serializable;

public class FighterInstance implements Serializable{
	
	public int max_hp;
	
	public FighterInfo base;
	
	/**
	 * 当前的生命值。
	 */
	public int hp;
	
	public boolean isDead;
	
	public int true_attack,true_defence,true_hit,true_block;
	
	
	
	/**
	 * 当前的怒气值。
	 */
	public int anger;
	
	// TODO 添加 MaxHp，MaxAnger
	// TODO 添加基本属性
	// TODO 添加异常状态？
	
	/**
	 * 创建格斗家的数据实体，包含当前生命值等临时（可变）属性，用于战斗等。
	 * @param base 格斗家的基础数据。
	 * @param ratio 按照星级提升的倍率。
	 */
	public FighterInstance(FighterInfo base, double ratio) {
		this.base=base;
		
		max_hp=base.base_hp;
		
		hp=base.base_hp;
		
		true_attack=base.base_attack;
		true_defence=base.base_defence;
		true_hit=base.base_hit;
		true_block=base.base_block;
		
		anger=0;
		
		isDead=false;
	}
	
	/**
	 * 占位用。用于天命、异常状态等属性的改变。
	 * 参数可以是：改变的属性类型，改变的数值。
	 */
	public void bouns(  )
	{}
	
	public String getStatus(){
		String nameS="姓名:"+base.name;
		String idS="编号:"+base.id;
		String qualityS="资质:"+base.ability;
		String hpS="生命力:"+hp+"/"+max_hp;
		String angerS="怒气:"+anger+"/"+1000;
		String attackS="攻击力:"+true_attack;
		String defenceS="防御力:"+true_defence;
		String hitS="暴击率:"+true_hit;
		String blockS="格挡率:"+true_block;
		
		String overall=nameS+"\n"+idS+"\n"+qualityS+"\n"+hpS+"\n"+angerS+"\n"+attackS+"\n"+defenceS+"\n"+hitS+"\n"+blockS+"\n";
		return overall;
		
		
	}
}
