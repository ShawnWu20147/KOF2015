package com.kof2015.server;

public class FighterInstance {
	
	/**
	 * 当前的生命值。
	 */
	public int hp;
	
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
		
	}
	
	/**
	 * 占位用。用于天命、异常状态等属性的改变。
	 * 参数可以是：改变的属性类型，改变的数值。
	 */
	public void bouns(  )
	{}
}
