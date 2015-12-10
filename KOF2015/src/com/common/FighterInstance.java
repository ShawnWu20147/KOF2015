package com.common;

import java.io.Serializable;

public class FighterInstance implements Serializable{
	
	
	
	public FighterInfo original_base;
	
	public int id;
	public String name;
	
	public int fighter_type;
	public String ft_type;
	
	

	public int hp;
	public int max_hp;
	
	public boolean isDead;
	
	public int true_attack,true_defence,true_hit,true_block;	//ָ���Ǵ���ʱ������� ����״̬
	
	
	public int true_atk_anger,true_atkd_anger,true_pw_anger,true_pwd_anger;
	
	public double true_skill_ratio;
	
	public String skill_name,skill_description;
	public String skill_type;
	
	public int skill_type_i;
	
	
	
	/**
	 * ��ǰ��ŭ��ֵ��
	 */
	public int anger;
	
	
	
	/**
	 * �����񶷼ҵ�����ʵ�壬������ǰ����ֵ����ʱ���ɱ䣩���ԣ�����ս���ȡ�
	 * @param base �񶷼ҵĻ������ݡ�
	 * @param ratio �����Ǽ������ı��ʡ�
	 */
	public FighterInstance(FighterInfo base, double ratio,int atk_plus) {
		this.original_base=base;
		
		name=base.name;
		id=base.id;
		
		this.fighter_type=base.fighter_type;
		switch(this.fighter_type){
		case 0:
			this.ft_type="��";
			break;
		case 1:
			this.ft_type="��";
			break;			
		case 2:
			this.ft_type="��";
			break;
		}
		
		
		
		max_hp=(int) (base.base_hp*(1+ratio));
		hp=(int) (base.base_hp*(1+ratio));
		
		
		
		true_attack=base.base_attack+atk_plus;
		true_defence=base.base_defence;
		true_hit=base.base_hit;
		true_block=base.base_block;
		
		true_atk_anger=base.base_attack_anger;
		true_atkd_anger=base.base_attacked_anger;
		true_pw_anger=base.base_power_anger;
		true_pwd_anger=base.base_powered_anger;
		
		true_skill_ratio=base.skill_ratio;
		
		skill_name=base.skill_name;
		skill_description=base.skill_descrption;
		
		this.skill_type_i=base.skill_type;
		
		switch(base.skill_type){
		case 0:
			skill_type="��ɱ(���2��)";
			break;
		case 1:
			skill_type="��ɱ.ǰ";
			break;
		case 2:
			skill_type="ȫ��ɱ";
			break;			
		case 3:
			skill_type="ȫ���Ѫ";
			break;
		case 4:
			skill_type="��ɱ.ǰ(���3��)";
			break;
		case 5:
			skill_type="��ɱ.��(���3��)";
			break;
		case 6:
			skill_type="��ɱ.��(1��)";
			break;
		case 7:
			skill_type="���ɱ(3��)";
			break;
		}
		
		
		
		if (this.fighter_type==0)
			anger=300;
		else
			anger=900;
		
		
		isDead=false;
	}
	
	/**
	 * ռλ�á������������쳣״̬�����Եĸı䡣
	 * ���������ǣ��ı���������ͣ��ı����ֵ��
	 */
	public void bouns(  )
	{}
	
	public String getStatus(){
		String nameS="����:"+name;
		String idS="���:"+original_base.id;
		String qualityS="����:"+original_base.ability;
		
		String tpS="�񶷼�����:"+ft_type;
		
		String hpS="������:"+hp+"/"+max_hp;
		String angerS="ŭ��:"+anger+"/"+1000;
		String attackS="������:"+true_attack;
		String defenceS="������:"+true_defence;
		String hitS="������:"+true_hit;
		String blockS="����:"+true_block;
		
		String powerS="��ɱ��:["+skill_name+"]:"+skill_description;
		String powerTypeS="��ɱ������:"+skill_type;
		
		
		String overall=nameS+"\n"+idS+"\n"+qualityS+"\n"+tpS+"\n"+hpS+"\n"+angerS+"\n"+attackS+"\n"+defenceS+"\n"
		+hitS+"\n"+blockS+"\n"+powerS+"\n"+powerTypeS+"\n";
		return overall;
		
		
	}
}
