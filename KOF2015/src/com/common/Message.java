package com.common;

import java.io.Serializable;

public class Message implements Serializable{
	public int type;
	public String msg;
	
	public String info1;
	public String info2;
	public String info3;
	
	public int i_info1;
	public int i_info2;
	public int i_info3;
	
	public int from;
	
	//type=-1 just msg
	//type=0 login
	//type=1 force quit
	
	public Message(){
		type=0;
	}
	
	public Message(int type){
		this.type=type;
	}

}
