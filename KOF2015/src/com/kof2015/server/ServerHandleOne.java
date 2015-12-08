package com.kof2015.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import com.common.FighterInfo;
import com.common.FighterInstance;
import com.common.Message;
import com.kof2015.client.ChooseFighter;

public class ServerHandleOne implements Runnable {
	ObjectInputStream ois1,ois2;
	ObjectOutputStream oos1,oos2;
	String name1,name2;
	
	Vector<Message> msg_list;
	
	Thread t1;
	Thread t2;
	Thread t3;
	
	ArrayList<FighterInfo> all_fighters;
	
	
	HashSet<Integer> all_cand;
	ArrayList<FighterInfo> can_fighters;
	
	volatile ChooseFighter []cf;
	
	volatile boolean p1_sel_over;
	volatile boolean p2_sel_over;
	
	volatile String extra_for_opp="";
	
	
	FighterInstance []p1_fi;
	FighterInstance []p2_fi;
	volatile boolean p1_format_over;
	volatile boolean p2_format_over;
	
	
	public ServerHandleOne(ArrayList<FighterInfo> all_fighters,ObjectInputStream ois1,ObjectInputStream ois2,ObjectOutputStream oos1,ObjectOutputStream oos2,String name1,String name2){
		this.all_fighters=all_fighters;
		this.ois1=ois1;
		this.oos1=oos1;
		this.ois2=ois2;
		this.oos2=oos2;
		this.name1=name1;
		this.name2=name2;
		msg_list=new Vector<Message>();
		
		all_cand=new HashSet<Integer>();
		can_fighters=new ArrayList<FighterInfo>();
		
		p1_sel_over=false;
		p2_sel_over=false;
		
		p1_format_over=false;
		p2_format_over=false;
		
	}
	
	class Recv_1 implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Thread.currentThread().sleep(20);
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
						//considering selecting fighters
						
						int who=toh.identifier;
						
						int nums=toh.i_info1;
						String debug_pl=name1;
						if (who==2) debug_pl=name2;
						int first=toh.i_info2;
						cf[first].selected=true;
						
						cf[first].who_select=who;
						
						String debug_sel="【"+debug_pl+"】"+"选择了:["+cf[first].fi.name+"]";
						if (nums==2){
							int second=toh.i_info3;
							debug_sel+=" && ["+cf[second].fi.name+"]";
							cf[second].selected=true;
							cf[second].who_select=who;
						}
						System.out.println(debug_sel);
						
						extra_for_opp="对手"+debug_sel+"\n";
						
						for (ChooseFighter cc:cf){
							System.out.println(cc.fi.name+" "+cc.selected+" "+cc.who_select);
						}
						
						if (who==1){
							p1_sel_over=true;
						}
						else{
							p2_sel_over=true;
						}
						
						break;
					case 3:
						
						FighterInfo []fi=toh.fi;
						assert(fi.length==6);
						if (toh.from==1){
							p1_fi=new FighterInstance[6];
							for (int i=0;i<6;i++){
								p1_fi[i]=new FighterInstance(fi[i], 0);
							}
							System.out.println("【"+name1+"】布阵完毕!");
							p1_format_over=true;
							if (p2_format_over){
								GO_TO_BATTLE();
							}
						}
						else if (toh.from==2){
							p2_fi=new FighterInstance[6];
							for (int i=0;i<6;i++){
								p2_fi[i]=new FighterInstance(fi[i], 0);
							}
							System.out.println("【"+name1+"】布阵完毕!");
							p2_format_over=true;
							if (p1_format_over){
								GO_TO_BATTLE();
							}
						}
						else{
							System.out.println("MEET A BUG!!!!!!!");
							System.exit(-1);
						}
						
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
			oos1.writeUnshared(msg);
			oos1.reset();
			oos2.writeUnshared(msg);
			oos2.reset();
			//the up 2 are useless
			
			int MAX_SEL=all_fighters.size();
			for (int i=0;i<16;i++){
				int cur=(int)(Math.random()*(double)MAX_SEL);
				while (all_cand.contains(cur)){
					cur=(int)(Math.random()*(double)MAX_SEL);
				}
				all_cand.add(cur);
				can_fighters.add(all_fighters.get(cur));
			}
			
			// now all_cand && can_fighters are with size 16
			cf=new ChooseFighter[16];
			for (int i=0;i<16;i++){
				cf[i]=new ChooseFighter(can_fighters.get(i),false,-2);
			}
			
			Message msg_both=new Message(2);
			msg_both.i_info1=-1;
			msg_both.cf=cf;
			if (d1>=0.5)
				msg_both.s_info1="你【后手】所以先选择，选择1张\n";
			else
				msg_both.s_info1="你【先手】所以后选择\n";
			
			oos1.writeUnshared(msg_both);
			oos1.reset();
			
			if (d1<0.5)
				msg_both.s_info1="你【后手】所以先选择，选择1张\n";
			else
				msg_both.s_info1="你【先手】所以后选择\n";			
			
			oos2.writeUnshared(msg_both);
			oos2.reset();
			
			Thread.currentThread().sleep(1000);
			
			if (d1>0.5){	
				for (int i=0;i<5;i++){
					Message msg1=new Message(2);
					if (i==0 || i==4) msg1.i_info3=1;
					else msg1.i_info3=2;
					msg1.i_info1=0;msg1.i_info2=1;
					msg1.cf=cf;
					msg1.s_info1="轮到你选择了,选择"+String.valueOf(msg1.i_info3)+"张\n";
					
					if (i!=0)
						msg1.s_info2=extra_for_opp;	//显示在别人的框子里
					else
						msg1.s_info2="";
					
					oos1.writeUnshared(msg1);
					oos1.reset();
					while(!p1_sel_over) Thread.currentThread().sleep(20);
					p1_sel_over=false;
					
					Message msg2=new Message(2);
					if (i==0 || i==4) msg2.i_info3=1;
					else msg2.i_info3=2;
					msg2.i_info1=0;msg2.i_info2=2;
					msg2.cf=cf;
					
					msg2.s_info1="轮到你选择了,选择"+String.valueOf(msg2.i_info3)+"张\n";
					msg2.s_info2=extra_for_opp;	//显示在别人的框子里
					
					oos2.writeUnshared(msg2);
					oos2.reset();
					while (!p2_sel_over) Thread.currentThread().sleep(20);
					p2_sel_over=false;

				}
			}
			else{
				for (int i=0;i<5;i++){		
					Message msg2=new Message(2);
					if (i==0 || i==4) msg2.i_info3=1;
					else msg2.i_info3=2;
					msg2.i_info1=0;msg2.i_info2=2;
					msg2.cf=cf;	
					msg2.s_info1="轮到你选择了,选择"+String.valueOf(msg2.i_info3)+"张\n";		
					if (i!=0)
						msg2.s_info2=extra_for_opp;	//显示在别人的框子里
					else
						msg2.s_info2="";
					
					oos2.writeUnshared(msg2);
					oos2.reset();
					while(!p2_sel_over) Thread.currentThread().sleep(20);
					p2_sel_over=false;
					
					Message msg1=new Message(2);
					if (i==0 || i==4) msg1.i_info3=1;
					else msg1.i_info3=2;
					msg1.i_info1=0;msg1.i_info2=1;
					msg1.cf=cf;
					
					msg1.s_info1="轮到你选择了,选择"+String.valueOf(msg1.i_info3)+"张\n";
					msg1.s_info2=extra_for_opp;	//显示在别人的框子里
					
					
					oos1.writeUnshared(msg1);
					oos1.reset();
					while (!p1_sel_over) Thread.currentThread().sleep(20);
					p1_sel_over=false;
					

				}
			}
			
			//通知双方结束选择
			Message msg_over=new Message(2);
			msg_over.i_info1=1;
			msg_over.cf=cf;
			
			if (d1<0.5){
				//1先手,后选择,最后一个也是它选的，所以2要知道对面选了什么
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2=extra_for_opp;
				oos2.writeUnshared(msg_over);
				oos2.flush();
				
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2="";
				oos1.writeUnshared(msg_over);
				oos1.flush();
			}
			else{
				//2先手,后选择,最后一个也是它选的，所以1要知道对面选了什么
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2=extra_for_opp;
				oos1.writeUnshared(msg_over);
				oos1.flush();
				
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2="";
				oos2.writeUnshared(msg_over);
				oos2.flush();
			}
			
			//now here means select over!!!!
			
			FighterInfo []p1f=new FighterInfo[8];
			FighterInfo []p2f=new FighterInfo[8];
			int index_p1=0,index_p2=0;
			for (int i=0;i<16;i++){
				if (cf[i].who_select==1){
					p1f[index_p1++]=cf[i].fi;
				}
				else{
					p2f[index_p2++]=cf[i].fi;
				}
			}
			Message msg_both_send=new Message(3);
			msg_both_send.fi=p1f;
			oos1.writeUnshared(msg_both_send);
			
			msg_both_send.fi=p2f;
			oos2.writeUnshared(msg_both_send);
			
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}


	public void GO_TO_BATTLE() {
		System.out.println("双方布阵完毕!准备战斗!");
		//using p1_fi[] and p2_fi[] which are FighterInstance
		// first send them to clients, with type=4 subtype=-1 means start
		
	}

}
