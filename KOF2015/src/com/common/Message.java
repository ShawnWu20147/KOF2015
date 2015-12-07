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
