package com.common;

import java.io.Serializable;

import com.kof2015.client.ChooseFighter;

public class Message implements Serializable{
	public int type;
	public String msg;
	
	public String s_info1;
	public String s_info2;
	public String s_info3;
	
	
	public int identifier;
	
	public int i_info1;
	public int i_info2;
	public int i_info3;
	
	public FighterInfo []fi;
	
	public FighterInstance []fi_b;
	
	public int []who_attacked;
	
	public int from;
	
	//type=-1 just msg
	
	//type=0 login
	
	//type=1 force quit
	
	//type=2 xuanren xinxi
		//if received by players
		// i_info_1 whether_end? 1=end 0=not -1=start
		// i_info_2 who_sel
		// i_info_3 sel_how_many
		// s_info_1 info_to_print
	
		//if received by servers
		// i_info_1 how_many
		// i_info_2 f1_index
		// i_info_3 f2_index
		// identifier who selects
	
	
	//type=3 servers send 8 fighters to each
		// fi[] are used
	
		// if received by server, that means clients format over and tell server the result
		// fi[] are used
	
	
	
	//type=4 battles
	
		//s_info1 used for print logs!
	
		//if received by clients
		//i_info_1  [-2 starts the battle] [-1 turn starts] [0 actions-bisha] [1 actions-normal attack] [2 tell result]
		//i_info_2 who's turn
		//i_info_3 who takes actions
		//fi_b[]  0~5 belongs to P1, 6~11 belongs to P2
	
	
		//if received by servers
		//from   1 or 2
		//i_info_1	[0 actions-bisha]	[1 actions-normal attack] [2- turn over]
		//i_info_2	who attacks (USING index)
		//i_info_3  if it is normal attack, who is attacked (USING index)
		// who attacked[]  if it is bisha, who are attacked
		//对于雅典娜等特殊角色,其实并不需要提供who attacked 但是仍然留着
	
	
	
	
	public ChooseFighter[] cf;
	
	public Message(){
		type=-1;
		//just a message
	}
	
	public void setChooseFighter(ChooseFighter[] cf){
		this.cf=cf;
	}
	
	
	public Message(int type){
		this.type=type;
		cf=null;
	}
	
	public Message(int type,String msg){
		this.type=type;
		this.msg=msg;
		cf=null;
	}

}
