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
		case 0:return "��";
		case 1:return "��";
		case 2:return "��";
		}
		return null;
	}
	
	public String toStringHtml(){
		String a="���:"+id;
		String b="����:"+name;
		String c="����:"+ability;
		String d="�񶷼�����:"+getType(fighter_type);
		String e="��������:"+base_hp;
		String f="��������:"+base_attack;
		String g="��������:"+base_defence;
		String h="��������:"+base_hit+"%";
		String i="������:"+base_block+"%";
		String j="����ʱŭ������:"+base_attack_anger;
		String k="������ʱŭ������:"+base_attacked_anger;
		String l="�Ŵ���ʱŭ������:"+base_power_anger;
		String m="������ʱŭ������:"+base_powered_anger;
		String n="�񶷼�����:"+description;
		String o="�񶷼Ҵ���:["+skill_name+"]:"+skill_descrption;
		String p="��������:"+getPowerType(skill_type);
		String q="���б���:"+skill_ratio;
		
		
		String all="<html>"+"<p>" +a+"</p>"+"<p>"+b+"</p>"+"<p>"+c+"</p>"+"<p>"+d+"</p>"+"<p>"+e+"</p>"+"<p>"+f+"</p>"+"<p>"+g+"</p>"+"<p>"+h+"</p>"+"<p>"+i+"</p>"+"<p>"+j+"</p>"+"<p>"+k+"</p>"+"<p>"
				+l+"</p>"+"<p>"+m+"</p>"+"<p>"+n+"</p>"+"<p>"+o+"</p>"+"<p>"+p+"</p>"+"<p>"+q+"</p>"+"</html>";
		return all;
		
		
	}
	
	@Override
	public String toString() {
		String a="���:"+id;
		String b="����:"+name;
		String c="����:"+ability;
		String d="�񶷼�����:"+getType(fighter_type);
		String e="��������:"+base_hp;
		String f="��������:"+base_attack;
		String g="��������:"+base_defence;
		String h="��������:"+base_hit+"%";
		String i="������:"+base_block+"%";
		String j="����ʱŭ������:"+base_attack_anger;
		String k="������ʱŭ������:"+base_attacked_anger;
		String l="�Ŵ���ʱŭ������:"+base_power_anger;
		String m="������ʱŭ������:"+base_powered_anger;
		String n="�񶷼�����:"+description;
		String o="�񶷼Ҵ���:["+skill_name+"]:"+skill_descrption;
		String p="��������:"+getPowerType(skill_type);
		String q="���б���:"+skill_ratio;
		
		String all=a+"\n"+b+"\n"+c+"\n"+d+"\n"+e+"\n"+f+"\n"+g+"\n"+h+"\n"+i+"\n"+j+"\n"+k+"\n"
				+l+"\n"+m+"\n"+n+"\n"+o+"\n"+p+"\n"+q+"\n";
		return all;
	}

	private String getPowerType(int skill_type2) {
		switch(skill_type2){
		case 0: return "����ɱ";
		case 1: return "����ɱ";
		case 2: return "AOE";
		case 3: return "ȫ���Ѫ";
		case 4: return "����ǰ��ȫ��";
		case 5: return "��������ȫ��";
		case 6: return "�������ŵ���";
		case 7: return "�������3��";
		}
		return null;
	}
	
	

	
}
