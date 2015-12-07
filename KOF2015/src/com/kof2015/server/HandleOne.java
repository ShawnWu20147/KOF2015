package com.kof2015.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import com.common.Message;

public class HandleOne implements Runnable {
	ObjectInputStream ois1,ois2;
	ObjectOutputStream oos1,oos2;
	String name1,name2;
	
	Vector<Message> msg_list;
	
	Thread t1;
	Thread t2;
	Thread t3;
	
	
	public HandleOne(ObjectInputStream ois1,ObjectInputStream ois2,ObjectOutputStream oos1,ObjectOutputStream oos2,String name1,String name2){
		this.ois1=ois1;
		this.oos1=oos1;
		this.ois2=ois2;
		this.oos2=oos2;
		this.name1=name1;
		this.name2=name2;
		msg_list=new Vector<Message>();
	}
	
	class Recv_1 implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Message msg=(Message) ois1.readObject();
					msg.from=1;
					msg_list.add(msg);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	class Recv_2 implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Message msg=(Message) ois2.readObject();
					msg.from=2;
					msg_list.add(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	class Handle_Msg implements Runnable{

		@Override
		public void run() {
			while (true){
				if (msg_list.size()>0){
					Message toh=msg_list.get(0);
					msg_list.remove(0);
					
					//following are the handle
					switch(toh.type){
					case 1:
						System.out.println("Impossible!");
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					}
				}
			}
			
		}
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		t1=new Thread(new Recv_1());
		t2=new Thread(new Recv_2());
		t3=new Thread(new Handle_Msg());
		
		t1.start();
		t2.start();
		t3.start();
		
		Message msg=new Message(-1);
		//following are choose pai
		double d1=Math.random();
		if (d1<0.5){
			System.out.println("player1["+name1+"]先手");
			msg.msg="player1["+name1+"]先手";
		}
		else{
			System.out.println("player2["+name2+"]先手");
			msg.msg="player2["+name2+"]先手";			
		}
		try {
			oos1.writeObject(msg);
			oos2.writeObject(msg);
			
			while (true){
				
			}
			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}

}
