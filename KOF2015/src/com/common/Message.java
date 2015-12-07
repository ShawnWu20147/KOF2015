package com.common;

import java.io.Serializable;

public class Message implements Serializable{
	public int type;
	public String msg;
	
	public String s_info1;
	public String s_info2;
	public String s_info3;
	
	public int i_info1;
	public int i_info2;
	public int i_info3;
	
	public int from;
	
	//type=-1 just msg
	//type=0 login
	//type=1 force quit
	
	public Message(){
		type=-1;
		//just a message
	}
	
	public Message(int type){
		this.type=type;
	}
	
	public Message(int type,String msg){
		this.type=type;
		this.msg=msg;
	}

}
