package com.kof2015.server;
import java.io.Serializable;


public class FighterInfo implements Serializable{
	public int id;
	public String name;
	public int ability;
	public int fighter_type;
	public int base_hp;
	public int base_attack;
	public int base_defence;
	public int base_hit;
	public int base_block;
	public int base_attack_anger;
	public int base_attacked_anger;
	public int base_power_anger;
	public int base_powered_anger;
	public String description;
	public String skill_name;
	public String skill_descrption;
	public int skill_type;
	public double skill_ratio;
	
	public FighterInfo(int id,String name,int ability,int fighter_type,int base_hp,int base_attack,int base_defence
			,int base_hit,int base_block,int base_attack_anger,int base_attacked_anger,int base_power_anger,int base_powered_anger
			, String description,String skill_name,String skill_description,int skill_type,double skill_ratio){
			this.id=id;
			this.name=name;
			this.ability=ability;
			this.fighter_type=fighter_type;
			this.base_hp=base_hp;
			this.base_attack=base_attack;
			this.base_defence=base_defence;
			this.base_hit=base_hit;
			this.base_block=base_block;
			this.base_attack_anger=base_attack_anger;
			this.base_attacked_anger=base_attacked_anger;
			this.base_power_anger=base_power_anger;
			this.base_powered_anger=base_powered_anger;
			this.description=description;
			this.skill_name=skill_name;
			this.skill_descrption=skill_description;
			this.skill_type=skill_type;
			this.skill_ratio=skill_ratio;
	}

	private String getType(int i){
		switch(i){
		case 0:return "攻";
		case 1:return "技";
		case 2:return "防";
		}
		return null;
	}
	
	public String toStringHtml(){
		String a="编号:"+id;
		String b="姓名:"+name;
		String c="资质:"+ability;
		String d="格斗家类型:"+getType(fighter_type);
		String e="基本生命:"+base_hp;
		String f="基本攻击:"+base_attack;
		String g="基本防御:"+base_defence;
		String h="基本暴击:"+base_hit+"%";
		String i="基本格挡:"+base_block+"%";
		String j="攻击时怒气增加:"+base_attack_anger;
		String k="被攻击时怒气增加:"+base_attacked_anger;
		String l="放大招时怒气增加:"+base_power_anger;
		String m="被大招时怒气增加:"+base_powered_anger;
		String n="格斗家描述:"+description;
		String o="格斗家大招:["+skill_name+"]:"+skill_descrption;
		String p="大招类型:"+getPowerType(skill_type);
		String q="大招倍率:"+skill_ratio;
		
		
		String all="<html>"+"<p>" +a+"</p>"+"<p>"+b+"</p>"+"<p>"+c+"</p>"+"<p>"+d+"</p>"+"<p>"+e+"</p>"+"<p>"+f+"</p>"+"<p>"+g+"</p>"+"<p>"+h+"</p>"+"<p>"+i+"</p>"+"<p>"+j+"</p>"+"<p>"+k+"</p>"+"<p>"
				+l+"</p>"+"<p>"+m+"</p>"+"<p>"+n+"</p>"+"<p>"+o+"</p>"+"<p>"+p+"</p>"+"<p>"+q+"</p>"+"</html>";
		return all;
		
		
	}
	
	@Override
	public String toString() {
		String a="编号:"+id;
		String b="姓名:"+name;
		String c="资质:"+ability;
		String d="格斗家类型:"+getType(fighter_type);
		String e="基本生命:"+base_hp;
		String f="基本攻击:"+base_attack;
		String g="基本防御:"+base_defence;
		String h="基本暴击:"+base_hit+"%";
		String i="基本格挡:"+base_block+"%";
		String j="攻击时怒气增加:"+base_attack_anger;
		String k="被攻击时怒气增加:"+base_attacked_anger;
		String l="放大招时怒气增加:"+base_power_anger;
		String m="被大招时怒气增加:"+base_powered_anger;
		String n="格斗家描述:"+description;
		String o="格斗家大招:["+skill_name+"]:"+skill_descrption;
		String p="大招类型:"+getPowerType(skill_type);
		String q="大招倍率:"+skill_ratio;
		
		String all=a+"\n"+b+"\n"+c+"\n"+d+"\n"+e+"\n"+f+"\n"+g+"\n"+h+"\n"+i+"\n"+j+"\n"+k+"\n"
				+l+"\n"+m+"\n"+n+"\n"+o+"\n"+p+"\n"+q+"\n";
		return all;
	}

	private String getPowerType(int skill_type2) {
		switch(skill_type2){
		case 0: return "单列杀";
		case 1: return "单体杀";
		case 2: return "AOE";
		case 3: return "全体回血";
		case 4: return "攻击前排全部";
		case 5: return "攻击后排全部";
		case 6: return "攻击后排单人";
		case 7: return "随机攻击3人";
		}
		return null;
	}
	
	

	
}
