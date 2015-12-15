package com.common;

import java.io.Serializable;

public class SkillState implements Serializable{
	String des;
	public int type;
	int ratio;
	
	int rounds_left;
	
	public SkillState(SkillState ts){
		this.des=ts.des;
		this.type=ts.type;
		this.ratio=ts.ratio;
		this.rounds_left=ts.rounds_left;
	}
	public SkillState(String des,int type,int ratio){
		this.des=des;
		this.type=type;
		this.ratio=ratio;
		
		rounds_left=4;
		if (this.type==6)
			rounds_left=2;
	}
	
	public String toString(){
		return des;
		
	}
	
	
	public int getRoundsLeft(){
		return rounds_left;
	}
	
	public void decreaseRound(){
		rounds_left-=1;
	}

	
	
}
