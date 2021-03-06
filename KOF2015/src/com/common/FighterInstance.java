package com.common;

import java.io.Serializable;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class FighterInstance implements Serializable{
	
	
	
	public FighterInfo original_base;
	
	public int id;
	public String name;
	
	public int fighter_type;
	public String ft_type;
	
	

	public int hp;
	public int max_hp;
	
	public boolean isDead;
	
	public int true_attack,true_defence,true_hit,true_block;	//指的是创建时候的数据 不含状态
	
	
	public int true_atk_anger,true_atkd_anger,true_pw_anger,true_pwd_anger;
	
	public double true_skill_ratio;
	
	public String skill_name,skill_description;
	public String skill_type;
	
	public int skill_type_i;
	
	public ArrayList<SkillState> all_ss;
	
	
	
	/**
	 * 当前的怒气值。
	 */
	public int anger;
	
	
	
	/**
	 * 创建格斗家的数据实体，包含当前生命值等临时（可变）属性，用于战斗等。
	 * @param base 格斗家的基础数据。
	 * @param ratio 按照星级提升的倍率。
	 */
	public FighterInstance(FighterInfo base, double ratio,int atk_plus) {
		this.original_base=base;
		
		name=base.name;
		id=base.id;
		
		this.fighter_type=base.fighter_type;
		switch(this.fighter_type){
		case 0:
			this.ft_type="攻";
			break;
		case 1:
			this.ft_type="技";
			break;			
		case 2:
			this.ft_type="防";
			break;
		}
		
		
		
		//max_hp=(int) (base.base_hp*(1+ratio));
		//hp=(int) (base.base_hp*(1+ratio));
		
		max_hp=(int) (base.base_hp+ratio*3000);
		hp=max_hp;
		
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
			skill_type="横杀(最多2人)";
			break;
		case 1:
			skill_type="单杀.前";
			break;
		case 2:
			skill_type="全屏杀";
			break;			
		case 3:
			skill_type="全体回血";
			break;
		case 4:
			skill_type="竖杀.前(最多3人)";
			break;
		case 5:
			skill_type="竖杀.后(最多3人)";
			break;
		case 6:
			skill_type="单杀.后(1人)";
			break;
		case 7:
			skill_type="随机杀(3人)";
			break;
		}
		
		
		
		if (this.fighter_type==0)
			anger=Constants.BASE_ANGER_ATTACK;
		else
			anger=0;
		
		
		
		isDead=false;
		
		all_ss=new ArrayList<SkillState>();
	}
	
	/**
	 * 占位用。用于天命、异常状态等属性的改变。
	 * 参数可以是：改变的属性类型，改变的数值。
	 */
	public void bouns(  )
	{}
	
	public String getStatus(){
		String nameS="姓名:"+name;
		String idS="编号:"+original_base.id;
		String qualityS="资质:"+original_base.ability;
		
		String tpS="格斗家类型:"+ft_type;
		
		String hpS="生命力:"+hp+"/"+max_hp;
		String angerS="怒气:"+anger+"/"+Constants.MAX_ANGER;
		String attackS="攻击力:"+getActualAttack();
		String defenceS="防御力:"+getActualDefence();
		String hitS="暴击率:"+getActualHit()+"%";
		String blockS="格挡率:"+getActualBlock()+"%";
		
		String powerS="必杀技:["+skill_name+"]:"+skill_description;
		String powerTypeS="必杀技类型:"+skill_type;
		
		String powerS_EX="必杀技额外效果:"+original_base.skill_state_description;
		
		
		String overall=nameS+"\n"+idS+"\n"+qualityS+"\n"+tpS+"\n"+hpS+"\n"+angerS+"\n"+attackS+"\n"+defenceS+"\n"
		+hitS+"\n"+blockS+"\n"+powerS+"\n"+powerTypeS+"\n"+powerS_EX+"\n";
		return overall;
		
		
	}
	
	public String getStateInfo(){
		String rs="";
		for (SkillState ss:all_ss){
			rs+=ss.toString()+"\n";
		}
		return rs;
	}
	
	public int getActualAttack(){
		int base=true_attack;
		for (SkillState ss:all_ss){
			int tp=ss.type;
			//System.out.println("tp is "+tp+" value is "+ss.ratio);
			if (Constants.ATTACK_BUFF_TYPE_INC.contains(tp)){
				base+=ss.ratio;
			}
			if (Constants.ATTACK_BUFF_TYPE_DEC.contains(tp)){
				base-=ss.ratio;
			}
		}
		return base;
	}
	
	public int getActualDefence(){
		int base=true_defence;
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.DEFENCE_BUFF_TYPE_INC.contains(tp)){
				base+=ss.ratio;
			}
			if (Constants.DEFENCE_BUFF_TYPE_DEC.contains(tp)){
				base-=ss.ratio;
			}
		}
		return base;
	}
	
	public int getActualHit(){
		int base=true_hit;
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.HIT_BUFF_TYPE_INC.contains(tp)){
				base+=ss.ratio;
			}
			if (Constants.HIT_BUFF_TYPE_DEC.contains(tp)){
				base-=ss.ratio;
			}
		}
		return base;
	}
	
	public int getActualBlock(){
		int base=true_block;
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.BLOCK_BUFF_TYPE_INC.contains(tp)){
				base+=ss.ratio;
			}
			if (Constants.BLOCK_BUFF_TYPE_DEC.contains(tp)){
				base-=ss.ratio;
			}
		}
		
		return base;
	}
	
	
	public void addState(SkillState ss){
		all_ss.add(ss);
	}
	
	
	public boolean isFaint(){
		for (SkillState ss:all_ss){
			if (ss.type==6)
				return true;
		}
		return false;
	}
	
	public boolean isSilent(){
		for (SkillState ss:all_ss){
			if (ss.type==9)
				return true;
		}
		return false;
	}
	
	public String getFaintInfo(){
		String rs="";
		for (SkillState ss:all_ss){
			if (ss.type==6)
				rs+=ss.toString();
		}
		return rs;
	}
	public String getFaintInfoHTML(){
		String rs="<html>";
		for (SkillState ss:all_ss){
			if (ss.type==6)
				rs+="<p>"+ss.toString()+"</p>";
		}
		rs+="</html>";
		return rs;
	}	
	
	
	
	public String getSilentInfo(){
		String rs="";
		for (SkillState ss:all_ss){
			if (ss.type==9)
				rs+=ss.toString();
		}
		return rs;
	}	
	public String getSilentInfoHTML(){
		String rs="<html>";
		for (SkillState ss:all_ss){
			if (ss.type==9)
				rs+="<p>"+ss.toString()+"</p>";
		}
		rs+="</html>";
		return rs;
	}	
	
	public String getBuffInfo(){
		String rs="";
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.ATTACK_BUFF_TYPE_INC.contains(tp) || Constants.DEFENCE_BUFF_TYPE_INC.contains(tp)
					|| Constants.HIT_BUFF_TYPE_INC.contains(tp) || Constants.BLOCK_BUFF_TYPE_INC.contains(tp)){
				rs+=ss.toString();
			}
		}
		return rs;
	}
	
	public String getBuffInfoHTML(){
		String rs="<html>";
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.ATTACK_BUFF_TYPE_INC.contains(tp) || Constants.DEFENCE_BUFF_TYPE_INC.contains(tp)
					|| Constants.HIT_BUFF_TYPE_INC.contains(tp) || Constants.BLOCK_BUFF_TYPE_INC.contains(tp)){
				rs+="<p>"+ss.toString()+"</p>";
			}
		}
		rs+="</html>";
		//System.out.println("BUFF HTML="+rs);
		return rs;
	}
	
	public String getDebuffInfo(){
		String rs="";
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.ATTACK_BUFF_TYPE_DEC.contains(tp) || Constants.DEFENCE_BUFF_TYPE_DEC.contains(tp)
					|| Constants.HIT_BUFF_TYPE_DEC.contains(tp) || Constants.BLOCK_BUFF_TYPE_DEC.contains(tp)){
				rs+=ss.toString();
			}
		}
		return rs;
	}	
	
	public String getDebuffInfoHTML(){
		String rs="<html>";
		for (SkillState ss:all_ss){
			int tp=ss.type;
			if (Constants.ATTACK_BUFF_TYPE_DEC.contains(tp) || Constants.DEFENCE_BUFF_TYPE_DEC.contains(tp)
					|| Constants.HIT_BUFF_TYPE_DEC.contains(tp) || Constants.BLOCK_BUFF_TYPE_DEC.contains(tp)){
				rs+="<p>"+ss.toString()+"</p>";
			}
		}
		rs+="</html>";
		//System.out.println("DEBUFF HTML="+rs);
		return rs;
	}	
	
	
	
	
}
