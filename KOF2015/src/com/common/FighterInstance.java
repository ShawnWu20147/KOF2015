package com.common;

import java.io.Serializable;

public class FighterInstance implements Serializable{
	
	public int max_hp;
	
	public FighterInfo base;
	
	/**
	 * ��ǰ������ֵ��
	 */
	public int hp;
	
	public boolean isDead;
	
	public int true_attack,true_defence,true_hit,true_block;
	
	
	
	/**
	 * ��ǰ��ŭ��ֵ��
	 */
	public int anger;
	
	// TODO ��� MaxHp��MaxAnger
	// TODO ��ӻ�������
	// TODO ����쳣״̬��
	
	/**
	 * �����񶷼ҵ�����ʵ�壬������ǰ����ֵ����ʱ���ɱ䣩���ԣ�����ս���ȡ�
	 * @param base �񶷼ҵĻ������ݡ�
	 * @param ratio �����Ǽ������ı��ʡ�
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
	 * ռλ�á������������쳣״̬�����Եĸı䡣
	 * ���������ǣ��ı���������ͣ��ı����ֵ��
	 */
	public void bouns(  )
	{}
	
	public String getStatus(){
		String nameS="����:"+base.name;
		String idS="���:"+base.id;
		String qualityS="����:"+base.ability;
		String hpS="������:"+hp+"/"+max_hp;
		String angerS="ŭ��:"+anger+"/"+1000;
		String attackS="������:"+true_attack;
		String defenceS="������:"+true_defence;
		String hitS="������:"+true_hit;
		String blockS="����:"+true_block;
		
		String overall=nameS+"\n"+idS+"\n"+qualityS+"\n"+hpS+"\n"+angerS+"\n"+attackS+"\n"+defenceS+"\n"+hitS+"\n"+blockS+"\n";
		return overall;
		
		
	}
}
